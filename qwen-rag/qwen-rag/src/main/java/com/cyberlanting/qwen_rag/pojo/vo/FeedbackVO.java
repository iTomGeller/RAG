package com.cyberlanting.qwen_rag.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackVO {
    private Long id;
    private Long userId;
    private String content;
}