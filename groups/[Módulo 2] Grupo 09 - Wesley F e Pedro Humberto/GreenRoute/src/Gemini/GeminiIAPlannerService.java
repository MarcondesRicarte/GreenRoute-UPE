package Gemini;

public class GeminiIAPlannerService implements IAPlannerService {

    private final GeminiService geminiService;

    public GeminiIAPlannerService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @Override
    public String planejarRota(String contextoViagem) {
        String prompt = """
                Você é um assistente de planejamento de viagens para veículos elétricos e híbridos.
                Com base nos dados abaixo, escreva uma análise breve e prática (máximo 5 frases)
                sobre a viabilidade da viagem, riscos envolvidos e recomendações objetivas.
                Responda em texto corrido, sem markdown, sem listas.

                Dados da viagem:
                %s
                """.formatted(contextoViagem);

        return geminiService.perguntar(prompt);
    }
}