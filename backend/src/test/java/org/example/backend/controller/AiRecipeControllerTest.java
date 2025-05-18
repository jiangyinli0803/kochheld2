package org.example.backend.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.model.chatgpt.AiRecipe;
import org.example.backend.model.chatgpt.ChatgptRequest;
import org.example.backend.model.chatgpt.ChatgptResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties ="OpenAi_API_Key=123")
class AiRecipeControllerTest {

    @Test
    void testSearchByIngredient() throws Exception {
        // Arrange
        String mockApiKey = "test-api-key";
        String ingredient    = "test";
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        String jsonFromGpt = """
            {
              "title": "Test Rezept",
              "ingredients": ["1 Test Zutat"],
              "description": "Test Beschreibung"
            }
            """;

        // 反序列化 stub
        AiRecipe expectedRecipe = new AiRecipe(
                "Test Rezept",
                List.of("1 Test Zutat"),
                "Test Beschreibung"
        );
        when(objectMapper.readValue(jsonFromGpt, AiRecipe.class)).thenReturn(expectedRecipe);
        // Mock RestClient and its response
        RestClient mockRestClient = mock(RestClient.class);

        RestClient.RequestBodyUriSpec uriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.ResponseSpec respSpec = mock(RestClient.ResponseSpec.class);
        RestClient.RequestBodySpec bodySpec  = mock(RestClient.RequestBodySpec.class);

        when(mockRestClient.post()).thenReturn(uriSpec);
        when(uriSpec.body(any(ChatgptRequest.class))).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(respSpec);

        ChatgptResponse gptResp = mock(ChatgptResponse.class);
        when(gptResp.text()).thenReturn(jsonFromGpt);          // 根据实际 getter 改名
        when(respSpec.body(ChatgptResponse.class)).thenReturn(gptResp);


        // 4. 创建控制器，覆写 createRestClient 以注入 mock
        AiRecipeController controller = new AiRecipeController(mockRestClient, objectMapper);

        /* ---------- Act ---------- */
        AiRecipe result = controller.searchByIngredient(ingredient);

        /* ---------- Assert ---------- */
        assertNotNull(result);
        assertEquals("Test Rezept", result.title());
        assertIterableEquals(List.of("1 Test Zutat"), result.ingredients());
        assertEquals("Test Beschreibung", result.description());

        /* ---------- Verify RestClient 交互 ---------- */
        verify(mockRestClient).post();
        verify(uriSpec).body(any(ChatgptRequest.class));
        verify(bodySpec).retrieve();
        verify(respSpec).body(ChatgptResponse.class);

        /* ---------- Verify ObjectMapper ---------- */
        verify(objectMapper).readValue(jsonFromGpt, AiRecipe.class);

        verifyNoMoreInteractions(mockRestClient, uriSpec, bodySpec, respSpec, objectMapper);
    }
}