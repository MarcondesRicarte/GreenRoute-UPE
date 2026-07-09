package view;

import controller.VeiculoController;
import Gemini.CadastroVeiculoIA;
import model.Veiculo;
import model.VeiculoEletrico;
import model.VeiculoHibrido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PainelVeiculos extends JPanel {

    private final VeiculoController veiculoController;
    private final CadastroVeiculoIA cadastroVeiculoIA;

    private DefaultTableModel tabelaModel;
    private JTable tabela;

    private JComboBox<String> comboTipo;
    private JTextField campoModelo, campoAutonomia, campoCarga, campoConsumo, campoTempoRecarga;
    private CardLayout cardLayoutCamposEspecificos;
    private JPanel painelCamposEspecificos;

    private JTextField campoTipoConector, campoTempoRecargaRapida;
    private JTextField campoCapacidadeTanque, campoConsumoCombustivel, campoTipoCombustivel;

    private JTextArea campoDescricaoIA;
    private JButton botaoCadastrarIA;

    private JButton botaoSalvar, botaoApagar, botaoCancelarEdicao;
    private Integer idEmEdicao = null; // null = modo "novo cadastro"; != null = editando esse ID

    public PainelVeiculos(VeiculoController veiculoController, CadastroVeiculoIA cadastroVeiculoIA) {
        this.veiculoController = veiculoController;
        this.cadastroVeiculoIA = cadastroVeiculoIA;

        setLayout(new BorderLayout(10, 10));
        add(construirFormularioManual(), BorderLayout.NORTH);
        add(construirTabela(), BorderLayout.CENTER);
        add(construirPainelIA(), BorderLayout.SOUTH);

        atualizarTabela();
    }

    // =====================================================================
    // FORMULÁRIO MANUAL
    // =====================================================================

    private JPanel construirFormularioManual() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Cadastrar / Editar Veículo"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        comboTipo = new JComboBox<>(new String[]{"Elétrico", "Híbrido"});
        campoModelo = new JTextField(15);
        campoAutonomia = new JTextField(8);
        campoCarga = new JTextField(8);
        campoConsumo = new JTextField(8);
        campoTempoRecarga = new JTextField(8);

        int linha = 0;
        adicionarCampo(painel, c, linha++, "Tipo:", comboTipo);
        adicionarCampo(painel, c, linha++, "Modelo:", campoModelo);
        adicionarCampo(painel, c, linha++, "Autonomia máxima (km):", campoAutonomia);
        adicionarCampo(painel, c, linha++, "Carga da bateria (0-100):", campoCarga);
        adicionarCampo(painel, c, linha++, "Consumo (kWh/km):", campoConsumo);
        adicionarCampo(painel, c, linha++, "Tempo recarga completa (min):", campoTempoRecarga);

        cardLayoutCamposEspecificos = new CardLayout();
        painelCamposEspecificos = new JPanel(cardLayoutCamposEspecificos);
        painelCamposEspecificos.add(construirCamposEletrico(), "Elétrico");
        painelCamposEspecificos.add(construirCamposHibrido(), "Híbrido");

        c.gridx = 0; c.gridy = linha; c.gridwidth = 2;
        painel.add(painelCamposEspecificos, c);
        linha++;

        comboTipo.addActionListener(e ->
                cardLayoutCamposEspecificos.show(painelCamposEspecificos, (String) comboTipo.getSelectedItem())
        );

        botaoSalvar = new JButton("Cadastrar Veículo");
        botaoSalvar.addActionListener(e -> salvarManual());

        botaoApagar = new JButton("Apagar Selecionado");
        botaoApagar.setEnabled(false);
        botaoApagar.addActionListener(e -> apagarSelecionado());

        botaoCancelarEdicao = new JButton("Cancelar Edição");
        botaoCancelarEdicao.setEnabled(false);
        botaoCancelarEdicao.addActionListener(e -> cancelarEdicao());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoApagar);
        painelBotoes.add(botaoCancelarEdicao);

        c.gridx = 0; c.gridy = linha; c.gridwidth = 2;
        painel.add(painelBotoes, c);

        return painel;
    }

    private JPanel construirCamposEletrico() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        campoTipoConector = new JTextField(10);
        campoTempoRecargaRapida = new JTextField(8);

        adicionarCampo(p, c, 0, "Tipo de conector:", campoTipoConector);
        adicionarCampo(p, c, 1, "Tempo recarga rápida (min):", campoTempoRecargaRapida);
        return p;
    }

    private JPanel construirCamposHibrido() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        campoCapacidadeTanque = new JTextField(8);
        campoConsumoCombustivel = new JTextField(8);
        campoTipoCombustivel = new JTextField(10);

        adicionarCampo(p, c, 0, "Capacidade do tanque (L):", campoCapacidadeTanque);
        adicionarCampo(p, c, 1, "Consumo combustível (km/L):", campoConsumoCombustivel);
        adicionarCampo(p, c, 2, "Tipo de combustível:", campoTipoCombustivel);
        return p;
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints c, int linha, String rotulo, JComponent campo) {
        c.gridx = 0; c.gridy = linha; c.gridwidth = 1;
        painel.add(new JLabel(rotulo), c);
        c.gridx = 1;
        painel.add(campo, c);
    }

    // =====================================================================
    // SALVAR (CREATE ou UPDATE, dependendo de idEmEdicao) / APAGAR / CANCELAR
    // =====================================================================

    private void salvarManual() {
        try {
            String modelo = campoModelo.getText();
            double autonomia = Double.parseDouble(campoAutonomia.getText());
            double carga = Double.parseDouble(campoCarga.getText());
            double consumo = Double.parseDouble(campoConsumo.getText());
            int tempoRecarga = Integer.parseInt(campoTempoRecarga.getText());

            Veiculo veiculo;
            if ("Elétrico".equals(comboTipo.getSelectedItem())) {
                veiculo = new VeiculoEletrico(0, modelo, autonomia, carga, consumo, tempoRecarga,
                        campoTipoConector.getText(),
                        Integer.parseInt(campoTempoRecargaRapida.getText()));
            } else {
                veiculo = new VeiculoHibrido(0, modelo, autonomia, carga, consumo, tempoRecarga,
                        Double.parseDouble(campoCapacidadeTanque.getText()),
                        Double.parseDouble(campoConsumoCombustivel.getText()),
                        campoTipoCombustivel.getText());
            }

            if (idEmEdicao == null) {
                veiculoController.cadastrarVeiculoController(veiculo);
                JOptionPane.showMessageDialog(this, "Veículo cadastrado com sucesso!");
            } else {
                veiculoController.atualizarVeiculoController(idEmEdicao, veiculo);
                JOptionPane.showMessageDialog(this, "Veículo atualizado com sucesso!");
            }

            cancelarEdicao();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifique se todos os campos numéricos foram preenchidos corretamente.",
                    "Erro de preenchimento", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Dado inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apagarSelecionado() {
        if (idEmEdicao == null) return;

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja apagar este veículo?", "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            veiculoController.apagarVeiculoController(idEmEdicao);
            JOptionPane.showMessageDialog(this, "Veículo apagado com sucesso!");
            cancelarEdicao();
            atualizarTabela();
        }
    }

    private void cancelarEdicao() {
        idEmEdicao = null;
        limparFormulario();
        comboTipo.setEnabled(true);
        comboTipo.setSelectedIndex(0);
        botaoSalvar.setText("Cadastrar Veículo");
        botaoApagar.setEnabled(false);
        botaoCancelarEdicao.setEnabled(false);
        tabela.clearSelection();
    }

    private void limparFormulario() {
        campoModelo.setText("");
        campoAutonomia.setText("");
        campoCarga.setText("");
        campoConsumo.setText("");
        campoTempoRecarga.setText("");
        campoTipoConector.setText("");
        campoTempoRecargaRapida.setText("");
        campoCapacidadeTanque.setText("");
        campoConsumoCombustivel.setText("");
        campoTipoCombustivel.setText("");
    }

    // =====================================================================
    // PREENCHER FORMULÁRIO (reaproveitado pela seleção na tabela E pela IA)
    // =====================================================================

    private void preencherFormulario(Veiculo veiculo) {
        campoModelo.setText(veiculo.getModelo());
        campoAutonomia.setText(String.valueOf(veiculo.getAutonomiaMaxima()));
        campoCarga.setText(String.valueOf(veiculo.getCargaBateriaAtual()));
        campoConsumo.setText(String.valueOf(veiculo.getConsumoKwhPorKm()));
        campoTempoRecarga.setText(String.valueOf(veiculo.getTempoRecargaCompleta()));

        if (veiculo instanceof VeiculoEletrico ve) {
            comboTipo.setSelectedItem("Elétrico");
            cardLayoutCamposEspecificos.show(painelCamposEspecificos, "Elétrico");
            campoTipoConector.setText(ve.getTipoConector());
            campoTempoRecargaRapida.setText(String.valueOf(ve.getTempoRecargaRapida()));
        } else if (veiculo instanceof VeiculoHibrido vh) {
            comboTipo.setSelectedItem("Híbrido");
            cardLayoutCamposEspecificos.show(painelCamposEspecificos, "Híbrido");
            campoCapacidadeTanque.setText(String.valueOf(vh.getCapacidadeTanqueCombustivel()));
            campoConsumoCombustivel.setText(String.valueOf(vh.getConsumoCombustivel()));
            campoTipoCombustivel.setText(vh.getTipoCombustivel());
        }
    }

    // =====================================================================
    // TABELA DE LISTAGEM (agora com seleção pra editar)
    // =====================================================================

    private JScrollPane construirTabela() {
        tabelaModel = new DefaultTableModel(
                new String[]{"ID", "Modelo", "Tipo", "Autonomia (km)", "Carga (%)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(tabelaModel);

        tabela.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                int linha = tabela.getSelectedRow();
                int id = (int) tabelaModel.getValueAt(linha, 0);
                Veiculo veiculo = veiculoController.buscarVeiculoPorId(id);
                if (veiculo == null) return;

                idEmEdicao = id;
                preencherFormulario(veiculo);
                comboTipo.setEnabled(false); // não permite trocar o tipo de um veículo já existente
                botaoSalvar.setText("Salvar Alterações");
                botaoApagar.setEnabled(true);
                botaoCancelarEdicao.setEnabled(true);
            }
        });

        return new JScrollPane(tabela);
    }

    private void atualizarTabela() {
        tabelaModel.setRowCount(0);
        ArrayList<Veiculo> veiculos = veiculoController.listarTodosVeiculosController();
        for (Veiculo v : veiculos) {
            String tipo = (v instanceof VeiculoEletrico) ? "Elétrico" : "Híbrido";
            tabelaModel.addRow(new Object[]{
                    v.getId(), v.getModelo(), tipo, v.getAutonomiaMaxima(), v.getCargaBateriaAtual()
            });
        }
    }

    // =====================================================================
    // CADASTRO RÁPIDO COM IA — agora preenche o formulário, não salva direto
    // =====================================================================

    private JPanel construirPainelIA() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Cadastro Rápido com IA (revise antes de salvar)"));

        campoDescricaoIA = new JTextArea(3, 40);
        campoDescricaoIA.setLineWrap(true);
        botaoCadastrarIA = new JButton("Interpretar");

        botaoCadastrarIA.addActionListener(e -> interpretarViaIA());

        painel.add(new JScrollPane(campoDescricaoIA), BorderLayout.CENTER);
        painel.add(botaoCadastrarIA, BorderLayout.EAST);
        return painel;
    }

    private void interpretarViaIA() {
        String descricao = campoDescricaoIA.getText();
        if (descricao.isBlank()) {
            JOptionPane.showMessageDialog(this, "Descreva o veículo antes de enviar.");
            return;
        }

        botaoCadastrarIA.setEnabled(false);
        botaoCadastrarIA.setText("Consultando Gemini...");

        new SwingWorker<Veiculo, Void>() {
            @Override
            protected Veiculo doInBackground() {
                return cadastroVeiculoIA.interpretarDescricao(descricao);
            }

            @Override
            protected void done() {
                botaoCadastrarIA.setEnabled(true);
                botaoCadastrarIA.setText("Interpretar");

                try {
                    Veiculo veiculo = get();
                    cancelarEdicao();          // garante que estamos em modo "novo cadastro"
                    preencherFormulario(veiculo);
                    campoDescricaoIA.setText("");
                    JOptionPane.showMessageDialog(PainelVeiculos.this,
                            "Dados interpretados! Revise os campos e clique em \"Cadastrar Veículo\" para confirmar.");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PainelVeiculos.this,
                            "Não foi possível interpretar a descrição: " + ex.getCause().getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}