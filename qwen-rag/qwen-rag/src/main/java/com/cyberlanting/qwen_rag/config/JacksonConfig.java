package com.cyberlanting.qwen_rag.config;

import com.cyberlanting.qwen_rag.common.util.ChatMessageSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.langchain4j.data.message.ChatMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Module chatMessageModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ChatMessage.class, new ChatMessageSerializer());
        return module;
    }
}