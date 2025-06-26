package com.cyberlanting.qwen_rag.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentInfo {
    private String title;
    private String url;
    private String content;
}
