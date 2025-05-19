package org.example.backend.model.chatgpt;

import java.util.List;

public record AiRecipe(
        String title,
        List<String> ingredients,
        String description
        ) {
}
