package com.cyberlanting.qwen_rag.mapper;

import com.cyberlanting.qwen_rag.pojo.entity.Feedback;
import com.cyberlanting.qwen_rag.pojo.vo.FeedbackVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    void insert(Feedback feedback);

    List<FeedbackVO> getFeedbackList(Long userId, Integer offset, Integer pageSize);

    Integer getTotal(Long userId);

    boolean isExist(Long userId, Long id);

    void delete(Long id);
}
