package controller;

import model.Cidade;
import model.Eletroposto;
import model.Veiculo;
import model.VeiculoEletrico;
import exceptions.AutonomiaInsuficienteException;
import exceptions.ConectorIncompativelException;
import Gemini.IAPlannerService;

import java.util.ArrayList;
import java.util.Random;

public class RotaController {
    private VeiculoController veiculoController;
    private CidadeController cidadeController;
    private EletropostoController eletropostoController;
    private IAPlannerService iaPlannerService;

    public RotaController(VeiculoController veiculoController, CidadeController cidadeController,
                          EletropostoController eletropostoController, IAPlannerService iaPlannerService) {
        this.veiculoController = veiculoController;
        this.cidadeController = cidadeController;
        this.eletropostoController = eletropostoController;
        this.iaPlannerService = iaPlannerService;
    }

    // ==================================================================
    // Regra de negócio: Simular Rota (algorítmico, já existia, agora com exceções)
    // ==================================================================
    public String simularRota(int veiculoId, int cidadeDestinoId) {
        Veiculo veiculo = veiculoController.buscarVeiculoPorId(veiculoId);
        Cidade destino = cidadeController.buscarCidadePorIdController(cidadeDestinoId);

        double autonomiaAtual = veiculo.getAutonomiaMaxima() * (veiculo.getCargaBateriaAtual() / 100.0);
        double distanciaDestino = destino.getDistanciaDaCapital();

        StringBuilder resultado = new StringBuilder();
        resultado.append("\n======== Resultado da Simulação de Rota ========\n");
        resultado.append("Veículo: ").append(veiculo.getModelo()).append("\n");
        resultado.append("Bateria atual: ").append(veiculo.getCargaBateriaAtual()).append("%\n");
        resultado.append("Autonomia atual: ").append(String.format("%.1f", autonomiaAtual)).append(" km\n");
        resultado.append("Destino: ").append(destino.getNome()).append(" - ").append(destino.getEstado()).append("\n");
        resultado.append("Distância: ").append(distanciaDestino).append(" km\n");
        resultado.append("================================================\n");

        if (autonomiaAtual >= distanciaDestino) {
            double kmSobrando = autonomiaAtual - distanciaDestino;
            resultado.append("Autonomia suficiente para chegar ao destino!\n");
            resultado.append("Sobram aproximadamente ").append(String.format("%.1f", kmSobrando)).append(" km de autonomia.\n");
            return resultado.toString();
        }

        // Autonomia insuficiente: usando exceções específicas
        double kmFaltando = distanciaDestino - autonomiaAtual;
        resultado.append("Autonomia insuficiente. Faltam ").append(String.format("%.1f", kmFaltando)).append(" km.\n");

        ArrayList<Cidade> todasCidades = cidadeController.listarCidadesController();
        ArrayList<Eletroposto> eletropostosNoCaminho = new ArrayList<>();

        for (Cidade c : todasCidades) {
            if (c.getId() == cidadeDestinoId) continue;

            boolean estaNoCaminhoAteDestino = c.getDistanciaDaCapital() < distanciaDestino;
            boolean estaDentroDoAlcanceAtual = c.getDistanciaDaCapital() <= autonomiaAtual;

            if (estaNoCaminhoAteDestino && estaDentroDoAlcanceAtual) {
                eletropostosNoCaminho.addAll(eletropostoController.buscarEletropostosPorCidadeController(c.getId()));
            }
        }

        if (eletropostosNoCaminho.isEmpty()) {
            throw new AutonomiaInsuficienteException(
                    "Autonomia insuficiente para chegar a " + destino.getNome() +
                            " e não há eletropostos cadastrados dentro do alcance atual (" + String.format("%.1f", autonomiaAtual) + " km) em nehuma cidade no caminho.");
        }

        ArrayList<Eletroposto> eletropostosParaExibir;

        // Verificação de compatibilidade de conector. só faz sentido para veículos elétricos
        // por isso achei melhor não importar veículo híbrido também.
        if (veiculo instanceof VeiculoEletrico veiculoEletrico) {
            String conectorVeiculo = veiculoEletrico.getTipoConector();

            ArrayList<Eletroposto> compativeis = new ArrayList<>();
            for (Eletroposto e : eletropostosNoCaminho) {
                if (e.getTiposConectoresDisponiveis().toLowerCase().contains(conectorVeiculo.toLowerCase())){
                    compativeis.add(e);
                }
            }
            if (compativeis.isEmpty()) {
                throw new ConectorIncompativelException(
                        "Há eletropostos em " + destino.getNome() + ", mas nenhum compatível com o conector \""
                                + conectorVeiculo + "\" deste veículo.");
            }
            eletropostosParaExibir = compativeis;
        } else {
            eletropostosParaExibir = eletropostosNoCaminho;
        }

        resultado.append("\nEletropostos dentro do seu alcance atual (")
                .append(String.format("%.1f", autonomiaAtual))
                .append(" km), para reabastecer antes de continuar até ")
                .append(destino.getNome()).append(":\n");


        for (Eletroposto e : eletropostosNoCaminho) {
            Cidade cidadeDoEletroposto = cidadeController.buscarCidadePorIdController(e.getCidadeId());
            resultado.append("\n------------------------------------------\n");
            resultado.append("Cidade: ").append(cidadeDoEletroposto.getNome())
                            .append(" (").append(cidadeDoEletroposto.getDistanciaDaCapital()).append(" km da capital)\n");
            resultado.append("Nome: ").append(e.getNome()).append("\n");
            resultado.append("Localização: ").append(e.getLocalizacao()).append("\n");
            resultado.append("Conectores: ").append(e.getTiposConectoresDisponiveis()).append("\n");
            resultado.append("Potência: ").append(e.getPotenciaCargaKw()).append(" kW\n");
            resultado.append("Preço: R$ ").append(e.getPrecoPorKwh()).append("/kWh\n");
            resultado.append("Vagas: ").append(e.getVagasDisponiveis()).append("\n");
        }
        resultado.append("------------------------------------------\n");

        return resultado.toString();
    }

