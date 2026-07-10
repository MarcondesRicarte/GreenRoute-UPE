package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.CidadeController;
import controller.EletropostoController;
import controller.RotaController;
import controller.VeiculoController;
import exception.AutonomiaInsuficienteException;
import exception.ConectorIncompativelException;
import model.Cidade;
import model.Eletroposto;
import model.Veiculo;
import model.VeiculoEletrico;
import model.VeiculoHibrido;
import service.IAPlannerService;

public class TelaPlanejadorRota extends JFrame {

    private VeiculoController veiculoController;
    private CidadeController cidadeController;
    private EletropostoController eletropostoController;
    private RotaController rotaController;
    private IAPlannerService iaPlannerService;

    private JComboBox<Veiculo> comboVeiculos;
    private JComboBox<Cidade> comboCidades;

    private JTextArea areaResultado;

    public TelaPlanejadorRota(VeiculoController veiculoController,
                              CidadeController cidadeController,
                              EletropostoController eletropostoController,
                              RotaController rotaController,
                              IAPlannerService iaPlannerService) {

        this.veiculoController = veiculoController;
        this.cidadeController = cidadeController;
        this.eletropostoController = eletropostoController;
        this.rotaController = rotaController;
        this.iaPlannerService = iaPlannerService;

        configurarJanela();
        montarTela();
        carregarDados();
    }

    private void configurarJanela() {

        setTitle("Planejador de Rotas Inteligente");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void montarTela() {

        JPanel painelFormulario =
                new JPanel(new GridLayout(3, 2, 10, 10));

        comboVeiculos =
                new JComboBox<>();

        comboCidades =
                new JComboBox<>();

        JButton botaoAtualizarListas =
                new JButton("Atualizar Listas");

        JButton botaoPlanejar =
                new JButton("Planejar Rota com IA");

        painelFormulario.add(new JLabel("Selecione o Veículo:"));
        painelFormulario.add(comboVeiculos);

        painelFormulario.add(new JLabel("Selecione a Cidade de Destino:"));
        painelFormulario.add(comboCidades);

        painelFormulario.add(botaoAtualizarListas);
        painelFormulario.add(botaoPlanejar);

        areaResultado =
                new JTextArea();

        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);

        JScrollPane painelResultado =
                new JScrollPane(areaResultado);

        JButton botaoVoltar =
                new JButton("Voltar");

        JPanel painelInferior =
                new JPanel(new GridLayout(1, 1));

        painelInferior.add(botaoVoltar);

        add(painelFormulario, BorderLayout.NORTH);
        add(painelResultado, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);

        botaoAtualizarListas.addActionListener(
                e -> carregarDados()
        );

        botaoPlanejar.addActionListener(
                e -> planejarRota()
        );

        botaoVoltar.addActionListener(
                e -> dispose()
        );

        TemaRosa.aplicar(getContentPane());
    }

    private void carregarDados() {

        comboVeiculos.removeAllItems();
        comboCidades.removeAllItems();

        ArrayList<Veiculo> veiculos =
                veiculoController.listar();

        ArrayList<Cidade> cidades =
                cidadeController.listar();

        for (int i = 0; i < veiculos.size(); i++) {

            comboVeiculos.addItem(
                    veiculos.get(i)
            );
        }

        for (int i = 0; i < cidades.size(); i++) {

            comboCidades.addItem(
                    cidades.get(i)
            );
        }
    }

    private void planejarRota() {

        Veiculo veiculoSelecionado =
                (Veiculo) comboVeiculos.getSelectedItem();

        Cidade cidadeSelecionada =
                (Cidade) comboCidades.getSelectedItem();

        if (veiculoSelecionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cadastre pelo menos um veículo antes de planejar a rota."
            );

            return;
        }

        if (cidadeSelecionada == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cadastre pelo menos uma cidade antes de planejar a rota."
            );

