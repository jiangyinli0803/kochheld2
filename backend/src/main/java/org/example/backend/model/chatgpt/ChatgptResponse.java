package org.example.backend.model.chatgpt;

import java.util.List;

public record ChatgptResponse(List<ChatgptChoice> choices) {
    public String text() {
        return choices.get(0).message().content();
    }
}