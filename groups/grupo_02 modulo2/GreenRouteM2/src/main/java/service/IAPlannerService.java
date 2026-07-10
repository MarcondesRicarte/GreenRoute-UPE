package service;

public interface IAPlannerService {

    String interpretarCadastroRapido(String entidade, String textoLivre);

    String gerarPlanejamentoRota(String dadosRota);
}
