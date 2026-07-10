package controller;

import java.util.ArrayList;

import exception.AutonomiaInsuficienteException;
import exception.ConectorIncompativelException;
import model.Cidade;
import model.Eletroposto;
import model.Veiculo;
import model.VeiculoEletrico;
import repository.CidadeRepository;
import repository.EletropostoRepository;
import repository.VeiculoRepository;

public class RotaController {

    private VeiculoRepository veiculoRepository;
    private CidadeRepository cidadeRepository;
    private EletropostoRepository eletropostoRepository;

    public RotaController(VeiculoRepository veiculoRepository,
                          CidadeRepository cidadeRepository,
                          EletropostoRepository eletropostoRepository) {

        this.veiculoRepository = veiculoRepository;
        this.cidadeRepository = cidadeRepository;
        this.eletropostoRepository = eletropostoRepository;
    }

    public String simularViagem(int veiculoId, int cidadeId) {

        Veiculo veiculo =
                veiculoRepository.buscarPorId(veiculoId);

        Cidade cidade =
                cidadeRepository.buscarPorId(cidadeId);

        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não encontrado.");
        }

        if (cidade == null) {
            throw new IllegalArgumentException("Cidade não encontrada.");
        }

        double autonomiaAtual =
                veiculo.calcularAutonomiaAtual();

        double distancia =
                cidade.getDistanciaDaCapital();

        String resultado = "";

        resultado += "\nSIMULAÇÃO DE VIAGEM\n";
        resultado += "--------------------------\n";
        resultado += "Veículo: " + veiculo.getModelo() + "\n";
        resultado += "Destino: " + cidade.getNome() + "\n";
        resultado += "Distância da capital: " + distancia + " km\n";
        resultado += "Autonomia atual: " + autonomiaAtual + " km\n";
        resultado += "--------------------------\n";

        if (autonomiaAtual >= distancia) {

            resultado += "A viagem é possível com a bateria atual.\n";

            return resultado;
        }

        ArrayList<Eletroposto> postos =
                eletropostoRepository.listar();

        if (postos.size() == 0) {

            throw new AutonomiaInsuficienteException(
                    "A autonomia atual do veículo não é suficiente para chegar ao destino.\n\n" +
                            "Não existe nenhum eletroposto cadastrado para auxiliar na recarga."
            );
        }

        if (veiculo instanceof VeiculoEletrico) {

            VeiculoEletrico eletrico =
                    (VeiculoEletrico) veiculo;

            String conectorVeiculo =
                    eletrico.getTipoConector();

            boolean encontrouConectorCompativel =
                    false;

            for (int i = 0; i < postos.size(); i++) {

                Eletroposto posto =
                        postos.get(i);

                String conectoresPosto =
                        posto.getTiposConectoresDisponiveis();

                if (conectoresPosto.toLowerCase().contains(
                        conectorVeiculo.toLowerCase()
                )) {
                    encontrouConectorCompativel = true;
                    break;
                }
            }

            if (!encontrouConectorCompativel) {

                throw new ConectorIncompativelException(
                        "O veículo precisa do conector " + conectorVeiculo + ", " +
                                "mas nenhum eletroposto cadastrado possui esse conector disponível."
                );
            }
        }

        resultado += "A autonomia atual não é suficiente para chegar ao destino.\n";
        resultado += "É necessária uma recarga.\n";
        resultado += "Eletropostos disponíveis:\n";
        resultado += "--------------------------\n";

        for (int i = 0; i < postos.size(); i++) {

            Eletroposto posto =
                    postos.get(i);

            Cidade cidadePosto =
                    cidadeRepository.buscarPorId(
                            posto.getCidadeId()
                    );

            resultado += "Nome: " + posto.getNome() + "\n";

            if (cidadePosto != null) {
                resultado += "Cidade: " + cidadePosto.getNome() + "\n";
            } else {
                resultado += "Cidade: não encontrada\n";
            }

            resultado += "Localização: " + posto.getLocalizacao() + "\n";
            resultado += "Conectores: " + posto.getTiposConectoresDisponiveis() + "\n";
            resultado += "Potência: " + posto.getPotenciaCargaKw() + " kW\n";
            resultado += "Preço por kWh: R$ " + posto.getPrecoPorKwh() + "\n";
            resultado += "Vagas disponíveis: " + posto.getVagasDisponiveis() + "\n";
            resultado += "--------------------------\n";
        }

        throw new AutonomiaInsuficienteException(resultado);
    }
}
