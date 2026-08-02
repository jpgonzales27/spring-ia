package com.example.medassistant.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping
    public String chat(@RequestBody String prompt){
        return chatClient
                .prompt(prompt).call().content();
    }

    @PostMapping(value = "/stream", produces = "text/event-stream; charset=UTF-8")
    public Flux<String> chatStream(@RequestBody String prompt){
        return chatClient
                .prompt(prompt).stream().content();
    }

}
