package iaService;

import br.upe.greenroute.exceptions.AIServiceException;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;

public class ConexaoGemini implements AIPlannerService{
    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("GEMINI_API_KEY");
    ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
            .apiKey(apiKey)
            .modelName("gemini-2.5-flash")
            .build();

    @Override
    public String planSmartRoute(String vehicleData, String cityData, String chargingStationData) {
        String prompt = ("Take on the role of logistics specialist and travel planner. Its goal is to analyze the current state of the vehicle, distance from the destination, available charging stations, weather, and traffic to create a safe and optimized travel route.\n\n"
                        + "strict rules you must follow:\n"
                        + "Compare the vehicle's current autonomy with the distance from the destination, assuming we are leaving the capital.\n"
                        + "If the current autonomy is GREATER OR EQUAL to the destination distance, the trip is completed directly.\n"
                        + "If the current autonomy is LESS than the destination distance:\n"
                        + " - For Electric Vehicles: You MUST plan a stop at one of the provide chharging stations. The chosen station's 'Distance from the capital' MUST BE LESS OR EQUAL to the vehicle autonomy.\n"
                        + " - For Hybrid Vehicles: DO NOT recommend electric chraging stations for them. Just warn that they will need to refuel with gas on the road.\n"
                        + "Calculate the aproximate charging time needed at the station (only for Electric Vehicles).\n"
                        + "Weather and Traffic: Based on the destination region in Brazil, estimate the current likely weather conditions and typical traffic quality.Briefly explain how this might impact the trip (e.g., rain reducing autonomy, traffic increasing travel time.(MAKE IT CLEAR THAT IT IS SPECULATION)\n"
                        + "If it is mathematically impossible to reach the destination for an Electric Vehicle (stations are too far), explicitly state that the trip is unsafe or impossible.\n"
                        + "Use ONLY text (no formatting at all!) to structure the relatorio\n"
                        + "OUTPUT FORMAT:\n"
                        + "Provide a clear, user-friendly itinerary in Brazilian Portuguese using Markdown. Include:\n"
                        + " - Resumo da viagem (Veiculo e destino,se a autonomia é suficiente, paradas par abastecimento se for eletrico, especulação da duração total da viagem com base em distância do destino)\n"
                        + "- Condições da Rota (clima estimado e trânsito na região)\n"
                        + " - Recomendações de Parada (Se houver, com a estimativa de tempo)\n"
                        + " - Alertas (se a viagem for arriscada ou exigir atenção)\n\n"
                        + "DATA FOR ANALYSIS:\n"
                        + vehicleData + "\n"
                        + cityData + "\n"
                        + chargingStationData);
        return chatWork(prompt);
    }

    @Override
    public String[] parseVehicle(String text) {
        String Prompt =
                ("Analyze the following text and extract the vehicle data\n"
                        + "Return ONLY the values separated by \";;\" in the following order:"
                        +"\"vehicle model;;maximum range in Kms with full charge;;current battery charge in percentage from 0.0 to 100.0;;consumption of kWh per Km;;estimated time in minutes for full recharge;;type of vehicle connector;;fast recharge in minutes in high-power chargers;;the capacity in liters of the fuel tank;;fuel consumption in Km/l in the combustion engine;;type of fuel;;vehicle type\".\n"
                        +"Attention: \n"
                        + "The vehicle type can ONLY strictly receive these two values: \"Elétrico\" or \"Híbrido\" (must be written exactly like this!) and must be returned obligatorily, if it is not in the text, look for the car model or analyze the rest of the text and find out which of the two types it belongs to..\n"
                        + "if the vehicle is \"Elétrico\" assume that the following data does not exist: \"the capacity in liters of the fuel tank,fuel consumption in Km/l in the combustion engine and type of fuel\""
                        + "if the vehicle is \"Híbrido\" assume that the following data does not exist: \"type of vehicle connector andfast recharge in minutes in high-power chargers\""
                        + "If maximum range in Kms with full charge, current battery charge in percentage from 0.0 to 100.0, consumption of kWh per Km, estimated time in minutes for full recharge, fast recharge in minutes in high-power chargers, the capacity in liters of the fuel tank or fuel consumption in Km/l in the combustion engine reported is less than 0, write \"Invalid\" in the field.\n"
                        + "If the data does not exist in the text, put '0' for numbers or '-' for texts.\n"
                        + "If and only if you do not find any data in the text return absolutely anything.\n"
                        + "Use Text:" + text);
        return chatWorkForRegistration(Prompt);
    }
    @Override
    public String[] parseCity(String text) {
        String Prompt =
                ("Analyze the following text and extract the city data\n"
                + "Return ONLY the values separated by \";;\" in the following order: \"name;;state;;distance from the capital in Km\".\n"
                +"Attention: \n"
                + "In the State Field, it must be the Federative Unit (UF) of the state designated in accordance with the Brazilian Constitution. If the typed state is not Brazilian, return the phrase \"Not located!\" in the State field.\n"
                + "If the Distance to the capital reported is less than 0, write \"Invalid\" in the field.\n"
                + "If the data does not exist in the text, put '0' for numbers or '-' for texts.\n"
                + "If and only if you do not find any data in the text return absolutely anything.\n"
                + "Use Text:" + text);
        return chatWorkForRegistration(Prompt);
    }
    @Override
    public String[] parseStation(String text) {
        String Prompt =
                ("Analyze the following text and extract the charging station data\n"
                        + "Return ONLY the values separated by \";;\" in the following order: \"Name;;Location;;city id;;connectors types available in the station;;charger power in kW;;price charged per kWh;;available vacancies \".\n"
                        +"Attention: \n"
                        + "city id is the numeric ID of the city where the station is located.\n"
                        + "If more than one type of connector present in the station is reported,return it in the data, separated by \",\" in this way: \"first,second, third...\".\n"
                        + "If the city id, charger power in kW, price charged per kWh or available vacancies reported is less than 0, write \"Invalid\" in the field.\n"
                        + "If the data does not exist in the text, put '0' for numbers or '-' for texts.\n"
                        + "If and only if you do not find any data in the text return absolutely anything.\n"
                        + "Use Text:" + text);
        return chatWorkForRegistration(Prompt);
    }
    private String chatWork(String text) {
        try {
            ChatRequest request = ChatRequest.builder()
                    .messages(UserMessage.from(text))
                    .build();
            ChatResponse chatResponse = model.chat(request);
            return chatResponse.aiMessage().text().trim();
        } catch (Exception e) {
            throw new AIServiceException("Erro interno ao processar a resposta da IA.", e);
        }
    }
    public String[] chatWorkForRegistration(String text) {
        String chatResponse = chatWork(text);
        if (chatResponse == null || chatResponse.isBlank()) {
            throw new AIServiceException("A IA não retornou nenhum dado.");
        }
        String[] extractedData = chatResponse.split(";;");
        if (extractedData.length<2) {
            throw new AIServiceException("A IA não formatou os dados corretamente com o delimitador esperado.");
        }
        return extractedData;
    }
}