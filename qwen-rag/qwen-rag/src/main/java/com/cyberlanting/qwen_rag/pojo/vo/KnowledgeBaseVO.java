package com.cyberlanting.qwen_rag.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KnowledgeBaseVO {
    private Long id;
    private Long userId;  // 关联用户ID
    private String name;
    private String type;
    private LocalDateTime createdAt;
}
