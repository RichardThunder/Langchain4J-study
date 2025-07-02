package org.example.langchain4jstudy.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
@Service
public class ServiceWithMemoryExample {
    @Value("${langchain4j.google-ai.chat-model.model-name}")
    private  String chatModelName;
    @Value("${langchain4j.google-ai.api-key}")
    private  String apiKey;
    // private final ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
    private final ChatMemory chatMemory;

    public ServiceWithMemoryExample(PersistentChatMemoryStore chatMemoryStore) {
        this.chatMemory = MessageWindowChatMemory.builder()
                .id("service-memory")
                .maxMessages(10)
                .chatMemoryStore(chatMemoryStore)
                .build();
    }
    @Bean
    public Assistant assistant(){
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .build();

        return AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();
    }

    public String serviceWithMemoryMessage() {
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .responseFormat(ResponseFormat.JSON)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();

        String answer = assistant.chat("Hello! My name is Klaus.");
        System.out.println(answer); // Hello Klaus! How can I assist you today?


        return assistant.chat("What is my name?");

    }
}