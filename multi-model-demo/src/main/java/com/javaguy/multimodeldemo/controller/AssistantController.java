package com.javaguy.multimodeldemo.controller;

import com.javaguy.multimodeldemo.service.SmartAssistantService;
import com.javaguy.multimodeldemo.service.SmartAssistantService.CustomModelResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {

    private final SmartAssistantService service;

    public AssistantController(SmartAssistantService service) {
        this.service = service;
    }

    @PostMapping("/ask")
    public CustomModelResponse ask(@RequestParam String question){
        return service.getResponse(question);
    }
}
