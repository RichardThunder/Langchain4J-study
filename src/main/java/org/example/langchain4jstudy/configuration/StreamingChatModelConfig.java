package org.example.langchain4jstudy.configuration;

import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StreamingChatModelConfig {
    @Value("${langchain4j.google-ai.chat-model.model-name}")
    private String chatModelName;
    @Value("${langchain4j.google-ai.api-key}")
    private String apiKey;

    @Bean
    public StreamingChatModel streamingChatModel(){
        return GoogleAiGeminiStreamingChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .build();
    }
}
