package com.cyberlanting.qwen_rag.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.Chat;
import com.cyberlanting.qwen_rag.pojo.entity.DocumentInfo;
import com.cyberlanting.qwen_rag.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Result<List<Chat>> getChatList() {
        Set<String> keys = new HashSet<>();
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(ScanOptions.scanOptions().match("OTTAR:MEMORY:*").build());
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }

        List<Chat> chatList = new ArrayList<>();

        for (String key : keys) {
            // 提取 memoryId
            String memoryIdStr = key.replace("OTTAR:MEMORY:", "");
            Long memoryId = Long.valueOf(memoryIdStr);

            // 构造 Chat 对象，name 可能需要额外处理，比如从数据库或其他缓存中查
            Chat chat = new Chat();
            chat.setMemoryId(memoryId);
            chat.setName("会话" + memoryId); // 示例默认名，可根据业务逻辑替换
            chatList.add(chat);
        }

        return Result.success(chatList); // 使用你自己的 Result 工具类
    }

    @Override
    public String queryAndEnhancedPrompt(String userQuery) throws JsonProcessingException {
        // 1. 准备请求URL和请求体
        String apiUrl = "http://127.0.0.1:5000/query";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", userQuery);

        // 2. 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. 发送POST请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(
                apiUrl,
                new HttpEntity<>(requestBody, headers),
                String.class
        );

        // 4. 解析响应
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
            Map<String, Object> responseMessage = (Map<String, Object>) responseBody.get("message");

            // 5. 提取文档信息
            List<String> documents = (List<String>) responseMessage.get("documents");
            List<String> titles = (List<String>) responseMessage.get("title");
            List<String> urls = (List<String>) responseMessage.get("url");

            // 6. 封装结果
            List<DocumentInfo> docInfos = new ArrayList<>();
            for (int i = 0; i < documents.size(); i++) {
                docInfos.add(new DocumentInfo(
                        titles.get(i),
                        urls.get(i),
                        documents.get(i)
                ));
            }
            // 6. 增强用户Prompt
            return buildEnhancedPrompt(userQuery, docInfos);
        } else {
            throw new RuntimeException("Query failed: " + response.getBody());
        }
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
