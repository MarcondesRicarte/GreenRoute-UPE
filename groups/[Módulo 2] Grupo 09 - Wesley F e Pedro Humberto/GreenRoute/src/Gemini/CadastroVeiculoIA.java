package Gemini;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.Veiculo;
import model.VeiculoEletrico;
import model.VeiculoHibrido;

public class CadastroVeiculoIA {

    private final GeminiService geminiService;
    private final Gson gson = new Gson();

    public CadastroVeiculoIA(GeminiService geminiService){
        this.geminiService = geminiService;
    }

    public Veiculo interpretarDescricao(String descricaoUsuario){
        String prompt = montarPrompt(descricaoUsuario);
        String respostaBruta = geminiService.perguntar(prompt);
        String json = limparFormatacaoJson(respostaBruta);

        VeiculoIADTO dto;
        try {
            dto = gson.fromJson(json, VeiculoIADTO.class);
        } catch (JsonSyntaxException e){
            throw new IllegalStateException("O Gemini não conseguiu retornar um JSON válido. Resposta de emergência: " + respostaBruta);
        }
        if (dto == null || dto.getTipo() == null) {
            throw new IllegalStateException("Não foi possível identificar os dados do veículo na descrição fornecida.");
        }

        return construirVeiculo(dto);
    }

    private String montarPrompt(String descricaoUsuario){
        return """
                
                Extraia as informações do veículo descrito abaixo e responda APENAS com um JSON,
                sem nenhum texto antes ou depois, sem markdown, no seguinte formato exato:
        
                {
                  "tipo": "eletrico ou hibrido",
                  "modelo": "string",
                  "autonomiaMaxima": number (em km),
                  "cargaBateriaAtual": number (0 a 100),
                  "consumoKwhPorKm": number,
                  "tempoRecargaCompleta": number (em minutos),
                  "tipoConector": "string (apenas se for eletrico, senão null)",
                  "tempoRecargaRapida": number (apenas se for eletrico, senão 0),
                  "capacidadeTanqueCombustivel": number (apenas se for hibrido, senão 0),
                  "consumoCombustivel": number (apenas se for hibrido, senão 0),
                  "tipoCombustivel": "string (apenas se for hibrido, senão null)"
                }
        
                Se algum dado não for mencionado na descrição, estime um valor plausível para
                aquele modelo de veículo em vez de deixar em branco.
        
                Descrição do veículo: "%s"
                """.formatted(descricaoUsuario);
    }

    private String limparFormatacaoJson(String texto){
        // tratamento para caso o gemini envolva o JSON em um ```json...```
        return texto.replace("```json", "").replace("```","").trim();
    }
    private Veiculo construirVeiculo(VeiculoIADTO dto) {
        if("eletrico".equalsIgnoreCase(dto.getTipo())){
            return new VeiculoEletrico(
              0,
                    dto.getModelo(),
                    dto.getAutonomiaMaxima(),
                    dto.getCargaBateriaAtual(),
                    dto.getConsumoKwhPorKm(),
                    dto.getTempoRecargaCompleta(),
                    dto.getTipoConector(),
                    dto.getTempoRecargaRapida()
            );
        } else if ("hibrido".equalsIgnoreCase(dto.getTipo())){
            return new VeiculoHibrido(
                    0,
                    dto.getModelo(),
                    dto.getAutonomiaMaxima(),
                    dto.getCargaBateriaAtual(),
                    dto.getConsumoKwhPorKm(),
                    dto.getTempoRecargaCompleta(),
                    dto.getCapacidadeTanqueCombustivel(),
                    dto.getConsumoCombustivel(),
                    dto.getTipoCombustivel()
            );
        } else {
            throw new IllegalArgumentException("Tipo de veículo não reconhecido: " + dto.getTipo());
        }
    }

}
