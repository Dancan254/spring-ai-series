package com.javaguy.gettingstarted;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping
    public String chat(@RequestParam(defaultValue = "Tell me a joke") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/system")
    public String chatWithSystem(@RequestParam(defaultValue = "Tell me a joke") String message) {
        return chatClient.prompt()
                .system("You are a helpful assistant that responds concisely and in plain text only.")
                .user(message)
                .call()
                .content();
    }

    @GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<String> stream(@RequestParam(defaultValue = "Tell me a joke") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    @GetMapping("/entity")
    public Answer entity(@RequestParam(defaultValue = "What is the capital of France?") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .entity(Answer.class);
    }
}
