package view;

import controller.CidadeController;
import model.Cidade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PainelCidades extends JPanel {

    private final CidadeController cidadeController;
    private DefaultTableModel tabelaModel;
    private JTable tabela;

    private JTextField campoNome, campoEstado, campoDistancia;
    private JButton botaoSalvar, botaoApagar, botaoCancelarEdicao;
    private Integer idEmEdicao = null;

    public PainelCidades(CidadeController cidadeController) {
        this.cidadeController = cidadeController;
        setLayout(new BorderLayout(10, 10));

        add(construirFormulario(), BorderLayout.NORTH);
        add(construirTabela(), BorderLayout.CENTER);

        atualizarTabela();
    }

    private JPanel construirFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Cadastrar / Editar Cidade"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        campoNome = new JTextField(15);
        campoEstado = new JTextField(5);
        campoDistancia = new JTextField(8);

        c.gridx = 0; c.gridy = 0; painel.add(new JLabel("Nome:"), c);
        c.gridx = 1; painel.add(campoNome, c);
        c.gridx = 0; c.gridy = 1; painel.add(new JLabel("Estado (UF):"), c);
        c.gridx = 1; painel.add(campoEstado, c);
        c.gridx = 0; c.gridy = 2; painel.add(new JLabel("Distância da capital (km):"), c);
        c.gridx = 1; painel.add(campoDistancia, c);

        botaoSalvar = new JButton("Cadastrar Cidade");
        botaoSalvar.addActionListener(e -> salvar());

        botaoApagar = new JButton("Apagar Selecionada");
        botaoApagar.setEnabled(false);
        botaoApagar.addActionListener(e -> apagarSelecionada());

        botaoCancelarEdicao = new JButton("Cancelar Edição");
        botaoCancelarEdicao.setEnabled(false);
        botaoCancelarEdicao.addActionListener(e -> cancelarEdicao());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoApagar);
        painelBotoes.add(botaoCancelarEdicao);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        painel.add(painelBotoes, c);

        return painel;
    }

    private void salvar() {
        try {
            Cidade cidade = new Cidade(0, campoNome.getText(), campoEstado.getText(),
                    Double.parseDouble(campoDistancia.getText()));

            if (idEmEdicao == null) {
                cidadeController.cadastrarCidadeController(cidade);
                JOptionPane.showMessageDialog(this, "Cidade cadastrada com sucesso!");
            } else {
                cidadeController.atualizarCidadeController(idEmEdicao, cidade);
                JOptionPane.showMessageDialog(this, "Cidade atualizada com sucesso!");
            }

            cancelarEdicao();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Distância inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apagarSelecionada() {
        if (idEmEdicao == null) return;

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja apagar esta cidade? Eletropostos vinculados a ela podem ficar órfãos.",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            cidadeController.apagarCidadeController(idEmEdicao);
            JOptionPane.showMessageDialog(this, "Cidade apagada com sucesso!");
            cancelarEdicao();
            atualizarTabela();
        }
    }

    private void cancelarEdicao() {
        idEmEdicao = null;
        campoNome.setText("");
        campoEstado.setText("");
        campoDistancia.setText("");
        botaoSalvar.setText("Cadastrar Cidade");
        botaoApagar.setEnabled(false);
        botaoCancelarEdicao.setEnabled(false);
        tabela.clearSelection();
    }

    private JScrollPane construirTabela() {
        tabelaModel = new DefaultTableModel(new String[]{"ID", "Nome", "Estado", "Distância (km)"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(tabelaModel);

        tabela.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                int linha = tabela.getSelectedRow();
                int id = (int) tabelaModel.getValueAt(linha, 0);
                Cidade cidade = cidadeController.buscarCidadePorIdController(id);
                if (cidade == null) return;

                idEmEdicao = id;
                campoNome.setText(cidade.getNome());
                campoEstado.setText(cidade.getEstado());
                campoDistancia.setText(String.valueOf(cidade.getDistanciaDaCapital()));
                botaoSalvar.setText("Salvar Alterações");
                botaoApagar.setEnabled(true);
                botaoCancelarEdicao.setEnabled(true);
            }
        });

        return new JScrollPane(tabela);
    }

    private void atualizarTabela() {
        tabelaModel.setRowCount(0);
        ArrayList<Cidade> cidades = cidadeController.listarCidadesController();
        for (Cidade c : cidades) {
            tabelaModel.addRow(new Object[]{c.getId(), c.getNome(), c.getEstado(), c.getDistanciaDaCapital()});
        }
    }
}