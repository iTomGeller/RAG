package com.cyberlanting.qwen_rag.controller;

import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.Chat;
import com.cyberlanting.qwen_rag.service.Assistant;
import com.cyberlanting.qwen_rag.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private OpenAiChatModel aiChatModel;

    @Autowired
    private OpenAiStreamingChatModel aiStreamingChatModel;

    @PostMapping(produces = "text/html;charset=utf-8")
    public Flux<String> chat(String memoryId, String message) throws JsonProcessingException {
        message = chatService.queryAndEnhancedPrompt(message);
//        log.info("enhanced message: {}", message);
//        String resultTest = aiChatModel.chat(message);
//        log.info("result: {}", resultTest);
        Flux<String> result = assistant.chat(memoryId, message);
        return result;
    }

    @GetMapping("/list")
    public Result<List<Chat>> getChatList() {
        return chatService.getChatList();
    }

}
