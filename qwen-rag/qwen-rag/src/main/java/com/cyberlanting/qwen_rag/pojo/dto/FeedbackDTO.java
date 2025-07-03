package com.cyberlanting.qwen_rag.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FeedbackDTO {
    private Long userId;
    private String content;
}
