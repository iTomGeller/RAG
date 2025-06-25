package com.cyberlanting.qwen_rag.controller;


import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.dto.KnowledgeBaseDTO;
import com.cyberlanting.qwen_rag.pojo.vo.KnowledgeBaseVO;
import com.cyberlanting.qwen_rag.service.KnowledgeBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/knowledgebase")
@Slf4j
public class KnowledgeBaseController {

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @PostMapping
    public Result<KnowledgeBaseVO> createKnowledgeBase(@RequestBody KnowledgeBaseDTO knowledgeBaseDTO) {
        KnowledgeBaseVO knowledgeBaseVO = knowledgeBaseService.createKnowledgeBase(knowledgeBaseDTO);
        return Result.success(knowledgeBaseVO);
    }

    @GetMapping
    public PageResult getKnowledgeBaseList(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return knowledgeBaseService.getKnowledgeBaseList(page, pageSize);
    }
}
