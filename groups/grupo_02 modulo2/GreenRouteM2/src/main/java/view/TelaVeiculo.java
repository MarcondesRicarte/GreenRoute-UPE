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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.VeiculoController;
import model.Veiculo;
import model.VeiculoEletrico;
import model.VeiculoHibrido;
import service.IAPlannerService;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TelaVeiculo extends JFrame {

    private VeiculoController veiculoController;
    private IAPlannerService iaPlannerService;

    private JComboBox<String> comboTipo;

    private JTextField campoId;
    private JTextField campoModelo;
    private JTextField campoAutonomia;
    private JTextField campoBateria;
    private JTextField campoConsumoKwh;
    private JTextField campoTempoRecarga;

    private JTextField campoConector;
    private JTextField campoTempoRapido;

    private JTextField campoTanque;
    private JTextField campoConsumoCombustivel;
    private JTextField campoCombustivel;

    private JTextArea areaCadastroRapido;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaVeiculo(VeiculoController veiculoController,
                       IAPlannerService iaPlannerService) {

        this.veiculoController = veiculoController;
        this.iaPlannerService = iaPlannerService;

        configurarJanela();
        montarTela();
        atualizarCamposTipo();
        atualizarTabela();
    }

    private void configurarJanela() {

        setTitle("Gerenciamento de Veículos");
        setSize(1000, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void montarTela() {

        JPanel painelFormulario =
                new JPanel(new GridLayout(12, 2, 10, 5));

        comboTipo =
                new JComboBox<>();

        comboTipo.addItem("Elétrico");
        comboTipo.addItem("Híbrido");

        campoId = new JTextField();
        campoModelo = new JTextField();
        campoAutonomia = new JTextField();
        campoBateria = new JTextField();
        campoConsumoKwh = new JTextField();
        campoTempoRecarga = new JTextField();

        campoConector = new JTextField();
        campoTempoRapido = new JTextField();

        campoTanque = new JTextField();
        campoConsumoCombustivel = new JTextField();
        campoCombustivel = new JTextField();

        painelFormulario.add(new JLabel("Tipo de Veículo:"));
        painelFormulario.add(comboTipo);

        painelFormulario.add(new JLabel("ID:"));
        painelFormulario.add(campoId);

        painelFormulario.add(new JLabel("Modelo:"));
        painelFormulario.add(campoModelo);

        painelFormulario.add(new JLabel("Autonomia Máxima (km):"));
        painelFormulario.add(campoAutonomia);

        painelFormulario.add(new JLabel("Carga da Bateria Atual (%):"));
        painelFormulario.add(campoBateria);

        painelFormulario.add(new JLabel("Consumo kWh/km:"));
        painelFormulario.add(campoConsumoKwh);

        painelFormulario.add(new JLabel("Tempo Recarga Completa (min):"));
        painelFormulario.add(campoTempoRecarga);

        painelFormulario.add(new JLabel("Tipo de Conector:"));
        painelFormulario.add(campoConector);

        painelFormulario.add(new JLabel("Tempo Recarga Rápida (min):"));
        painelFormulario.add(campoTempoRapido);

        painelFormulario.add(new JLabel("Capacidade do Tanque:"));
        painelFormulario.add(campoTanque);

        painelFormulario.add(new JLabel("Consumo Combustível (km/l):"));
        painelFormulario.add(campoConsumoCombustivel);

        painelFormulario.add(new JLabel("Tipo de Combustível:"));
        painelFormulario.add(campoCombustivel);

        areaCadastroRapido =
                new JTextArea(3, 30);

        areaCadastroRapido.setLineWrap(true);
        areaCadastroRapido.setWrapStyleWord(true);

        JScrollPane scrollCadastroRapido =
                new JScrollPane(areaCadastroRapido);

        JButton botaoPreencherIA =
                new JButton("Preencher com IA");

        JPanel painelIA =
                new JPanel(new BorderLayout(10, 5));

        painelIA.add(new JLabel("Cadastro Rápido por IA:"), BorderLayout.NORTH);
        painelIA.add(scrollCadastroRapido, BorderLayout.CENTER);
        painelIA.add(botaoPreencherIA, BorderLayout.EAST);

        JPanel painelBotoes =
                new JPanel(new GridLayout(1, 5, 10, 10));

        JButton botaoCadastrar =
                new JButton("Cadastrar");

        JButton botaoAtualizar =
                new JButton("Atualizar");

        JButton botaoExcluir =
                new JButton("Excluir");

        JButton botaoLimpar =
                new JButton("Limpar");

        JButton botaoVoltar =
                new JButton("Voltar");

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoAtualizar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoLimpar);
        painelBotoes.add(botaoVoltar);

        modeloTabela =
                new DefaultTableModel();

        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Tipo");
        modeloTabela.addColumn("Modelo");
        modeloTabela.addColumn("Autonomia");
        modeloTabela.addColumn("Bateria");
        modeloTabela.addColumn("Consumo");
        modeloTabela.addColumn("Tempo Recarga");
        modeloTabela.addColumn("Informação Extra");

        tabela =
                new JTable(modeloTabela);

        JScrollPane painelTabela =
                new JScrollPane(tabela);

        JPanel painelFormularioComIA =
                new JPanel(new BorderLayout(10, 10));

        painelFormularioComIA.add(painelFormulario, BorderLayout.CENTER);
        painelFormularioComIA.add(painelIA, BorderLayout.SOUTH);

        JPanel painelSuperior =
                new JPanel(new BorderLayout(10, 10));

        painelSuperior.add(painelFormularioComIA, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);
        add(painelTabela, BorderLayout.CENTER);

        comboTipo.addActionListener(e -> atualizarCamposTipo());

        botaoCadastrar.addActionListener(e -> cadastrarVeiculo());

        botaoAtualizar.addActionListener(e -> atualizarVeiculo());

        botaoExcluir.addActionListener(e -> excluirVeiculo());

        botaoLimpar.addActionListener(e -> limparCampos());

        botaoVoltar.addActionListener(e -> dispose());

        botaoPreencherIA.addActionListener(e -> preencherComIA());

                tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                                if (!e.getValueIsAdjusting()) {
                                        preencherCampos();
                                }
                        }
                });

        TemaRosa.aplicar(getContentPane());
        TemaRosa.aplicarTabela(tabela);
    }

    private void atualizarCamposTipo() {

        String tipo =
                comboTipo.getSelectedItem().toString();

        boolean eletrico =
                tipo.equals("Elétrico");

        campoConector.setEnabled(eletrico);
        campoTempoRapido.setEnabled(eletrico);

        campoTanque.setEnabled(!eletrico);
        campoConsumoCombustivel.setEnabled(!eletrico);
        campoCombustivel.setEnabled(!eletrico);
    }

    private void cadastrarVeiculo() {

        try {

            int id =
                    converterInt(campoId.getText());

                        if (veiculoController.buscarPorId(id) != null) {

                                JOptionPane.showMessageDialog(
                                                this,
                                                "Já existe um veículo com esse ID."
                                );

                                return;
                        }

                        String tipo = comboTipo.getSelectedItem().toString();

                        Veiculo novo;

                        if (tipo.equals("Elétrico")) {
                                double autonomia = converterDouble(campoAutonomia.getText());
                                double bateria = converterDouble(campoBateria.getText());
                                double consumo = converterDouble(campoConsumoKwh.getText());
                                int tempo = converterInt(campoTempoRecarga.getText());
                                String conector = campoConector.getText();
                                int rapido = converterInt(campoTempoRapido.getText());

                                novo = new VeiculoEletrico(id, campoModelo.getText(), autonomia, bateria, consumo, tempo, conector, rapido);
                        } else {
                                double autonomia = converterDouble(campoAutonomia.getText());
                                double bateria = converterDouble(campoBateria.getText());
                                double consumo = converterDouble(campoConsumoKwh.getText());
                                int tempo = converterInt(campoTempoRecarga.getText());
                                double tanque = converterDouble(campoTanque.getText());
                                double consumoComb = converterDouble(campoConsumoCombustivel.getText());
                                String combustivel = campoCombustivel.getText();

                                novo = new VeiculoHibrido(id, campoModelo.getText(), autonomia, bateria, consumo, tempo, tanque, consumoComb, combustivel);
                        }

                        veiculoController.cadastrar(novo);

                        atualizarTabela();
                        limparCampos();

                        JOptionPane.showMessageDialog(this, "Veículo cadastrado com sucesso.");

                } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Dados numéricos inválidos: " + ex.getMessage());
                } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao cadastrar veículo: " + ex.getMessage());
                }

        }

        private void atualizarTabela() {

                modeloTabela.setRowCount(0);

                for (Veiculo v : veiculoController.listar()) {
                        String tipo = (v instanceof VeiculoEletrico) ? "Elétrico" : "Híbrido";

                        String infoExtra = "";

                        if (v instanceof VeiculoEletrico) {
                                VeiculoEletrico ve = (VeiculoEletrico) v;
                                infoExtra = "Conector: " + ve.getTipoConector();
                        } else if (v instanceof VeiculoHibrido) {
                                VeiculoHibrido vh = (VeiculoHibrido) v;
                                infoExtra = "Tanque: " + vh.getCapacidadeTanqueCombustivel();
                        }

                        modeloTabela.addRow(new Object[]{
                                        v.getId(),
                                        tipo,
                                        v.getModelo(),
                                        v.getAutonomiaMaxima(),
                                        v.getCargaBateriaAtual(),
                                        v.getConsumoKwhPorKm(),
                                        v.getTempoRecargaCompleta(),
                                        infoExtra
                        });
                }
        }

        private void preencherCampos() {

                int linha = tabela.getSelectedRow();

                if (linha < 0) {
                        return;
                }

                Object idObj = modeloTabela.getValueAt(linha, 0);

                if (idObj == null) return;

                int id = Integer.parseInt(idObj.toString());

                Veiculo v = veiculoController.buscarPorId(id);

                if (v == null) return;

                campoId.setText(String.valueOf(v.getId()));
                campoModelo.setText(v.getModelo());
                campoAutonomia.setText(String.valueOf(v.getAutonomiaMaxima()));
                campoBateria.setText(String.valueOf(v.getCargaBateriaAtual()));
                campoConsumoKwh.setText(String.valueOf(v.getConsumoKwhPorKm()));
                campoTempoRecarga.setText(String.valueOf(v.getTempoRecargaCompleta()));

                if (v instanceof VeiculoEletrico) {
                        comboTipo.setSelectedItem("Elétrico");
                        VeiculoEletrico ve = (VeiculoEletrico) v;
                        campoConector.setText(ve.getTipoConector());
                        campoTempoRapido.setText(String.valueOf(ve.getTempoRecargaRapida()));
                } else if (v instanceof VeiculoHibrido) {
                        comboTipo.setSelectedItem("Híbrido");
                        VeiculoHibrido vh = (VeiculoHibrido) v;
                        campoTanque.setText(String.valueOf(vh.getCapacidadeTanqueCombustivel()));
                        campoConsumoCombustivel.setText(String.valueOf(vh.getConsumoCombustivel()));
                        campoCombustivel.setText(vh.getTipoCombustivel());
                }

                atualizarCamposTipo();
        }

        private void atualizarVeiculo() {

                try {
                        int id = converterInt(campoId.getText());

                        if (veiculoController.buscarPorId(id) == null) {
                                JOptionPane.showMessageDialog(this, "Veículo não encontrado para atualização.");
                                return;
                        }

                        String tipo = comboTipo.getSelectedItem().toString();
                        Veiculo novo;

                        if (tipo.equals("Elétrico")) {
                                double autonomia = converterDouble(campoAutonomia.getText());
                                double bateria = converterDouble(campoBateria.getText());
                                double consumo = converterDouble(campoConsumoKwh.getText());
                                int tempo = converterInt(campoTempoRecarga.getText());
                                String conector = campoConector.getText();
                                int rapido = converterInt(campoTempoRapido.getText());

                                novo = new VeiculoEletrico(id, campoModelo.getText(), autonomia, bateria, consumo, tempo, conector, rapido);
                        } else {
                                double autonomia = converterDouble(campoAutonomia.getText());
                                double bateria = converterDouble(campoBateria.getText());
                                double consumo = converterDouble(campoConsumoKwh.getText());
                                int tempo = converterInt(campoTempoRecarga.getText());
                                double tanque = converterDouble(campoTanque.getText());
                                double consumoComb = converterDouble(campoConsumoCombustivel.getText());
                                String combustivel = campoCombustivel.getText();

                                novo = new VeiculoHibrido(id, campoModelo.getText(), autonomia, bateria, consumo, tempo, tanque, consumoComb, combustivel);
                        }

                        boolean ok = veiculoController.atualizar(id, novo);

                        if (ok) {
                                atualizarTabela();
                                JOptionPane.showMessageDialog(this, "Veículo atualizado com sucesso.");
                        } else {
                                JOptionPane.showMessageDialog(this, "Falha ao atualizar veículo.");
                        }

                } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Dados numéricos inválidos: " + ex.getMessage());
                }
        }

        private void excluirVeiculo() {

                try {
                        int id = converterInt(campoId.getText());

                        boolean ok = veiculoController.excluir(id);

                        if (ok) {
                                atualizarTabela();
                                limparCampos();
                                JOptionPane.showMessageDialog(this, "Veículo excluído com sucesso.");
                        } else {
                                JOptionPane.showMessageDialog(this, "Veículo não encontrado para exclusão.");
                        }

                } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "ID inválido: " + ex.getMessage());
                }
        }

        private void limparCampos() {

                campoId.setText("");
                campoModelo.setText("");
                campoAutonomia.setText("");
                campoBateria.setText("");
                campoConsumoKwh.setText("");
                campoTempoRecarga.setText("");
                campoConector.setText("");
                campoTempoRapido.setText("");
                campoTanque.setText("");
                campoConsumoCombustivel.setText("");
                campoCombustivel.setText("");
                areaCadastroRapido.setText("");

                tabela.clearSelection();
                atualizarCamposTipo();
        }

        private void preencherComIA() {

                try {
                        String resultado = iaPlannerService.interpretarCadastroRapido("veiculo", areaCadastroRapido.getText());

                        String[] linhas = resultado.split("\\r?\\n");

                        for (String linha : linhas) {
                                if (!linha.contains("=")) continue;
                                String[] partes = linha.split("=", 2);
                                String chave = partes[0].trim().toLowerCase();
                                String valor = partes[1].trim();

                                switch (chave) {
                                        case "id": campoId.setText(valor); break;
                                        case "tipo": comboTipo.setSelectedItem(valor); break;
                                        case "modelo": campoModelo.setText(valor); break;
                                        case "autonomia": campoAutonomia.setText(valor); break;
                                        case "bateria": campoBateria.setText(valor); break;
                                        case "consumokwh": campoConsumoKwh.setText(valor); break;
                                        case "temporecarga": campoTempoRecarga.setText(valor); break;
                                        case "conector": campoConector.setText(valor); break;
                                        case "temporapido": campoTempoRapido.setText(valor); break;
                                        case "tanque": campoTanque.setText(valor); break;
                                        case "consumocombustivel": campoConsumoCombustivel.setText(valor); break;
                                        case "combustivel": campoCombustivel.setText(valor); break;
                                }
                        }

                        atualizarCamposTipo();

                } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao preencher com IA: " + ex.getMessage());
                }
        }

        private int converterInt(String texto) {
                if (texto == null || texto.trim().isEmpty()) return 0;
                return Integer.parseInt(texto.trim());
        }

        private double converterDouble(String texto) {
                if (texto == null || texto.trim().isEmpty()) return 0.0;
                return Double.parseDouble(texto.trim().replace(',', '.'));
        }
}
