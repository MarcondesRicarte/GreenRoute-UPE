package com.greenroute.view.eletroposto;

import com.greenroute.controller.EletropostoController;
import com.greenroute.gemini.EletropostoIA;
import com.greenroute.model.Eletroposto;

import javax.swing.*;
import java.awt.*;

public class TelaCadastrarEletroposto extends JFrame {

    private EletropostoController eletropostoController;
    private EletropostoIA eletropostoIA;
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtLocalizacao;
    private JTextField txtCidadeId;
    private JTextField txtConectores;
    private JTextField txtPotencia;
    private JTextField txtPreco;
    private JTextField txtVagas;
    private JTextArea txtDescricao;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnCadastrarIA;



    public TelaCadastrarEletroposto(EletropostoController eletropostoController) {

        this.eletropostoController = eletropostoController;
        this.eletropostoIA = new EletropostoIA();

        setTitle("Cadastrar Eletroposto");

        setSize(700, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel painelFormulario = new JPanel();

        painelFormulario.setLayout(new GridLayout(8, 2, 10, 10));

        painelFormulario.setBorder(
                BorderFactory.createTitledBorder("Dados do Eletroposto"));

        painelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        painelFormulario.add(txtId);

        painelFormulario.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("Localização:"));
        txtLocalizacao = new JTextField();
        painelFormulario.add(txtLocalizacao);



        painelFormulario.add(new JLabel("ID da Cidade:"));
        txtCidadeId = new JTextField();
        painelFormulario.add(txtCidadeId);

        painelFormulario.add(new JLabel("Conectores Disponíveis:"));
        txtConectores = new JTextField();
        painelFormulario.add(txtConectores);

        painelFormulario.add(new JLabel("Potência de Carga (kW):"));
        txtPotencia = new JTextField();
        painelFormulario.add(txtPotencia);

        painelFormulario.add(new JLabel("Preço por kWh (R$):"));
        txtPreco = new JTextField();
        painelFormulario.add(txtPreco);

        painelFormulario.add(new JLabel("Vagas Disponíveis:"));
        txtVagas = new JTextField();
        painelFormulario.add(txtVagas);

        add(painelFormulario, BorderLayout.NORTH);

        // Painel de descrição para preenchimento por IA

        JPanel painelDescricao = new JPanel(new BorderLayout(5, 5));
        painelDescricao.setBorder(
                BorderFactory.createTitledBorder("Descrição (para Cadastrar por IA)"));


        txtDescricao = new JTextArea(6, 30);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        painelDescricao.add(scrollDescricao, BorderLayout.CENTER);

        add(painelDescricao, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        btnCadastrarIA = new JButton("Cadastrar por IA");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCadastrarIA);
        painelBotoes.add(btnCancelar);

        add(painelBotoes, BorderLayout.SOUTH);

        btnCadastrarIA.addActionListener(e -> {

            String descricao = txtDescricao.getText();

            if (descricao == null || descricao.isBlank()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Digite uma descrição antes de usar o Cadastrar por IA."
                );
                return;
            }


            btnCadastrarIA.setEnabled(false);
            btnCadastrarIA.setText("Gerando...");

            // Chamada à IA feita em background para não travar a interface
            SwingWorker<Eletroposto, Void> worker = new SwingWorker<>() {

                @Override
                protected Eletroposto doInBackground() throws Exception {
                    return eletropostoIA.gerarEletroposto(descricao);
                }

                @Override
                protected void done() {

                    btnCadastrarIA.setEnabled(true);
                    btnCadastrarIA.setText("Cadastrar por IA");

                    try {

                        Eletroposto eletroposto = get();

                        txtId.setText(String.valueOf(eletroposto.getId()));
                        txtNome.setText(eletroposto.getNome());
                        txtLocalizacao.setText(eletroposto.getLocalizacao());
                        txtCidadeId.setText(String.valueOf(eletroposto.getCidadeId()));
                        txtConectores.setText(eletroposto.getTiposConectoresDisponiveis());
                        txtPotencia.setText(String.valueOf(eletroposto.getPotenciaCargaKw()));
                        txtPreco.setText(String.valueOf(eletroposto.getPrecoPorKwh()));
                        txtVagas.setText(String.valueOf(eletroposto.getVagasDisponiveis()));

                    } catch (Exception ex) {

                        String mensagem = ex.getCause() != null
                                ? ex.getCause().getMessage()
                                : ex.getMessage();

                        if (mensagem != null &&
                                (mensagem.contains("429") || mensagem.contains("RESOURCE_EXHAUSTED"))) {

                            mensagem = "Limite diário de uso da IA foi atingido. "
                                    + "Tente novamente mais tarde ou cadastre manualmente.";
                        }

                        JOptionPane.showMessageDialog(
                                TelaCadastrarEletroposto.this,
                                "Erro ao gerar dados por IA: " + mensagem
                        );
                    }
                }
            };

            worker.execute();
        });

        btnSalvar.addActionListener(e -> {

            try {

                int id = Integer.parseInt(txtId.getText());

                if (eletropostoController.buscarEletropostoPorId(id) != null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Já existe um eletroposto com esse ID!"
                    );

                    return;
                }

                String nome = txtNome.getText();
                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o nome do Eletroposto.");
                    return;
                }

                String localizacao = txtLocalizacao.getText();
                if (localizacao.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe a localização do Eletroposto.");
                    return;
                }

                int cidadeId = Integer.parseInt(txtCidadeId.getText());
                String conectores = txtConectores.getText();
                if (conectores.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o(s) tipo(s) de conector(es) do Eletroposto.");
                    return;
                }


                double potencia = Double.parseDouble(txtPotencia.getText());
                double preco = Double.parseDouble(txtPreco.getText());
                int vagas = Integer.parseInt(txtVagas.getText());

                Eletroposto eletroposto = new Eletroposto(
                        id,
                        nome,
                        localizacao,
                        cidadeId,
                        conectores,
                        potencia,
                        preco,
                        vagas
                );

                eletropostoController.cadastrarEletroposto(eletroposto);

                JOptionPane.showMessageDialog(
                        this,
                        "Eletroposto cadastrado com sucesso!"
                );

                dispose();


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao cadastrar eletroposto!"
                );
            }

        });



        btnCancelar.addActionListener(e -> dispose());

    }

}