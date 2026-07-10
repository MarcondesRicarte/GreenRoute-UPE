package controller;

public interface IAPlannerService {
    // Método para o Planejador de Rotas Inteligente (Ação 2)
    String planejarRotaInteligente(String dadosVeiculo, String dadosCidade);
    
    // Método para o Cadastro Automático via LLM (Ação 1)
    String extrairDadosTextoLivre(String textoLivre);
}
