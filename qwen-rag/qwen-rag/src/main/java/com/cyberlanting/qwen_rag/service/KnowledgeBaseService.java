package com.cyberlanting.qwen_rag.service;


import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.pojo.dto.KnowledgeBaseDTO;
import com.cyberlanting.qwen_rag.pojo.vo.KnowledgeBaseVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public interface KnowledgeBaseService {
    KnowledgeBaseVO createKnowledgeBase(KnowledgeBaseDTO knowledgeBaseDTO);

    PageResult getKnowledgeBaseList(Integer page, Integer pageSize);

    void initializeKnowledgeBase(Long userId);
}
