package com.cyberlanting.qwen_rag.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {
    private Long id;
    private Long knowledgeBaseId;  // 关联知识库ID
    private String name;
    private String url;
    private LocalDateTime createdAt;
}