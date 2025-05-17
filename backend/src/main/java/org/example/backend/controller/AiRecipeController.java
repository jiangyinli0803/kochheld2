package org.example.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.model.chatgpt.AiRecipe;
import org.example.backend.model.chatgpt.ChatgptRequest;
import org.example.backend.model.chatgpt.ChatgptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("api/recipe")
public class AiRecipeController {
    private final RestClient restClient;
    @Autowired
    private ObjectMapper objectMapper;

    public AiRecipeController(@Value("${OpenAi_API_Key}") String openAiApiKey) {

        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .build();
    }

    @GetMapping("/search")
    AiRecipe SearchByIngredient(@RequestParam String ingredient) throws JsonProcessingException{

        String request = "Create a simple recipe based on the ingredient: " + ingredient + ". " +
                "Respond ONLY with a valid JSON object like this format:  " +
                "{\\\"title\\\": \\\"...\\\", \\\"ingredients\\\": [\\\"...\\\"], \\\"description\\\": \\\"...\\\" }. " +
                "The parameter names should in English, but the contents should written in German. Do not include any explanation or formatting, only raw JSON.";

        ChatgptResponse response = restClient.post()
                .body(new ChatgptRequest(request))
                .retrieve()
                .body(ChatgptResponse.class);

        if (response == null || response.text() == null) {
            throw new IllegalStateException("Empty GPT response");
        }
        return objectMapper.readValue(response.text(), AiRecipe.class);
    }
}
