package com.example.medassistant.controller;

import com.example.medassistant.dto.ChatRequest;
import com.example.medassistant.service.AssistantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AssistantService assistantService;

    @PostMapping
    public ResponseEntity<String> chat(
            @RequestBody ChatRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        return ResponseEntity.ok(assistantService.chat(request.prompt(), request.model(), userId));
    }

    @PostMapping(value = "/stream", produces = "text/event-stream; charset=UTF-8")
    public Flux<String> chatStream(
            @RequestBody ChatRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        return assistantService.chatStream(request.prompt(), request.model(), userId);
    }

    @PostMapping("/explain")
    public ResponseEntity<String> explainCondition(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(assistantService.explainCondition(request.prompt(), request.model()));
    }

    @PostMapping("/symptoms")
    public ResponseEntity<String> analyzeSymptoms(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(assistantService.analyzeSymptoms(request.prompt(), request.model()));
    }

    @PostMapping("/diagnose")
    public ResponseEntity<String> diagnoseWithReasoning(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(assistantService.diagnoseWithReasoning(request.prompt(), request.model()));
    }

    @PostMapping("/consult")
    public ResponseEntity<String> consult(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(assistantService.consult(request.prompt(), request.model()));
    }
}