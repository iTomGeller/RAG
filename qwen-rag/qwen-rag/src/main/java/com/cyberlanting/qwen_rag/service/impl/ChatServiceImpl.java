package com.cyberlanting.qwen_rag.service.impl;


import com.cyberlanting.qwen_rag.common.context.BaseContext;
import com.cyberlanting.qwen_rag.common.exception.NotLoginException;
import com.cyberlanting.qwen_rag.pojo.dto.ChatMessageDTO;
import com.cyberlanting.qwen_rag.pojo.entity.Feedback;
import com.cyberlanting.qwen_rag.pojo.vo.DocumentInfoVO;
import com.cyberlanting.qwen_rag.pojo.vo.FeedbackVO;
import com.cyberlanting.qwen_rag.pojo.vo.FileVO;
import com.cyberlanting.qwen_rag.repository.RedisChatMemoryStore;
import com.cyberlanting.qwen_rag.service.FeedbackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.Chat;
import com.cyberlanting.qwen_rag.pojo.entity.DocumentInfo;
import com.cyberlanting.qwen_rag.service.ChatService;
import dev.langchain4j.data.message.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${qwen-rag.hyj-rag.ip}")
    private String ragServerIp;

    @Autowired
    private RedisChatMemoryStore redisChatMemoryStore;

    @Autowired
    private FeedbackService feedbackService;

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

//    @Override
//    public Result<List<Chat>> getChatList() {
//        Set<String> keys = new HashSet<>();
//        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(ScanOptions.scanOptions().match("OTTAR:MEMORY:*").build());
//        while (cursor.hasNext()) {
//            keys.add(new String(cursor.next()));
//        }
//
//        List<Chat> chatList = new ArrayList<>();
//
//        for (String key : keys) {
//            // 提取 memoryId
//            String memoryIdStr = key.replace("OTTAR:MEMORY:", "");
//            Long memoryId = Long.valueOf(memoryIdStr);
//
//            // 构造 Chat 对象，name 可能需要额外处理，比如从数据库或其他缓存中查
//            Chat chat = new Chat();
//            chat.setMemoryId(memoryId);
//            chat.setName("会话" + memoryId); // 示例默认名，可根据业务逻辑替换
//            chatList.add(chat);
//        }
//
//        return Result.success(chatList); // 使用你自己的 Result 工具类
//    }

    @Override
    public Result<List<Chat>> getChatList() {
        Long userId = getUserId();
        Set<String> keys = new HashSet<>();

        // Modify the scan pattern to include the user ID
        String scanPattern = "OTTER:MEMORY:" + userId + ":*";
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection()
                .scan(ScanOptions.scanOptions().match(scanPattern).build());

        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }

        List<Chat> chatList = new ArrayList<>();

        for (String key : keys) {
            // Extract memoryId from the key (format: OTTAR:MEMORY:userId:memoryId)
            String[] parts = key.split(":");
            if (parts.length >= 4) {
                try {
                    Long memoryId = Long.valueOf(parts[3]);

                    Chat chat = new Chat();
                    chat.setMemoryId(memoryId);
                    chat.setName("会话" + memoryId); // You can customize this as needed
                    chatList.add(chat);
                } catch (NumberFormatException e) {
                    // Handle invalid memoryId format if needed
                    continue;
                }
            }
        }

        return Result.success(chatList);
    }

    @Override
    public List<DocumentInfoVO> queryAndEnhancedPrompt(String userQuery) throws JsonProcessingException {
        // 1. 准备请求URL和请求体
        String apiUrl = ragServerIp + "/query";

        log.info("query: " + apiUrl);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", userQuery);

        // 2. 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. 发送POST请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(
                    apiUrl,
                    new HttpEntity<>(requestBody, headers),
                    String.class
            );
        } catch (Exception e) {
            log.error("Query failed with exception: " + e.getMessage());
            return Collections.emptyList(); // 返回空列表
        }

        // 4. 处理响应
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
                Map<String, Object> responseMessage = (Map<String, Object>) responseBody.get("message");

                List<List<String>> documentsOrigin = (List<List<String>>) responseMessage.get("documents");
                // 5. 提取文档信息
                List<String> documents = documentsOrigin.get(0);
                List<String> titles = (List<String>) responseMessage.get("title");
                List<String> urls = (List<String>) responseMessage.get("url");

                // 6. 封装结果
                List<DocumentInfo> docInfos = new ArrayList<>();
                List<DocumentInfoVO> documentInfoVOS = new ArrayList<>();
                for (int i = 0; i < documents.size(); i++) {
                    docInfos.add(new DocumentInfo(
                            titles.get(i),
                            urls.get(i),
                            documents.get(i)
                    ));
                    documentInfoVOS.add(new DocumentInfoVO(
                            titles.get(i),
                            urls.get(i)
                    ));
                }
                // 6. 增强用户Prompt
                userQuery = buildEnhancedPrompt(userQuery, docInfos);
                return documentInfoVOS;
            } catch (Exception e) {
                log.error("Error parsing response: " + e.getMessage());
                return Collections.emptyList(); // 返回空列表
            }
        } else if (response.getStatusCode().value() == 500) {
            log.error("Server returned 500 error: " + response.getBody());
            return Collections.emptyList(); // 返回空列表
        } else {
            throw new RuntimeException("Query failed: " + response.getBody());
        }
    }

    public Result getChatContext(String memoryId) {
        List<ChatMessage> chatMessages = redisChatMemoryStore.getMessages(memoryId);
        return Result.success(chatMessages);
    }

    public Result deleteChat(String memoryId) {
        redisChatMemoryStore.deleteMessages(memoryId);
        return Result.success("删除成功");
    }

    @Override
    public String getUserFeedback() {
        StringBuilder sb = new StringBuilder();
        List<FeedbackVO> feedbackVOList = feedbackService.getFeedbackList(1, 10).getList();
        for (FeedbackVO feedbackVO : feedbackVOList) {
            sb.append("- ").append(feedbackVO.getContent()).append("\n");
        }
        return sb.toString();
    }

    private String buildEnhancedPrompt(String originalQuery, List<DocumentInfo> docInfos) {
        StringBuilder sb = new StringBuilder();
        sb.append(originalQuery).append("\n\n");
        sb.append("结合以下的相关参考文档进行回答:\n");

        for (int i = 0; i < docInfos.size(); i++) {
            DocumentInfo doc = docInfos.get(i);
            sb.append(i + 1).append(". ").append(doc.getTitle()).append("\n");
            sb.append("   摘要: ").append(doc.getContent()).append("\n\n");
        }

        return sb.toString();
    }

}
