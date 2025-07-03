package com.cyberlanting.qwen_rag.service.impl;

import com.cyberlanting.qwen_rag.common.context.BaseContext;
import com.cyberlanting.qwen_rag.common.exception.NotLoginException;
import com.cyberlanting.qwen_rag.common.exception.ParamaterErrorException;
import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.mapper.FeedbackMapper;
import com.cyberlanting.qwen_rag.pojo.dto.FeedbackDTO;
import com.cyberlanting.qwen_rag.pojo.entity.Feedback;
import com.cyberlanting.qwen_rag.pojo.vo.FeedbackVO;
import com.cyberlanting.qwen_rag.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    public Result<FeedbackVO> addFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = new Feedback(null, getUserId(), feedbackDTO.getContent());
        feedbackMapper.insert(feedback);
        return Result.success(new FeedbackVO(feedback.getId(), feedback.getUserId(), feedback.getContent()));
    }

    public PageResult getFeedbackList(Integer page, Integer pageSize) {
        Long userId = getUserId();
        Integer total = feedbackMapper.getTotal(userId);
        // 处理total为0的情况
        Integer totalPage = total == 0 ? 0 : (total - 1) / pageSize + 1;
        // 当total为0时，只有page=1是合法的（或无数据情况）
        if (page < 1 || (total > 0 && page > totalPage)) {
            throw new ParamaterErrorException("页码错误");
        }
        Integer offset = (page - 1) * pageSize;
        List<FeedbackVO> feedbackList = feedbackMapper.getFeedbackList(userId, offset, pageSize);
        return new PageResult(totalPage, total, feedbackList);
    }

    public Result deleteFeedback(Long id) {
        Long userId = getUserId();
        if (!feedbackMapper.isExist(userId, id)) {
            return Result.error("反馈不存在");
        }
        feedbackMapper.delete(id);
        return Result.success("删除成功");
    }
}
