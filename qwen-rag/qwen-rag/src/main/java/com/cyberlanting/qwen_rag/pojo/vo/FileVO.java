package com.cyberlanting.qwen_rag.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileVO {
    private Long id;
    private Long knowledgeBaseId;
    private String name;
    private String url;
    private LocalDateTime createdAt;
}
