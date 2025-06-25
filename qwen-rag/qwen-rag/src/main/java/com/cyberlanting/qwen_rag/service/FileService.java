package com.cyberlanting.qwen_rag.service;

import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.common.result.Result;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Result uploadFile(MultipartFile file, Long knowledgeBaseId) throws FileUploadException;

    PageResult getFileList(Integer page, Integer pageSize, Long knowledgeBaseId);

    Result uploadAvatar(MultipartFile file);
}
