package com.greenroute.gemini;

import com.greenroute.model.Cidade;
import com.greenroute.model.Veiculo;

public interface IAPlannerService {

    /**
     * Consulta a IA para planejar a viagem entre o veículo informado
     * e a cidade de destino, retornando uma resposta descritiva
     * (viabilidade da viagem, necessidade de recarga, duração estimada etc.).
     */
    String planejarRota(Veiculo veiculo, Cidade cidade);
}
