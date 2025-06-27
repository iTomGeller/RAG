package com.cyberlanting.qwen_rag.service.impl;

import com.aliyuncs.utils.StringUtils;
import com.cyberlanting.qwen_rag.common.exception.ParamaterErrorException;
import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.common.util.FileUtils;
import com.cyberlanting.qwen_rag.mapper.FileMapper;
import com.cyberlanting.qwen_rag.common.context.BaseContext;
import com.cyberlanting.qwen_rag.common.exception.NotLoginException;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.common.util.AliOSSUtils;
import com.cyberlanting.qwen_rag.mapper.KnowledgeBaseMapper;
import com.cyberlanting.qwen_rag.pojo.entity.File;
import com.cyberlanting.qwen_rag.pojo.vo.FileVO;
import com.cyberlanting.qwen_rag.service.Assistant;
import com.cyberlanting.qwen_rag.service.FileService;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    private FileMapper FileMapper;

    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Value("${qwen-rag.hyj-rag.ip}")
    private String ragServerIp;


    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    public Result<FileVO> uploadFile(MultipartFile file, Long knowledgeBaseId) throws FileUploadException {
        log.info("文件上传：{}", file);
        try {
            //原始文件名
            /*首先通过file.getOriginalFilename()获取原始文件名*/
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   dfdfdf.png
            /*然后通过originalFilename.lastIndexOf(".")获取文件名的后缀。*/
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            /*使用UUID.randomUUID().toString()生成一个随机的文件名，并将后缀拼接在文件名后面，构造出新的文件名。*/
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            /*然后，调用aliOssUtil.upload方法将文件上传到OSS，并获取文件的请求路径。*/
            String filePath = aliOSSUtils.upload(file.getBytes(), objectName);

            File filePersisted = persistFile(originalFilename, filePath, knowledgeBaseId);

            FileVO fileVO = new FileVO();
            BeanUtils.copyProperties(filePersisted, fileVO);

            /*最后，返回一个Result对象，其中包含上传文件的请求路径。*/
            return Result.success(fileVO);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return Result.error("文件上传失败");
    }

    public PageResult getFileList(Integer page, Integer pageSize, Long knowledgeBaseId) {
        Long userId = getUserId();
        if (!knowledgeBaseMapper.isBelongsToUser(userId, knowledgeBaseId)) {
            throw new ParamaterErrorException("知识库不存在或不属于该用户");
        }
        Integer total = FileMapper.getTotal(userId, knowledgeBaseId);
        // 处理total为0的情况
        Integer totalPage = total == 0 ? 0 : (total - 1) / pageSize + 1;
        // 当total为0时，只有page=1是合法的（或无数据情况）
        if (page < 1 || (total > 0 && page > totalPage)) {
            throw new ParamaterErrorException("页码错误");
        }
        Integer offset = (page - 1) * pageSize;
        List<FileVO> fileList = total == 0 ? Collections.emptyList() : FileMapper.getFileList(knowledgeBaseId, offset, pageSize);
        return new PageResult(totalPage, total, fileList);
    }

    public Result uploadAvatar(MultipartFile file) {
        log.info("文件上传：{}", file);
        try {
            //原始文件名
            /*首先通过file.getOriginalFilename()获取原始文件名*/
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   dfdfdf.png
            /*然后通过originalFilename.lastIndexOf(".")获取文件名的后缀。*/
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            /*使用UUID.randomUUID().toString()生成一个随机的文件名，并将后缀拼接在文件名后面，构造出新的文件名。*/
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            /*然后，调用aliOssUtil.upload方法将文件上传到OSS，并获取文件的请求路径。*/
            String filePath = aliOSSUtils.upload(file.getBytes(), objectName);
            /*最后，返回一个Result对象，其中包含上传文件的请求路径。*/
            return Result.success((Object)filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return Result.error("文件上传失败");
    }

    @Override
    public Result<FileVO> uploadFileWithClassification(MultipartFile file) {
        String content = "";
        try {
            content = FileUtils.parseFileToText(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (content == null || content.isEmpty()) {
            return Result.error("文件上传失败");
        }
        String prompt = "请将以下文档内容分类到指定的知识库类别中。可用类别及 ID：\n" +
                "1. 理科知识库\n2. 医学/健康知识库\n3. 其他\n4. 文科知识库\n5. 社科知识库\n6. 工科/应用科学知识库\n\n" +
                "文档内容：\n" + content + "\n\n" +
                "**必须只返回数字 1-6**，不要包含任何其他文字或解释。如果无法确定，返回 3。";

        String aiResponse = openAiChatModel.chat(prompt).trim();
        log.info("AI 分类响应: {}", aiResponse);

        Long knowledgeBaseId;
        try {
            knowledgeBaseId = Long.parseLong(aiResponse);
            if (knowledgeBaseId < 1 || knowledgeBaseId > 6) {
                knowledgeBaseId = 3L;  // 超出范围则默认 3
            }
        } catch (NumberFormatException e) {
            knowledgeBaseId = 3L;  // 解析失败则默认 3
            log.error("AI 返回无效分类: {}", aiResponse);
        }
        try {
            Result<FileVO> fileVO = uploadFile(file, knowledgeBaseId);
            if (fileVO.getData() == null) {
                return Result.error("文件上传失败");
            }
            persistFileToVectorDB(content, fileVO.getData().getName(), fileVO.getData().getUrl());
            return fileVO;
        } catch (FileUploadException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从OSS URL中提取对象Key
     */
    private String extractObjectNameFromUrl(String url) {
        // 统一处理HTTP和HTTPS情况
        String httpRemoved = url.replace("https://", "").replace("http://", "");

        // 找到第一个斜杠后的所有内容就是objectKey
        int firstSlashIndex = httpRemoved.indexOf("/");
        if (firstSlashIndex == -1) {
            return ""; // 如果URL格式异常，返回空字符串
        }

        return httpRemoved.substring(firstSlashIndex + 1);
    }

    public File persistFile(String originName, String url, Long knowledgeBaseId) {
        log.info("文件保存：{}, {}", originName, url);
        Long userId = getUserId();
        if (!knowledgeBaseMapper.isBelongsToUser(userId, knowledgeBaseId)) {
            throw new ParamaterErrorException("知识库不存在或不属于该用户");
        }
        File file = File.builder()
                .knowledgeBaseId(knowledgeBaseId)
                .name(originName)
                .url(url)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        FileMapper.insert(file);
        return file;
    }

//    // 异步方法
//    private void persistFileToVectorDB(String document, String title, String url) {
//        // 1. 创建WebClient
//        WebClient client = WebClient.create("http://127.0.0.1:5000");
//
//        // 2. 准备请求体
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("document", document);
//
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("title", title);
//        metadata.put("url", url);
//        requestBody.put("metadata", metadata);
//
//        // 3. 发送异步请求
//        Mono<String> response = client.post()
//                .uri("/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class);
//
//        // 4. 处理响应（异步）
//        response.subscribe(
//                result -> System.out.println("存储成功: " + result),
//                error -> System.out.println("存储失败: " + error.getMessage())
//        );
//    }

    // 同步方法
    public void persistFileToVectorDB(String document, String title, String url) {
        // 1. 准备请求URL和请求体
        String apiUrl = ragServerIp + "/save";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("document", document);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("title", title);
        metadata.put("url", url);
        requestBody.put("metadata", metadata);

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

        // 4. 处理响应
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("存储成功: {}", response.getBody());
        } else {
            log.info("存储失败: {}", response.getStatusCode());
        }
    }

    @Override
    public Result deleteFile(Long id) {
        // 1. 从数据库获取文件信息
        File file = FileMapper.getFileById(id);
        if (file == null) {
            return Result.error("文件不存在");
        }

        // 2. 获取文件URL
        String url = file.getUrl();
        if (StringUtils.isEmpty(url)) {
            FileMapper.delete(id); // 如果URL为空，只删除数据库记录
            return Result.success();
        }

        try {
            // 3. 从URL中提取对象Key
            String objectName = extractObjectNameFromUrl(url);
            if (StringUtils.isEmpty(objectName)) {
                log.warn("无法从URL中提取对象Key，URL: {}", url);
                FileMapper.delete(id);
                return Result.success();
            }

            // 4. 调用OSS删除文件
            aliOSSUtils.delete(objectName);

            // 5. 删除数据库记录
            FileMapper.delete(id);

            return Result.success();
        } catch (Exception e) {
            log.error("删除文件失败，文件ID：{}，URL：{}", id, url, e);
            // 即使OSS删除失败，也删除数据库记录（可根据业务需求调整）
            FileMapper.delete(id);
            return Result.error("删除文件失败，但已移除数据库记录");
        }
    }


}
