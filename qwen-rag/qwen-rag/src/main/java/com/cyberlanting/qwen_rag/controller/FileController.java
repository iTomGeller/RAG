package com.cyberlanting.qwen_rag.controller;

import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.File;
import com.cyberlanting.qwen_rag.pojo.vo.FileVO;
import com.cyberlanting.qwen_rag.service.FileService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload/{knowledgeBaseId}")
    public Result<FileVO> uploadFile(MultipartFile file, @PathVariable Long knowledgeBaseId) throws FileUploadException {
        if (file == null || file.isEmpty()) {
            return Result.error("文件为空");
        }
        return fileService.uploadFile(file, knowledgeBaseId);
    }

    @PostMapping("/upload/avatar")
    public Result uploadAvatar(@RequestParam MultipartFile file) throws FileUploadException {
        if (file == null || file.isEmpty()) {
            return Result.error("文件为空");
        }
        return fileService.uploadAvatar(file);
    }

    @GetMapping("/{knowledgeBaseId}")
    public PageResult getFileList(@RequestParam Integer page, @RequestParam Integer pageSize, @PathVariable Long knowledgeBaseId) {
        return fileService.getFileList(page, pageSize, knowledgeBaseId);
    }

    @DeleteMapping
    public Result deleteFile(@RequestParam Long id) {
        return fileService.deleteFile(id);
    }
}
