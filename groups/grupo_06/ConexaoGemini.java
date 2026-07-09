package gemini;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;

public class ConexaoGemini {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GEMINI_API_KEY");

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .build();

        System.out.println("Enviando pergunta ao Gemini...");

        ChatRequest request = ChatRequest.builder()
                .messages(UserMessage.from("Me fale de forma breve qual a cor do ceu"))
                .build();

        ChatResponse response = model.chat(request);

        String respostaTexto = response.aiMessage().text();

        System.out.println("\n--- Resposta do Gemini ---");
        System.out.println(respostaTexto);
    }

    public static String chamarIA(String prompt) {
        
        try {
            // Pega a chave que o seu MenuPrincipal configurou no sistema
            String apiKey = System.getProperty("GEMINI_API_KEY");
            
            // Caso o MenuPrincipal não tenha achado, tenta ler do .env como plano B
            if (apiKey == null || apiKey.isEmpty()) {
                io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.load();
                apiKey = dotenv.get("GEMINI_API_KEY");
            }

            // Configura o modelo mantendo os mesmos parâmetros padrão do arquivo
            ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                    .apiKey(apiKey)
                    .modelName("gemini-2.5-flash")
                    .build();

            ChatRequest request = ChatRequest.builder()
                    .messages(UserMessage.from(prompt))
                    .build();

            ChatResponse response = model.chat(request);

            // Retorna o texto puro para a TelaEletropostos processar
            return response.aiMessage().text(); 
        
        
    } catch (Exception e) {
        e.printStackTrace();
        // Retorna uma mensagem amigável que a tela pode capturar e exibir em um JOptionPane
        return "Erro: Não foi possível obter resposta da IA (Limite de requisições excedido ou sem conexão).";
    }

    }

    public static String enviarPrompt(String prompt) {
        // TODO Auto-generated method stub
        return chamarIA(prompt);
    }
}