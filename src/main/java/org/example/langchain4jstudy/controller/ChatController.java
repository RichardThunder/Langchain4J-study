package org.example.langchain4jstudy.controller;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;


import java.util.concurrent.CompletableFuture;

import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import org.example.langchain4jstudy.service.*;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ChatController {
    @Autowired
    ServiceWithMemoryExample serviceWithMemoryExample;
    @Autowired
    Assistant assistant;
    @Autowired
    ServiceWithMemoryForEachUserExample serviceWithMemoryForEachUserExample;
    @Autowired
    ServiceWithPersistentMemoryExample serviceWithPersistentMemoryExample;
    @Autowired
    private final ChatModel chatModel;
    private final StreamingChatModel streamingChatModel;
    private final WeatherForecastService weatherForecastService;

    public ChatController(ChatModel chatModel, StreamingChatModel streamingChatModel, 
                         WeatherForecastService weatherForecastService) {
        this.chatModel = chatModel;
        this.streamingChatModel = streamingChatModel;
        this.weatherForecastService = weatherForecastService;
    }

    @GetMapping("/chat")
    public String model(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        return chatModel.chat(message);
    }

    @GetMapping("/streaming-chat")
    public String streamingChat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        StringBuilder responseBuilder = new StringBuilder();
        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        streamingChatModel.chat(message, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                responseBuilder.append(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                futureResponse.complete(completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
            }
        });

        futureResponse.join();
        return responseBuilder.toString();
    }

    /*@GetMapping("/weather")
    public String weatherForecast(@RequestParam(value="location", defaultValue = "Paris") String location) {
        WeatherForecastService weatherForecastService =
                new WeatherForecastService();

        ChatModel gemini = GoogleAiGeminiChatModel.builder()
                .apiKey(System.getenv("GEMINI_AI_KEY"))
                .modelName("gemini-1.5-flash")
                .temperature(0.0)
                .build();

        WeatherAssistant weatherAssistant =
                AiServices.builder(WeatherAssistant.class)
                        .chatModel(gemini)
                        .tools(weatherForecastService)
                        .build();

        String tokyoWeather = weatherAssistant.chat(
                "What is the weather forecast for Tokyo?");

        log.info("Gemini> {}", tokyoWeather);
        return tokyoWeather;
    }
*/
    @GetMapping("/chatMemory")
    public String chatMemory(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        // This method is a placeholder for future implementation
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id("12345")
                .maxMessages(10)
                .chatMemoryStore(new PersistentChatMemoryStore())
                .build();
        return "Chat memory functionality is not yet implemented.";
    }
    @GetMapping("/ServiceWithMemoryExample")
    public String ServiceWithMemoryExample(@RequestParam(value="message")String message){
        //serviceWithMemoryExample.serviceWithMemoryMessage();
        return assistant.chat(message);
    }

    @GetMapping("/ServiceWithMemoryForEachUser")
    public String ServiceWithMemoryForEachUser() {
        // This method is a placeholder for future implementation
        serviceWithMemoryForEachUserExample.memoryForEachUser();
       return "done";
    }

    /**
     * Endpoint for chat with persistent memory.
     * This endpoint allows for chat interactions with memory persistence between sessions.
     * 
     * @param message The user's message
     * @return The assistant's response
     */
    @GetMapping("/persistent-chat")
    public String persistentChat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        log.info("Received message for persistent chat: {}", message);
        return serviceWithPersistentMemoryExample.chat(message);
    }

    /**
     * Endpoint for demonstrating memory persistence.
     * This endpoint demonstrates the memory persistence by sending two messages and checking
     * if the assistant remembers information from the first message.
     * 
     * @return The assistant's response to the second message
     */
    @GetMapping("/demonstrate-persistent-memory")
    public String demonstratePersistentMemory() {
        log.info("Demonstrating persistent memory");
        return serviceWithPersistentMemoryExample.demonstrateMemory();
    }
}
