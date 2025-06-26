package com.cyberlanting.qwen_rag.service.impl;

import com.aliyuncs.utils.StringUtils;
import com.cyberlanting.qwen_rag.common.exception.ParamaterErrorException;
import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.mapper.FileMapper;
import com.cyberlanting.qwen_rag.common.context.BaseContext;
import com.cyberlanting.qwen_rag.common.exception.NotLoginException;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.common.util.AliOSSUtils;
import com.cyberlanting.qwen_rag.mapper.KnowledgeBaseMapper;
import com.cyberlanting.qwen_rag.pojo.entity.File;
import com.cyberlanting.qwen_rag.pojo.vo.FileVO;
import com.cyberlanting.qwen_rag.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    private FileMapper FileMapper;

    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

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

}
