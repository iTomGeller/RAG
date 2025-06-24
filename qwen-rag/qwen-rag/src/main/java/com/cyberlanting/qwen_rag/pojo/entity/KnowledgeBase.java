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
public class KnowledgeBase {
    private Long id;
    private Long userId;  // 关联用户ID
    private String name;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}