package com.cyberlanting.qwen_rag.controller;

import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.Chat;
import com.cyberlanting.qwen_rag.pojo.entity.DocumentInfo;
import com.cyberlanting.qwen_rag.pojo.vo.DocumentInfoVO;
import com.cyberlanting.qwen_rag.service.Assistant;
import com.cyberlanting.qwen_rag.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private Assistant assistant;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;


//    @RequestMapping(produces = "text/html;charset=utf-8")
//    public Flux<String> chat(String memoryId, String message) throws JsonProcessingException {
//        chatService.queryAndEnhancedPrompt(message);
//        log.info("enhanced message: {}", message);
////        String resultTest = aiChatModel.chat(message);
////        log.info("result: {}", resultTest);
//        Flux<String> result = assistant.chat(memoryId, message);
//        return result;
//    }

    @PostMapping(produces = "text/event-stream")
    public Flux<String> chat(String memoryId, String message) throws JsonProcessingException {
        // 阶段1：准备数据源
        List<DocumentInfoVO> documentInfos = chatService.queryAndEnhancedPrompt(message);


        String sourcesJson = objectMapper.writeValueAsString(documentInfos);
        log.info("sources: {}", sourcesJson);

        // 阶段2：流式生成回复内容
        Flux<String> contentStream = assistant.chat(memoryId, message);

        return Flux.concat(
                Flux.just("SOURCES:" + sourcesJson + "\n"),  // 数据源标记
                Flux.just("CONTENT:\n"),                     // 内容开始标记（只出现一次）
                contentStream,                               // 原始内容流（无额外标记）
                Flux.just("END\n")                           // 结束标记
        );
    }

    @GetMapping("/list")
    public Result<List<Chat>> getChatList() {
        return chatService.getChatList();
    }

}
