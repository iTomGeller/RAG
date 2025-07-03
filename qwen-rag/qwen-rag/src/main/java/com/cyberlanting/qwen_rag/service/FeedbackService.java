package com.cyberlanting.qwen_rag.service;

import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.dto.FeedbackDTO;
import com.cyberlanting.qwen_rag.pojo.vo.FeedbackVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FeedbackService {
    Result<FeedbackVO> addFeedback(FeedbackDTO feedbackDTO);

    PageResult getFeedbackList(Integer page, Integer pageSize);

    Result deleteFeedback(Long id);
}
