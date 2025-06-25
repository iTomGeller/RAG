package com.cyberlanting.qwen_rag.service.impl;

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
        Integer totalPage = (total - 1) / pageSize + 1;
        if (page < 1 || page > totalPage) {
            throw new ParamaterErrorException("页码错误");
        }
        Integer offset = (page - 1) * pageSize;
        List<FileVO> fileList = FileMapper.getFileList(knowledgeBaseId, offset, pageSize);
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
