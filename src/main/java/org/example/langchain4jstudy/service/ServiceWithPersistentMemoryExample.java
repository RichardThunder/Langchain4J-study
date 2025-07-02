package org.example.langchain4jstudy.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * A Spring service that demonstrates persistent memory functionality using langchain4j.
 * This service allows for chat interactions with memory persistence between sessions.
 */
@Slf4j
@Service
public class ServiceWithPersistentMemoryExample {

    @Value("${langchain4j.google-ai.chat-model.model-name}")
    private String chatModelName;

    @Value("${langchain4j.google-ai.api-key}")
    private String apiKey;

    private final ChatMemory chatMemory;

    public ServiceWithPersistentMemoryExample(PersistentChatMemoryStore chatMemoryStore) {
        this.chatMemory = MessageWindowChatMemory.builder()
                .id("persistent-memory-service")
                .maxMessages(10)
                .chatMemoryStore(chatMemoryStore)
                .build();
    }

    /**
     * Creates an Assistant bean that can be used for chat interactions with persistent memory.
     */
    @Bean
    public Assistant persistentMemoryAssistant() {
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .build();

        return AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();
    }

    /**
     * Sends a message to the assistant and returns the response.
     * The conversation history is persisted between calls.
     * 
     * @param message The user's message
     * @return The assistant's response
     */
    public String chat(String message) {
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .responseFormat(ResponseFormat.JSON)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();

        String response = assistant.chat(message);
        log.info("User: {}", message);
        log.info("Assistant: {}", response);

        return response;
    }

    /**
     * Demonstrates the memory persistence by sending two messages and checking if the assistant
     * remembers information from the first message.
     * 
     * @return The assistant's response to the second message
     */
    public String demonstrateMemory() {
        String firstResponse = chat("Hello! My name is Klaus.");
        log.info("First response: {}", firstResponse);

        String secondResponse = chat("What is my name?");
        log.info("Second response: {}", secondResponse);

        return secondResponse;
    }
}
