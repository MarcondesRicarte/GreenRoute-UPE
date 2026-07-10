package controller;

import gemini.ConexaoGemini;

public class IAPlannerServiceImpl implements IAPlannerService {

    private final ConexaoGemini gemini = new ConexaoGemini();

    @Override
    public String extrairDadosTextoLivre(String textoLivre) {
        // Agora ele chama a IA de verdade!
        return gemini.perguntarParaGemini("Extraia os dados de veículo deste texto: " + textoLivre);
    }

    @Override
    public String planejarRotaInteligente(String dadosVeiculo, String dadosCidade) {
        // Agora ele chama a IA de verdade!
        return gemini.perguntarParaGemini("Planeje a rota para: " + dadosVeiculo + " em " + dadosCidade);
    }
}
