package org.example.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.model.chatgpt.AiRecipe;
import org.example.backend.model.chatgpt.ChatgptRequest;
import org.example.backend.model.chatgpt.ChatgptResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;


@RestController
@RequestMapping("api/recipe")
public class AiRecipeController {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public AiRecipeController(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;   }


    @GetMapping("/search")
    public AiRecipe searchByIngredient(@RequestParam String ingredient) throws JsonProcessingException {

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
