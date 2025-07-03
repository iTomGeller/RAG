package com.cyberlanting.qwen_rag.controller;

import com.cyberlanting.qwen_rag.common.result.PageResult;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.dto.FeedbackDTO;
import com.cyberlanting.qwen_rag.pojo.entity.Feedback;
import com.cyberlanting.qwen_rag.pojo.vo.FeedbackVO;
import com.cyberlanting.qwen_rag.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public Result<FeedbackVO> addFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.addFeedback(feedbackDTO);
    }

    @GetMapping
    public PageResult getFeedbackList(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return feedbackService.getFeedbackList(page, pageSize);
    }

    @DeleteMapping
    public Result deleteFeedback(@RequestParam Long id) {
        return feedbackService.deleteFeedback(id);
    }

}
