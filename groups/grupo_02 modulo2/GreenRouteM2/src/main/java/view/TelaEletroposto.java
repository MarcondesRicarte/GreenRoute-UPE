package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.CidadeController;
import controller.EletropostoController;
import model.Cidade;
import model.Eletroposto;
import service.IAPlannerService;

public class TelaEletroposto extends JFrame {

    private EletropostoController eletropostoController;
    private CidadeController cidadeController;
    private IAPlannerService iaPlannerService;

    private JTextField campoId;
    private JTextField campoNome;
    private JTextField campoLocalizacao;
    private JTextField campoCidadeId;
    private JTextField campoConectores;
    private JTextField campoPotencia;
    private JTextField campoPreco;
    private JTextField campoVagas;

    private JTextArea areaCadastroRapido;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaEletroposto(EletropostoController eletropostoController,
                           CidadeController cidadeController,
                           IAPlannerService iaPlannerService) {

        this.eletropostoController = eletropostoController;
        this.cidadeController = cidadeController;
        this.iaPlannerService = iaPlannerService;

        configurarJanela();
        montarTela();
        atualizarTabela();
    }

    private void configurarJanela() {

        setTitle("Gerenciamento de Eletropostos");
        setSize(1000, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void montarTela() {

        JPanel painelFormulario =
                new JPanel(new GridLayout(8, 2, 10, 5));

        campoId = new JTextField();
        campoNome = new JTextField();
        campoLocalizacao = new JTextField();
        campoCidadeId = new JTextField();
        campoConectores = new JTextField();
        campoPotencia = new JTextField();
        campoPreco = new JTextField();
        campoVagas = new JTextField();

        painelFormulario.add(new JLabel("ID:"));
        painelFormulario.add(campoId);

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("Localização:"));
        painelFormulario.add(campoLocalizacao);

        painelFormulario.add(new JLabel("ID da Cidade:"));
        painelFormulario.add(campoCidadeId);

        painelFormulario.add(new JLabel("Conectores Disponíveis:"));
        painelFormulario.add(campoConectores);

        painelFormulario.add(new JLabel("Potência kW:"));
        painelFormulario.add(campoPotencia);

        painelFormulario.add(new JLabel("Preço por kWh:"));
        painelFormulario.add(campoPreco);

        painelFormulario.add(new JLabel("Vagas Disponíveis:"));
        painelFormulario.add(campoVagas);

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
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Localização");
        modeloTabela.addColumn("Cidade ID");
        modeloTabela.addColumn("Cidade");
        modeloTabela.addColumn("Conectores");
        modeloTabela.addColumn("Potência");
        modeloTabela.addColumn("Preço");
        modeloTabela.addColumn("Vagas");

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

        botaoCadastrar.addActionListener(e -> cadastrarEletroposto());

        botaoAtualizar.addActionListener(e -> atualizarEletroposto());

        botaoExcluir.addActionListener(e -> excluirEletroposto());

        botaoLimpar.addActionListener(e -> limparCampos());

        botaoVoltar.addActionListener(e -> dispose());

        botaoPreencherIA.addActionListener(e -> preencherComIA());

        tabela.getSelectionModel().addListSelectionListener(
                e -> preencherCampos()
        );

        TemaRosa.aplicar(getContentPane());
        TemaRosa.aplicarTabela(tabela);
    }

    private void cadastrarEletroposto() {

        try {

            int id =
                    converterInt(campoId.getText());

            if (eletropostoController.buscarPorId(id) != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Já existe um eletroposto com esse ID."
                );

                return;
            }

            Eletroposto eletroposto =
                    criarEletropostoPelosCampos(id);

            eletropostoController.cadastrar(eletroposto);

            JOptionPane.showMessageDialog(
                    this,
                    "Eletroposto cadastrado com sucesso."
            );

            limparCampos();
            atualizarTabela();

        } catch (NumberFormatException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha os campos numéricos corretamente."

            );

        } catch (IllegalArgumentException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage()
            );
        }
    }

    private void atualizarEletroposto() {

        try {

            int id =
                    converterInt(campoId.getText());

            if (eletropostoController.buscarPorId(id) == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Eletroposto não encontrado."
                );

                return;
            }

            Eletroposto eletroposto =
                    criarEletropostoPelosCampos(id);

            eletropostoController.atualizar(id, eletroposto);

            JOptionPane.showMessageDialog(
                    this,
                    "Eletroposto atualizado com sucesso."
            );

            limparCampos();
            atualizarTabela();

        } catch (NumberFormatException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha os campos numéricos corretamente."

            );

        } catch (IllegalArgumentException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage()
            );
        }
    }

    private void excluirEletroposto() {

        try {

            int id =
                    converterInt(campoId.getText());

            int confirmacao =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Deseja realmente excluir este eletroposto?",
                            "Confirmar exclusão",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirmacao == JOptionPane.YES_OPTION) {

                boolean excluiu =
                        eletropostoController.excluir(id);

                if (excluiu) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Eletroposto excluído com sucesso."
                    );

                    limparCampos();
                    atualizarTabela();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Eletroposto não encontrado."
                    );
                }
            }

        } catch (NumberFormatException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe um ID válido."
            );
        }
    }

    private Eletroposto criarEletropostoPelosCampos(int id) {

        String nome =
                campoNome.getText().trim();

        String localizacao =
                campoLocalizacao.getText().trim();

        int cidadeId =
                converterInt(campoCidadeId.getText());

        String conectores =
                campoConectores.getText().trim();

        double potencia =
                converterDouble(campoPotencia.getText());

        double preco =
                converterDouble(campoPreco.getText());

        int vagas =
                converterInt(campoVagas.getText());

        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do eletroposto.");
        }

        if (localizacao.isEmpty()) {
            throw new IllegalArgumentException("Informe a localização.");
        }

        if (conectores.isEmpty()) {
            throw new IllegalArgumentException("Informe os conectores disponíveis.");
        }

        if (cidadeController.buscarPorId(cidadeId) == null) {
            throw new IllegalArgumentException("Não existe cidade cadastrada com esse ID.");
        }

        return new Eletroposto(
                id,
                nome,
                localizacao,
                cidadeId,
                conectores,
                potencia,
                preco,
                vagas
        );
    }

    private void preencherComIA() {

        try {

            String texto =
                    areaCadastroRapido.getText().trim();

            if (texto.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Digite uma descrição para o cadastro rápido por IA."
                );

                return;
            }

            String textoComContexto =
                    texto +
                            "\n\nCidades cadastradas:\n" +
                            listarCidadesParaIA();

            String resposta =
                    iaPlannerService.interpretarCadastroRapido("Eletroposto", textoComContexto);

            String id =
                    valorCampo(resposta, "id");

            if (id.isEmpty()) {
                id = gerarProximoId();
            }

            preencherCampo(campoId, id);
            preencherCampo(campoNome, valorCampo(resposta, "nome"));
            preencherCampo(campoLocalizacao, valorCampo(resposta, "localizacao"));
            preencherCampo(campoCidadeId, valorCampo(resposta, "cidadeId"));
            preencherCampo(campoConectores, valorCampo(resposta, "conectores"));
            preencherCampo(campoPotencia, valorCampo(resposta, "potencia"));
            preencherCampo(campoPreco, valorCampo(resposta, "preco"));
            preencherCampo(campoVagas, valorCampo(resposta, "vagas"));

            JOptionPane.showMessageDialog(
                    this,
                    "Campos preenchidos pela IA. Revise antes de salvar."
            );

        } catch (RuntimeException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage()
            );
        }
    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        ArrayList<Eletroposto> eletropostos =
                eletropostoController.listar();

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
                nomeCidade = "Não encontrada";
            }

            modeloTabela.addRow(
                    new Object[]{
                            eletroposto.getId(),
                            eletroposto.getNome(),
                            eletroposto.getLocalizacao(),
                            eletroposto.getCidadeId(),
                            nomeCidade,
                            eletroposto.getTiposConectoresDisponiveis(),
                            eletroposto.getPotenciaCargaKw(),
                            eletroposto.getPrecoPorKwh(),
                            eletroposto.getVagasDisponiveis()
                    }
            );
        }
    }

    private void preencherCampos() {

        int linhaSelecionada =
                tabela.getSelectedRow();

        if (linhaSelecionada < 0) {
            return;
        }

        campoId.setText(
                modeloTabela.getValueAt(linhaSelecionada, 0).toString()
        );

        campoNome.setText(
                modeloTabela.getValueAt(linhaSelecionada, 1).toString()
        );

        campoLocalizacao.setText(
                modeloTabela.getValueAt(linhaSelecionada, 2).toString()
        );

        campoCidadeId.setText(
                modeloTabela.getValueAt(linhaSelecionada, 3).toString()
        );

        campoConectores.setText(
                modeloTabela.getValueAt(linhaSelecionada, 5).toString()
        );

        campoPotencia.setText(
                modeloTabela.getValueAt(linhaSelecionada, 6).toString()
        );

        campoPreco.setText(
                modeloTabela.getValueAt(linhaSelecionada, 7).toString()
        );

        campoVagas.setText(
                modeloTabela.getValueAt(linhaSelecionada, 8).toString()
        );
    }

    private void limparCampos() {

        campoId.setText("");
        campoNome.setText("");
        campoLocalizacao.setText("");
        campoCidadeId.setText("");
        campoConectores.setText("");
        campoPotencia.setText("");
        campoPreco.setText("");
        campoVagas.setText("");
        areaCadastroRapido.setText("");

        tabela.clearSelection();
    }

    private String listarCidadesParaIA() {

        ArrayList<Cidade> cidades =
                cidadeController.listar();

        StringBuilder texto =
                new StringBuilder();

        for (int i = 0; i < cidades.size(); i++) {

            Cidade cidade =
                    cidades.get(i);

            texto.append("id=")
                    .append(cidade.getId())
                    .append(", nome=")
                    .append(cidade.getNome())
                    .append(", estado=")
                    .append(cidade.getEstado())
                    .append("\n");
        }

        return texto.toString();
    }

    private String valorCampo(String resposta, String chave) {

        String[] linhas =
                resposta.split("\\R");

        for (int i = 0; i < linhas.length; i++) {

            String linha =
                    linhas[i].trim();

            String prefixo =
                    chave + "=";

            if (linha.toLowerCase().startsWith(prefixo.toLowerCase())) {
                return linha.substring(prefixo.length()).trim();
            }
        }

        return "";
    }

    private void preencherCampo(JTextField campo, String valor) {

        if (valor != null && !valor.trim().isEmpty()) {
            campo.setText(valor);
        }
    }

    private String gerarProximoId() {

        ArrayList<Eletroposto> eletropostos =
                eletropostoController.listar();

        int maior =
                0;

        for (int i = 0; i < eletropostos.size(); i++) {

            if (eletropostos.get(i).getId() > maior) {
                maior = eletropostos.get(i).getId();
            }
        }

        return String.valueOf(maior + 1);
    }

    private double converterDouble(String texto) {

        return Double.parseDouble(
                texto.trim().replace(",", ".")
        );
    }

    private int converterInt(String texto) {

        return Integer.parseInt(
                texto.trim()
        );
    }
}
