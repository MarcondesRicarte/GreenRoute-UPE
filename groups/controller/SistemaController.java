package controller;

import java.util.ArrayList;
import model.Cidade;
import model.Eletroposto;
import model.Veiculo;
import Repository.GreenRouteRepository; 
public class SistemaController {

    private GreenRouteRepository repo = new GreenRouteRepository();

    private VeiculoController veiculoController = new VeiculoController();
    private CidadeController cidadeController = new CidadeController();
    private EletropostoController eletropostoController = new EletropostoController();

    public GreenRouteRepository getRepo() {
        return this.repo;
    }

    public void simularViagem(int veiculoId, int cidadeId) {
        Veiculo v = veiculoController.buscarVeiculo(veiculoId);
        Cidade c = cidadeController.buscarCidade(cidadeId);

        if (v == null || c == null) {
            System.out.println("Erro: Veículo ou Cidade não encontrados!");
            return;
        }
    }
}