package com.javaguy.multimodeldemo.config;

import com.javaguy.multimodeldemo.model.ModelRole;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MultiModelConfig {
    @Value("${app.groq.api-key}")
    private String groqApiKey;

    @Value("${app.openai.api-key}")
    private String openaiApiKey;

    @Bean
    public Map<ModelRole, ChatClient> chatClientsByRole(
            OpenAiChatModel baseChatModel,
            OpenAiApi baseOpenAiApi){
        //derive groq
        OpenAiApi groqApi = baseOpenAiApi.mutate()
                .baseUrl("https://api.groq.com/openai")
                .apiKey(groqApiKey)
                .build();
        //openai chat models
        OpenAiChatModel fastModel = baseChatModel.mutate()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("llama-3.1-8b-instant")
                        .temperature(0.5)
                        .maxTokens(256)
                        .build())
                .build();
        OpenAiChatModel codeModel = baseChatModel.mutate()
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.2)
                        .model("gpt-4o")
                        .maxTokens(1024)
                        .build())
                .build();

        OpenAiChatModel reasoningModel = baseChatModel.mutate()
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.7)
                        .model("gpt-4o")
                        .maxTokens(2048)
                        .build())
                .build();

        ChatClient fastClient = ChatClient.builder(fastModel)
                .defaultSystem("""
                        You are a fast, concise assistant.
                        Answer factual questions in ONE short sentence.
                        No preamble. No disclaimers. Just the answer.
                        """)
                .build();

        ChatClient codeClient = ChatClient.builder(codeModel)
                .defaultSystem("""
                        You are an expert software engineer.
                        When asked for code, produce clean, correct, idiomatic code
                        with a brief explanation. Prefer Java and Spring Boot
                        conventions unless another language is specified.
                        Include imports and show minimal working examples.
                        """)
                .build();

        ChatClient reasoningClient = ChatClient.builder(reasoningModel)
                .defaultSystem("""
                        You are a thoughtful analyst. For complex questions,
                      reason step by step before giving your final answer.
                      Acknowledge trade-offs and uncertainty where they exist.
                      Structure your response clearly.
                      """)
                .build();

        return Map.of(
                ModelRole.FAST, fastClient,
                ModelRole.CODE, codeClient,
                ModelRole.REASONING, reasoningClient
        );
    }
}
