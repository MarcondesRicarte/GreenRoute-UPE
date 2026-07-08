package com.greenroute.view.cidade;

import com.greenroute.controller.CidadeController;
import com.greenroute.gemini.CidadeIA;
import com.greenroute.model.Cidade;

import javax.swing.*;
import java.awt.*;

public class TelaCadastrarCidade extends JFrame {



    private CidadeController cidadeController;
    private CidadeIA cidadeIA;
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtEstado;
    private JTextField txtDistancia;
    private JTextArea txtDescricao;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnCadastrarIA;


    public TelaCadastrarCidade(CidadeController cidadeController) {



        this.cidadeController = cidadeController;
        this.cidadeIA = new CidadeIA();

        setTitle("Cadastrar Cidade");

        setSize(700, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel painelFormulario = new JPanel();

        painelFormulario.setLayout(new GridLayout(4, 2, 10, 10));

        painelFormulario.setBorder(
                BorderFactory.createTitledBorder("Dados da Cidade"));

        painelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        painelFormulario.add(txtId);
        painelFormulario.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelFormulario.add(txtNome);


        painelFormulario.add(new JLabel("Estado:"));
        txtEstado = new JTextField();
        painelFormulario.add(txtEstado);



        painelFormulario.add(new JLabel("Distância da Capital (km):"));
        txtDistancia = new JTextField();
        painelFormulario.add(txtDistancia);

        add(painelFormulario, BorderLayout.NORTH);

        // Painel de descricão para preenchimento por IA

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

            // Chama a IA em segundo plano, pra tela não travar enquanto espera

            SwingWorker<Cidade, Void> worker = new SwingWorker<>() {


                @Override
                protected Cidade doInBackground() throws Exception {
                    return cidadeIA.gerarCidade(descricao);
                }

                @Override
                protected void done() {

                    btnCadastrarIA.setEnabled(true);
                    btnCadastrarIA.setText("Cadastrar por IA");

                    try {

                        Cidade cidade = get();

                        txtId.setText(String.valueOf(cidade.getId()));
                        txtNome.setText(cidade.getNome());
                        txtEstado.setText(cidade.getEstado());
                        txtDistancia.setText(String.valueOf(cidade.getDistanciaDaCapital()));


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
                                TelaCadastrarCidade.this,
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
                if (cidadeController.buscarCidadePorId(id) != null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Já existe uma cidade com esse ID!"
                    );

                    return;
                }

                String nome = txtNome.getText();
                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o nome da cidade.");
                    return;
                }

                String estado = txtEstado.getText();
                if (estado.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o estado da cidade.");
                    return;
                }

                double distancia =
                        Double.parseDouble(txtDistancia.getText());

                Cidade cidade = new Cidade(
                        id,
                        nome,
                        estado,
                        distancia
                );
                cidadeController.cadastrarCidade(cidade);

                JOptionPane.showMessageDialog(
                        this,
                        "Cidade cadastrada com sucesso!"
                );

                dispose();


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao cadastrar cidade!"
                );
            }

        });


        btnCancelar.addActionListener(e -> dispose());

    }

}