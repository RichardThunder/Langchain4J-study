package org.example.langchain4jstudy.configuration;

import dev.langchain4j.model.chat.ChatModel;

import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ChatModelConfig {
    @Value("${langchain4j.google-ai.chat-model.model-name}")
    private String chatModelName;
    @Value("${langchain4j.google-ai.api-key}")
    private String apiKey;

    @Bean
    ChatModel chatModel(){
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .temperature(1.0)
                .topP(0.95)
                .topK(64)
                .seed(42).maxOutputTokens(8192)
                .timeout(Duration.ofSeconds(60))
                .responseFormat(ResponseFormat.JSON) // or .responseFormat(ResponseFormat.builder()...build())
                .build();
    }

}
