package com.cyberlanting.qwen_rag.repository;

import com.cyberlanting.qwen_rag.pojo.dto.ChatMessageDTO;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
public class RedisChatMemoryStore implements ChatMemoryStore {

    // 注入RedisTemplate
    @Autowired
    private StringRedisTemplate redisTemplate;

    // 统一管理 Key 前缀
    private static final String MEMORY_KEY_PREFIX = "OTTAR:MEMORY:";

    private String getMemoryKey(Object memoryId) {
        return MEMORY_KEY_PREFIX + memoryId.toString();
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // 获取会话信息
        String json = redisTemplate.opsForValue().get(getMemoryKey(memoryId)); // 使用统一的 Key
        // 如果 Redis 中没有数据，json 会是 null，Deserializer 会返回空列表，这是正常的首次聊天情况
        List<ChatMessage> list = ChatMessageDeserializer.messagesFromJson(json);
        // 为了调试，可以打印出获取到的消息
        System.out.println("GET Messages for " + memoryId + ": " + (list != null ? list.size() : 0) + " messages");
        return list;
    }


    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> list) {
        // 更新会话消息
        // 1. 把list转换成json数据
        String json = ChatMessageSerializer.messagesToJson(list);
        // 为了调试，可以打印出要存储的 JSON
        // System.out.println("UPDATE Messages for " + memoryId + ". JSON to store: " + json);
        // 2. 把json数据存储到redis中
        redisTemplate.opsForValue().set(getMemoryKey(memoryId), json, Duration.ofDays(3)); // 使用统一的 Key
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete(getMemoryKey(memoryId)); // 使用统一的 Key
    }
}