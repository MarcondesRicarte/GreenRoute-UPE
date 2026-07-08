package com.greenroute.gemini;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;

public class ConexaoGemini {

    private final ChatLanguageModel model;

    public ConexaoGemini() {

        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GEMINI_API_KEY");

        model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .maxRetries(5)
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

            String mensagem = e.getMessage();

            if (mensagem != null &&
                    (mensagem.contains("503") || mensagem.contains("UNAVAILABLE"))) {

                throw new IllegalStateException(
                        "O serviço de IA está sobrecarregado no momento. " +
                                "Tente novamente em alguns instantes."
                );
            }

            if (mensagem != null &&
                    (mensagem.contains("429") || mensagem.contains("RESOURCE_EXHAUSTED"))) {

                throw new IllegalStateException(
                        "O limite de uso gratuito da IA foi atingido. " +
                                "Tente novamente mais tarde ou use outra chave de API."
                );
            }

            throw new IllegalStateException(
                    "Erro ao se comunicar com a IA: " + mensagem
            );
        }
    }
}