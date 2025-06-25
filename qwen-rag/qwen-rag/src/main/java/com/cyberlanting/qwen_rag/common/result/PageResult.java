package com.cyberlanting.qwen_rag.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult {
    private Integer totalPage;
    private Integer total;
    private List list;
}