package gemini;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;

public class IAPlannerService {
    private ChatLanguageModel model;

    public IAPlannerService() {
        try {
            Dotenv dotenv = Dotenv.load();
            String apiKey = dotenv.get("GEMINI_API_KEY");

            this.model = GoogleAiGeminiChatModel.builder()
                    .apiKey(apiKey)
                    .modelName("gemini-2.5-flash")
                    .build();
        } catch (Exception e) {
            System.out.println("Erro ao inicializar o Gemini: " + e.getMessage());
        }
    }

    public String processarCadastroRapido(String textoLivre) {
        if (model == null) return "Erro: Conexão com o Gemini não inicializada.";

        String prompt = "Você é um assistente do sistema GreenRoute. O usuário digitou o seguinte texto livre para cadastrar um veículo: \"" 
                + textoLivre + "\". Extraia as informações estruturadas estritamente no formato abaixo, sem saudações ou textos adicionais, se não encontrar o valor deixe em branco:\n"
                + "Modelo:\nAutonomiaMax:\nCargaAtual:\nConsumokWh:\nTempoRecarga:\nTipoConector:\n";

        ChatRequest request = ChatRequest.builder()
                .messages(UserMessage.from(prompt))
                .build();

        ChatResponse response = model.chat(request);
        return response.aiMessage().text();
    }

    public String planejarRotaInteligente(String dadosVeiculo, String dadosCidade) {
        if (model == null) return "Erro: Conexão com o Gemini não inicializada.";

        String prompt = "Você é o Planejador de Rotas Inteligente do GreenRoute. Analise estes dados e gere um relatório descritivo "
                + "e amigável sobre a viabilidade da viagem, tempo estimado total levando em conta paradas para recarga se necessário, "
                + "e simule um impacto realista fictício de clima (ex: chuva forte drenando bateria) ou trânsito nas rodovias.\n"
                + "Dados do Veículo selecionado: " + dadosVeiculo + "\n"
                + "Dados da Cidade de destino: " + dadosCidade;

        ChatRequest request = ChatRequest.builder()
                .messages(UserMessage.from(prompt))
                .build();

        ChatResponse response = model.chat(request);
        return response.aiMessage().text();
    }
}
