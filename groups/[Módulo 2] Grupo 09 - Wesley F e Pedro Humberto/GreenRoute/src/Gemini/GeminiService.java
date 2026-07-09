package Gemini;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;
import java.time.Duration;

public class GeminiService {

    private final ChatLanguageModel model;

    public GeminiService() {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GEMINI_API_KEY");

        this.model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .timeout(Duration.ofSeconds(20))
                .build();
    }

    public String perguntar(String pergunta) {
        ChatRequest request = ChatRequest.builder()
                .messages(UserMessage.from(pergunta))
                .build();

        try {
            ChatResponse response = model.chat(request);
            return response.aiMessage().text();

        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("503")) {
                return "Serviço sobrecarregado. O sistema tentará se conectar novamente.";
            } else {
                return "Erro inesperado: " + e.getMessage();
            }
        }
    }
}