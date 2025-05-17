package org.example.backend.model.chatgpt;

public record ChatGPTRequestMessage(String role,
                                    String content) {
}
