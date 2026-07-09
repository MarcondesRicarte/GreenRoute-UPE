package view;

import controller.EletropostoController;
import controller.CidadeController;
import model.Eletroposto;
import model.Cidade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PainelEletropostos extends JPanel {

    private final EletropostoController eletropostoController;
    private final CidadeController cidadeController;

    private DefaultTableModel tabelaModel;
    private JTable tabela;
    private JTextField campoNome, campoLocalizacao, campoConectores, campoPotencia, campoPreco, campoVagas;
    private JComboBox<Cidade> comboCidade;
    private JButton botaoSalvar, botaoApagar, botaoCancelarEdicao;
    private Integer idEmEdicao = null;

    public PainelEletropostos(EletropostoController eletropostoController, CidadeController cidadeController) {
        this.eletropostoController = eletropostoController;
        this.cidadeController = cidadeController;

        setLayout(new BorderLayout(10, 10));
        add(construirFormulario(), BorderLayout.NORTH);
        add(construirTabela(), BorderLayout.CENTER);

        atualizarTabela();
    }

    private JPanel construirFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Cadastrar / Editar Eletroposto"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        campoNome = new JTextField(15);
        campoLocalizacao = new JTextField(15);
        comboCidade = new JComboBox<>();
        campoConectores = new JTextField(12);
        campoPotencia = new JTextField(8);
        campoPreco = new JTextField(8);
        campoVagas = new JTextField(5);

        atualizarComboCidades();

        int linha = 0;
        adicionarCampo(painel, c, linha++, "Nome:", campoNome);
        adicionarCampo(painel, c, linha++, "Localização:", campoLocalizacao);
        adicionarCampo(painel, c, linha++, "Cidade:", comboCidade);

        JButton botaoAtualizarCidades = new JButton("Atualizar lista de cidades");
        botaoAtualizarCidades.addActionListener(e -> atualizarComboCidades());
        c.gridx = 0; c.gridy = linha++; c.gridwidth = 2;
        painel.add(botaoAtualizarCidades, c);
        c.gridwidth = 1;

        adicionarCampo(painel, c, linha++, "Conectores disponíveis:", campoConectores);
        adicionarCampo(painel, c, linha++, "Potência de carga (kW):", campoPotencia);
        adicionarCampo(painel, c, linha++, "Preço por kWh (R$):", campoPreco);
        adicionarCampo(painel, c, linha++, "Vagas disponíveis:", campoVagas);

        botaoSalvar = new JButton("Cadastrar Eletroposto");
        botaoSalvar.addActionListener(e -> salvar());

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

    private void adicionarCampo(JPanel painel, GridBagConstraints c, int linha, String rotulo, JComponent campo) {
        c.gridx = 0; c.gridy = linha; c.gridwidth = 1;
        painel.add(new JLabel(rotulo), c);
        c.gridx = 1;
        painel.add(campo, c);
    }

    private void atualizarComboCidades() {
        comboCidade.removeAllItems();
        ArrayList<Cidade> cidades = cidadeController.listarCidadesController();
        for (Cidade cidade : cidades) {
            comboCidade.addItem(cidade);
        }
    }

    private void salvar() {
        if (comboCidade.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Cadastre uma cidade antes de cadastrar um eletroposto.",
                    "Nenhuma cidade disponível", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Cidade cidadeSelecionada = (Cidade) comboCidade.getSelectedItem();

            Eletroposto eletroposto = new Eletroposto(
                    0, campoNome.getText(), campoLocalizacao.getText(), cidadeSelecionada.getId(),
                    campoConectores.getText(),
                    Double.parseDouble(campoPotencia.getText()),
                    Double.parseDouble(campoPreco.getText()),
                    Integer.parseInt(campoVagas.getText())
            );

            if (idEmEdicao == null) {
                eletropostoController.cadastrarEletropostoController(eletroposto);
                JOptionPane.showMessageDialog(this, "Eletroposto cadastrado com sucesso!");
            } else {
                eletropostoController.atualizarEletropostoController(idEmEdicao, eletroposto);
                JOptionPane.showMessageDialog(this, "Eletroposto atualizado com sucesso!");
            }

            cancelarEdicao();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifique os campos numéricos (potência, preço, vagas).",
                    "Erro de preenchimento", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Dado inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apagarSelecionado() {
        if (idEmEdicao == null) return;

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja apagar este eletroposto?", "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            eletropostoController.apagarEletropostoController(idEmEdicao);
            JOptionPane.showMessageDialog(this, "Eletroposto apagado com sucesso!");
            cancelarEdicao();
            atualizarTabela();
        }
    }

    private void cancelarEdicao() {
        idEmEdicao = null;
        campoNome.setText("");
        campoLocalizacao.setText("");
        campoConectores.setText("");
        campoPotencia.setText("");
        campoPreco.setText("");
        campoVagas.setText("");
        botaoSalvar.setText("Cadastrar Eletroposto");
        botaoApagar.setEnabled(false);
        botaoCancelarEdicao.setEnabled(false);
        tabela.clearSelection();
    }

    private JScrollPane construirTabela() {
        tabelaModel = new DefaultTableModel(
                new String[]{"ID", "Nome", "Localização", "Cidade ID", "Conectores", "Potência (kW)", "Preço/kWh", "Vagas"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(tabelaModel);

        tabela.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                int linha = tabela.getSelectedRow();
                int id = (int) tabelaModel.getValueAt(linha, 0);
                Eletroposto eletroposto = eletropostoController.buscarEletropostoPorIdController(id);
                if (eletroposto == null) return;

                idEmEdicao = id;
                campoNome.setText(eletroposto.getNome());
                campoLocalizacao.setText(eletroposto.getLocalizacao());
                campoConectores.setText(eletroposto.getTiposConectoresDisponiveis());
                campoPotencia.setText(String.valueOf(eletroposto.getPotenciaCargaKw()));
                campoPreco.setText(String.valueOf(eletroposto.getPrecoPorKwh()));
                campoVagas.setText(String.valueOf(eletroposto.getVagasDisponiveis()));

                for (int i = 0; i < comboCidade.getItemCount(); i++) {
                    if (comboCidade.getItemAt(i).getId() == eletroposto.getCidadeId()) {
                        comboCidade.setSelectedIndex(i);
                        break;
                    }
                }

                botaoSalvar.setText("Salvar Alterações");
                botaoApagar.setEnabled(true);
                botaoCancelarEdicao.setEnabled(true);
            }
        });

        return new JScrollPane(tabela);
    }

    private void atualizarTabela() {
        tabelaModel.setRowCount(0);
        ArrayList<Eletroposto> eletropostos = eletropostoController.listarEletropostoController();
        for (Eletroposto e : eletropostos) {
            tabelaModel.addRow(new Object[]{
                    e.getId(), e.getNome(), e.getLocalizacao(), e.getCidadeId(),
                    e.getTiposConectoresDisponiveis(), e.getPotenciaCargaKw(), e.getPrecoPorKwh(), e.getVagasDisponiveis()
            });
        }
    }
}