    // ==================================================================
    // Ação 2: Planejador de Rotas Inteligente (via LLM)
    // ==================================================================
    public String planejarRotaComIA(int veiculoId, int cidadeDestinoId) {
        Veiculo veiculo = veiculoController.buscarVeiculoPorId(veiculoId);
        Cidade destino = cidadeController.buscarCidadePorIdController(cidadeDestinoId);

        double autonomiaAtual = veiculo.getAutonomiaMaxima() * (veiculo.getCargaBateriaAtual() / 100.0);
        double distancia = destino.getDistanciaDaCapital();

        // Dados "reais" simulados, conforme pedido no enunciado (clima, trânsito)
        String[] climasPossiveis = {"Ensolarado", "Nublado", "Chuvoso"};
        String[] transitoPossiveis = {"Leve", "Moderado", "Intenso"};
        Random random = new Random();
        String climaSimulado = climasPossiveis[random.nextInt(climasPossiveis.length)];
        String transitoSimulado = transitoPossiveis[random.nextInt(transitoPossiveis.length)];

        String contexto = String.format(
                "Veículo: %s%n" +
                        "Autonomia restante: %.1f km%n" +
                        "Tempo de recarga completa: %d minutos%n" +
                        "Cidade de destino: %s%n" +
                        "Distância até o destino: %.1f km%n" +
                        "Condição climática simulada: %s%n" +
                        "Condição de trânsito simulada: %s",
                veiculo.getModelo(), autonomiaAtual, veiculo.getTempoRecargaCompleta(),
                destino.getNome(), distancia, climaSimulado, transitoSimulado
        );

        return iaPlannerService.planejarRota(contexto);
    }
}