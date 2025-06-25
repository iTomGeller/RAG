package com.cyberlanting.qwen_rag.service.impl;


import com.cyberlanting.qwen_rag.common.context.BaseContext;
import com.cyberlanting.qwen_rag.common.exception.NotLoginException;
import com.cyberlanting.qwen_rag.common.exception.ParamaterErrorException;
import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.mapper.KnowledgeBaseMapper;
import com.cyberlanting.qwen_rag.pojo.dto.KnowledgeBaseDTO;
import com.cyberlanting.qwen_rag.pojo.entity.KnowledgeBase;
import com.cyberlanting.qwen_rag.pojo.vo.KnowledgeBaseVO;
import com.cyberlanting.qwen_rag.service.KnowledgeBaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    public KnowledgeBaseVO createKnowledgeBase(KnowledgeBaseDTO knowledgeBaseDTO) {
        Long userId = getUserId();
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        BeanUtils.copyProperties(knowledgeBaseDTO, knowledgeBase);
        if (knowledgeBase.getName() == null || knowledgeBase.getName().isEmpty()) {
            throw new ParamaterErrorException("知识库未命名");
        }
        knowledgeBase.setUserId(userId);
        if (knowledgeBase.getType() == null || knowledgeBase.getType().isEmpty() ) {
            knowledgeBase.setType("Default");
        }
        knowledgeBase.setCreatedAt(LocalDateTime.now());
        knowledgeBase.setUpdatedAt(LocalDateTime.now());
        knowledgeBaseMapper.insert(knowledgeBase);
        KnowledgeBaseVO knowledgeBaseVO = new KnowledgeBaseVO();
        BeanUtils.copyProperties(knowledgeBase, knowledgeBaseVO);
        return knowledgeBaseVO;
    }

    public PageResult getKnowledgeBaseList(Integer page, Integer pageSize) {
        Long userId = getUserId();
        Integer total = knowledgeBaseMapper.getTotal(userId);
        Integer totalPage = (total - 1) / pageSize + 1;
        if (page < 1 || page > totalPage) {
            throw new ParamaterErrorException("页码错误");
        }
        Integer offset = (page - 1) * pageSize;
        List<KnowledgeBaseVO> knowledgeBaseList = knowledgeBaseMapper.getKnowledgeBaseList(userId, offset, pageSize);
        return new PageResult(totalPage, total, knowledgeBaseList);
    }
}
