package org.example.backend.model.chatgpt;

import java.util.Collections;
import java.util.List;

public record ChatgptRequest(
        String model,
        List<ChatgptRequestMessage> messages
) {
    public ChatgptRequest(String message) {
        this("gpt-4o-mini", Collections.singletonList(new ChatgptRequestMessage("user", message)));
}}
