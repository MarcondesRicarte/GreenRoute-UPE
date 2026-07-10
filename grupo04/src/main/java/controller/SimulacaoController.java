package controller;

import exception.AutonomiaInsuficienteException;
import java.util.ArrayList;
import model.Cidade;
import model.Eletroposto;
import model.Veiculo;
import repository.CidadeRepository;
import repository.EletropostoRepository;
import repository.VeiculoRepository;

public class SimulacaoController {

    private VeiculoRepository veiculoRepo;
    private CidadeRepository cidadeRepo;
    private EletropostoRepository eletropostoRepo;

    // IA
    private IAPlannerService iaPlanner = new IAPlannerServiceImpl();

    public SimulacaoController(VeiculoRepository veiculoRepo,
                               CidadeRepository cidadeRepo,
                               EletropostoRepository eletropostoRepo) {

        this.veiculoRepo = veiculoRepo;
        this.cidadeRepo = cidadeRepo;
        this.eletropostoRepo = eletropostoRepo;
    }

    public String rodarSimulacao(int veiculoId, int cidadeId)
            throws AutonomiaInsuficienteException {

        Veiculo veiculo = veiculoRepo.buscarPorId(veiculoId);
        Cidade cidade = cidadeRepo.buscarPorId(cidadeId);

        if (veiculo == null || cidade == null) {
            throw new IllegalArgumentException("Veículo ou Cidade não cadastrados!");
        }

        double autonomia = veiculo.autonomiaAtual();
        double distancia = cidade.getDistanciaDaCapital();

        if (autonomia < distancia) {
            throw new AutonomiaInsuficienteException(
                    "Autonomia insuficiente! O veículo possui "
                    + String.format("%.2f", autonomia)
                    + " km, mas a distância é "
                    + distancia + " km.");
        }

        // Aqui chama o Gemini
        String dadosVeiculo =
                "Modelo: " + veiculo.getModelo()
                + ", autonomia: " + autonomia + " km";

        String dadosCidade =
                cidade.getNome()
                + ", distância: " + distancia + " km";

        return iaPlanner.planejarRotaInteligente(
                dadosVeiculo,
                dadosCidade
        );
    }

    public ArrayList<Eletroposto> obterEletropostosDaCidade(int cidadeId) {

        ArrayList<Eletroposto> filtrados = new ArrayList<>();

        for (Eletroposto e : eletropostoRepo.listar()) {

            if (e.getCidadeId() == cidadeId) {
                filtrados.add(e);
            }
        }

        return filtrados;
    }
}