            return;
        }

        try {

            String resultado =
                    rotaController.simularViagem(
                            veiculoSelecionado.getId(),
                            cidadeSelecionada.getId()
                    );

            areaResultado.setText(
                    gerarRespostaIA(
                            veiculoSelecionado,
                            cidadeSelecionada,
                            resultado
                    )
            );

        } catch (AutonomiaInsuficienteException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Autonomia insuficiente",
                    JOptionPane.WARNING_MESSAGE
            );

            areaResultado.setText(
                    gerarRespostaIA(
                            veiculoSelecionado,
                            cidadeSelecionada,
                            erro.getMessage()
                    )
            );

        } catch (ConectorIncompativelException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Conector incompatível",
                    JOptionPane.WARNING_MESSAGE
            );

            areaResultado.setText(
                    gerarRespostaIA(
                            veiculoSelecionado,
                            cidadeSelecionada,
                            erro.getMessage()
                    )
            );

        } catch (IllegalArgumentException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String gerarRespostaIA(Veiculo veiculo,
                                   Cidade cidade,
                                   String resultadoTecnico) {

        String dados =
                montarDadosParaIA(
                        veiculo,
                        cidade,
                        resultadoTecnico
                );

        try {

            return iaPlannerService.gerarPlanejamentoRota(dados);

        } catch (RuntimeException erro) {

            return "PLANEJADOR DE ROTAS\n" +
                    "----------------------------------------\n" +
                    "A IA não conseguiu processar a análise no momento.\n\n" +
                    "Resultado técnico da simulação:\n\n" +
                    resultadoTecnico;
        }
    }

    private String montarDadosParaIA(Veiculo veiculo,
                                     Cidade cidade,
                                     String resultadoTecnico) {

        StringBuilder dados =
                new StringBuilder();

        dados.append("Resultado técnico da simulação:\n")
                .append(resultadoTecnico)
                .append("\n\n");

        dados.append("Dados reais simulados:\n")
                .append("Clima: condição normal\n")
                .append("Trânsito: fluxo moderado\n")
                .append("Critério principal: autonomia atual do veículo e distância até o destino\n\n");

        dados.append("Veículo selecionado:\n")
                .append("ID: ")
                .append(veiculo.getId())
                .append("\n")
                .append("Tipo: ")
                .append(identificarTipoVeiculo(veiculo))
                .append("\n")
                .append("Modelo: ")
                .append(veiculo.getModelo())
                .append("\n")
                .append("Autonomia máxima: ")
                .append(veiculo.getAutonomiaMaxima())
                .append(" km\n")
                .append("Carga atual da bateria: ")
                .append(veiculo.getCargaBateriaAtual())
                .append("%\n")
                .append("Autonomia atual calculada: ")
                .append(veiculo.calcularAutonomiaAtual())
                .append(" km\n")
                .append("Consumo kWh/km: ")
                .append(veiculo.getConsumoKwhPorKm())
                .append("\n")
                .append("Tempo de recarga completa: ")
                .append(veiculo.getTempoRecargaCompleta())
                .append(" minutos\n")
                .append(montarDadosExtrasVeiculo(veiculo))
                .append("\n");

        dados.append("Cidade de destino:\n")
                .append("ID: ")
                .append(cidade.getId())
                .append("\n")
                .append("Nome: ")
                .append(cidade.getNome())
                .append("\n")
                .append("Estado: ")
                .append(cidade.getEstado())
                .append("\n")
                .append("Distância da capital: ")
                .append(cidade.getDistanciaDaCapital())
                .append(" km\n\n");

        dados.append("Eletropostos cadastrados:\n")
                .append(listarEletropostosParaIA());

        return dados.toString();
    }

    private String identificarTipoVeiculo(Veiculo veiculo) {

        if (veiculo instanceof VeiculoEletrico) {
            return "Elétrico";
        }

        if (veiculo instanceof VeiculoHibrido) {
            return "Híbrido";
        }

        return "Não identificado";
    }

    private String montarDadosExtrasVeiculo(Veiculo veiculo) {

        StringBuilder dados =
                new StringBuilder();

        if (veiculo instanceof VeiculoEletrico) {

            VeiculoEletrico eletrico =
                    (VeiculoEletrico) veiculo;

            dados.append("Tipo de conector: ")
                    .append(eletrico.getTipoConector())
                    .append("\n")
                    .append("Tempo de recarga rápida: ")
                    .append(eletrico.getTempoRecargaRapida())
                    .append(" minutos\n");
        }

        if (veiculo instanceof VeiculoHibrido) {

            VeiculoHibrido hibrido =
                    (VeiculoHibrido) veiculo;

            dados.append("Capacidade do tanque: ")
                    .append(hibrido.getCapacidadeTanqueCombustivel())
                    .append("\n")
                    .append("Consumo de combustível: ")
                    .append(hibrido.getConsumoCombustivel())
                    .append(" km/l\n")
                    .append("Tipo de combustível: ")
                    .append(hibrido.getTipoCombustivel())
                    .append("\n");
        }

        return dados.toString();
    }

    private String listarEletropostosParaIA() {

        ArrayList<Eletroposto> eletropostos =
                eletropostoController.listar();

        if (eletropostos.size() == 0) {
            return "Nenhum eletroposto cadastrado.\n";
        }

        StringBuilder texto =
                new StringBuilder();

        for (int i = 0; i < eletropostos.size(); i++) {

            Eletroposto eletroposto =
                    eletropostos.get(i);

            Cidade cidade =
                    cidadeController.buscarPorId(
                            eletroposto.getCidadeId()
                    );

            String nomeCidade;

            if (cidade != null) {
                nomeCidade = cidade.getNome();
            } else {
                nomeCidade = "Cidade não encontrada";
            }

            texto.append("ID: ")
                    .append(eletroposto.getId())
                    .append("\n")
                    .append("Nome: ")
                    .append(eletroposto.getNome())
                    .append("\n")
                    .append("Localização: ")
                    .append(eletroposto.getLocalizacao())
                    .append("\n")
                    .append("Cidade: ")
                    .append(nomeCidade)
                    .append("\n")
                    .append("Conectores: ")
                    .append(eletroposto.getTiposConectoresDisponiveis())
                    .append("\n")
                    .append("Potência: ")
                    .append(eletroposto.getPotenciaCargaKw())
                    .append(" kW\n")
                    .append("Preço por kWh: R$ ")
                    .append(eletroposto.getPrecoPorKwh())
                    .append("\n")
                    .append("Vagas disponíveis: ")
                    .append(eletroposto.getVagasDisponiveis())
                    .append("\n\n");
        }

        return texto.toString();
    }
}
