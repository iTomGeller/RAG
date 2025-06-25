package com.cyberlanting.qwen_rag.mapper;

import com.cyberlanting.qwen_rag.pojo.entity.KnowledgeBase;
import com.cyberlanting.qwen_rag.pojo.vo.KnowledgeBaseVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KnowledgeBaseMapper {
    void insert(KnowledgeBase knowledgeBase);

    Boolean isBelongsToUser(Long userId, Long knowledgeBaseId);

    List<KnowledgeBaseVO> getKnowledgeBaseList(Long userId, Integer offset, Integer pageSize);

    Integer getTotal(Long userId);
}
