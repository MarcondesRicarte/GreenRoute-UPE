package com.greenroute.gemini;

import com.greenroute.model.Cidade;
import com.greenroute.model.Veiculo;

public class PlanejadorRotaIA implements IAPlannerService {

    private ConexaoGemini conexaoGemini;

    public PlanejadorRotaIA() {
        conexaoGemini = new ConexaoGemini();
    }

    @Override
    public String planejarRota(Veiculo veiculo, Cidade cidade) {

        double autonomiaAtual = veiculo.calcularAutonomia();
        double distancia = cidade.getDistanciaDaCapital();
        double autonomiaRestante = autonomiaAtual - distancia;

        String prompt = """
                Você é um consultor de rotas especializado em veículos elétricos e híbridos.

                Analise a viagem abaixo e escreva uma resposta descritiva, em português,
                explicando se a viagem é viável com a autonomia atual, se será necessário
                recarregar/abastecer no caminho e uma estimativa da duração total da viagem
                (considerando o tempo de recarga, caso necessário).

                Dados do veículo:
                Modelo: %s
                Autonomia atual: %.2f km
                Tempo de recarga completa: %d minutos

                Dados da cidade de destino:
                Nome: %s
                Distância da capital: %.2f km

                Cálculo de referência (autonomia atual - distância):
                %.2f km

                Leve em conta também condições simuladas de clima e trânsito na rota
                (varie de forma plausível, como se fossem dados reais do dia da viagem)
                e comente brevemente o impacto delas na viagem.

                Responda de forma clara e objetiva, em no máximo 2 parágrafos, sem usar
                tópicos ou listas.
                """.formatted(
                veiculo.getModelo(),
                autonomiaAtual,
                veiculo.getTempoRecargaCompleta(),
                cidade.getNome(),
                distancia,
                autonomiaRestante
        );

        return conexaoGemini.perguntar(prompt);
    }
}
