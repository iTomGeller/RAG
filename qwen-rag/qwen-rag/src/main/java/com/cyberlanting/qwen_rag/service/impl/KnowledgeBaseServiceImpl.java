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
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        // 处理total为0的情况
        Integer totalPage = total == 0 ? 0 : (total - 1) / pageSize + 1;
        // 当total为0时，只有page=1是合法的（或无数据情况）
        if (page < 1 || (total > 0 && page > totalPage)) {
            throw new ParamaterErrorException("页码错误");
        }
        Integer offset = (page - 1) * pageSize;
        List<KnowledgeBaseVO> knowledgeBaseList = total == 0 ? Collections.emptyList()
                : knowledgeBaseMapper.getKnowledgeBaseList(userId, offset, pageSize);
        return new PageResult(totalPage, total, knowledgeBaseList);
    }

    public void initializeKnowledgeBase(Long userId) {
        // 创建知识库
        // 知识库名称列表：文科知识库 (Humanities)、理科知识库 (Science)、工科/应用科学知识库 (Engineering)、医学/健康知识库 (Health)、社科知识库 (Social)
        Map<String, String> knowledgeBaseNameList = Map.of(
                "Humanities", "文科知识库",
                "Science", "理科知识库",
                "Engineering", "工科/应用科学知识库",
                "Health", "医学/健康知识库",
                "Social", "社科知识库",
                "Default", "其他"
        );
        for (Map.Entry<String, String> entry : knowledgeBaseNameList.entrySet()) {
            KnowledgeBase knowledgeBase = KnowledgeBase.builder()
                    .type(entry.getKey())
                    .name(entry.getValue())
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            knowledgeBaseMapper.insert(knowledgeBase);
        }
    }
}
