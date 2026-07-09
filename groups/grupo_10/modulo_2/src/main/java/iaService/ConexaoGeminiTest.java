package iaService;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;

public class ConexaoGeminiTest {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GEMINI_API_KEY");

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .build();

        System.out.println("Enviando pergunta ao Gemini...");

        ChatRequest request = ChatRequest.builder()
                .messages(UserMessage.from("Analyze the following text and extract the city data\n"
                        + "Return ONLY the values separated by \";;\" in the following order: \"name;;state;;distance from the capital in Km\".\n"
                        +"Attention: \n"
                        + "In the State Field, it must be the Federative Unit (UF) of the state designated in accordance with the Brazilian Constitution. If the typed state is not Brazilian, return the phrase \"Not located!\" in the State field.\n"
                        + "If the Distance to the capital reported is less than 0, write \"Invalid\" in the field.\n"
                        + "If the data does not exist in the text, put '0' for numbers or '-' for texts.\n"
                        + "If and only if you do not find any data in the text return absolutely anything.\n"
                        + "Use Text: Cadastra a cidade Feira Nova de Pernambuco com 77 kms de distância da capital"))
                .build();

        ChatResponse response = model.chat(request);

        String respostaTexto = response.aiMessage().text();

        System.out.println("\n--- Resposta do Gemini ---");
        System.out.println(respostaTexto);
    }
}