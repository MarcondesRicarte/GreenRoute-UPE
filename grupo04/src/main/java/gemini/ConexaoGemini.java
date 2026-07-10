package gemini;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;

public class ConexaoGemini {

    public String perguntarParaGemini(String prompt) {

        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GEMINI_API_KEY");

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .build();

        ChatRequest request = ChatRequest.builder()
                .messages(UserMessage.from(prompt))
                .build();

        ChatResponse response = model.chat(request);

        return response.aiMessage().text();
    }

    public static void main(String[] args) {
        ConexaoGemini conexao = new ConexaoGemini();
        String resposta = conexao.perguntarParaGemini("Olá, você está funcionando?");
        System.out.println(resposta);
    }
}
