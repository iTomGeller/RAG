package com.cyberlanting.qwen_rag.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.agent.tool.ToolExecutionRequest;

import java.io.IOException;
import java.util.List;

public class ChatMessageSerializer extends JsonSerializer<ChatMessage> {
    @Override
    public void serialize(ChatMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", value.type().toString());

        if (value instanceof UserMessage) {
            serializeUserMessage((UserMessage) value, gen);
        } else if (value instanceof AiMessage) {
            serializeAiMessage((AiMessage) value, gen);
        }

        gen.writeEndObject();
    }

    private void serializeUserMessage(UserMessage message, JsonGenerator gen) throws IOException {
        if (message.name() != null) {
            gen.writeStringField("name", message.name());
        }

        List<Content> contents = message.contents();
        if (contents.size() == 1 && contents.get(0) instanceof TextContent) {
            // 如果是单一文本内容，直接序列化为text字段
            gen.writeStringField("text", ((TextContent) contents.get(0)).text());
        } else {
            // 否则序列化整个contents数组
            gen.writeArrayFieldStart("contents");
            for (Content content : contents) {
                gen.writeObject(content);
            }
            gen.writeEndArray();
        }
    }

    private void serializeAiMessage(AiMessage message, JsonGenerator gen) throws IOException {
        if (message.text() != null) {
            gen.writeStringField("text", message.text());
        }

        if (!message.toolExecutionRequests().isEmpty()) {
            gen.writeArrayFieldStart("toolExecutionRequests");
            for (ToolExecutionRequest request : message.toolExecutionRequests()) {
                gen.writeObject(request);
            }
            gen.writeEndArray();
        }
    }
}