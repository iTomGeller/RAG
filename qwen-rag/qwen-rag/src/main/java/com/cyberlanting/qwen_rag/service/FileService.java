package com.cyberlanting.qwen_rag.service;

import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.vo.FileVO;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Result uploadFile(MultipartFile file, Long knowledgeBaseId) throws FileUploadException;

    PageResult getFileList(Integer page, Integer pageSize, Long knowledgeBaseId);

    Result uploadAvatar(MultipartFile file);

    Result deleteFile(Long id);

    Result<FileVO> uploadFileWithClassification(MultipartFile file);
}
