package com.javaguy.multimodeldemo.service;

import com.javaguy.multimodeldemo.model.ModelRole;
import com.javaguy.multimodeldemo.router.ModelRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
public class SmartAssistantService {

    private static final Logger log = LoggerFactory.getLogger(SmartAssistantService.class);

    private final ModelRouter modelRouter;
    private final Map<ModelRole, ChatClient> clientsByRole;

    public SmartAssistantService(ModelRouter modelRouter, Map<ModelRole, ChatClient> clientsByRole) {
        this.modelRouter = modelRouter;
        this.clientsByRole = clientsByRole;
    }

    public CustomModelResponse getResponse(String question){
        ModelRole role = modelRouter.classify(question);
        log.info("routing question  to role {}  | question = {}", role, question);

        ChatClient client = clientsByRole.get(role);
        Instant start = Instant.now();

        String response = client.prompt()
                .user(question)
                .call()
                .content();

        Duration duration = Duration.between(start, Instant.now());

        return new CustomModelResponse(role, response, duration.toMillis());
    }
    public record CustomModelResponse(ModelRole role, String response, long latencyMs){}
}
