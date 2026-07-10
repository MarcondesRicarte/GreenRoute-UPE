package com.greenroute.view.eletroposto;

import com.greenroute.controller.EletropostoController;
import com.greenroute.model.Eletroposto;

import javax.swing.*;
import java.awt.*;

public class TelaAtualizarEletroposto extends JFrame {

    private EletropostoController eletropostoController;

    private JTextField txtId;
    private JButton btnBuscar;

    private JTextField txtNome;
    private JTextField txtLocalizacao;
    private JTextField txtCidadeId;
    private JTextField txtConectores;
    private JTextField txtPotencia;
    private JTextField txtPreco;
    private JTextField txtVagas;
    private JButton btnAtualizar;
    private JButton btnCancelar;



    public TelaAtualizarEletroposto(EletropostoController eletropostoController) {


        this.eletropostoController = eletropostoController;

        setTitle("Atualizar Eletroposto");

        setSize(700, 500);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelBusca = new JPanel();

        painelBusca.add(new JLabel("ID:"));

        txtId = new JTextField(10);
        painelBusca.add(txtId);

        btnBuscar = new JButton("Buscar");
        painelBusca.add(btnBuscar);

        add(painelBusca, BorderLayout.NORTH);
        JPanel painelFormulario = new JPanel();

        painelFormulario.setLayout(new GridLayout(7, 2, 10, 10));

        painelFormulario.setBorder(
                BorderFactory.createTitledBorder("Novos Dados"));

        painelFormulario.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        txtNome.setEnabled(false);
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("Localização:"));
        txtLocalizacao = new JTextField();
        txtLocalizacao.setEnabled(false);
        painelFormulario.add(txtLocalizacao);


        painelFormulario.add(new JLabel("ID da Cidade:"));
        txtCidadeId = new JTextField();
        txtCidadeId.setEnabled(false);
        painelFormulario.add(txtCidadeId);

        painelFormulario.add(new JLabel("Conectores Disponíveis:"));
        txtConectores = new JTextField();
        txtConectores.setEnabled(false);
        painelFormulario.add(txtConectores);


        painelFormulario.add(new JLabel("Potência de Carga (kW):"));
        txtPotencia = new JTextField();
        txtPotencia.setEnabled(false);
        painelFormulario.add(txtPotencia);

        painelFormulario.add(new JLabel("Preço por kWh (R$):"));
        txtPreco = new JTextField();
        txtPreco.setEnabled(false);
        painelFormulario.add(txtPreco);

        painelFormulario.add(new JLabel("Vagas Disponíveis:"));
        txtVagas = new JTextField();
        txtVagas.setEnabled(false);
        painelFormulario.add(txtVagas);

        add(painelFormulario, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();



        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setEnabled(false);

        btnCancelar = new JButton("Cancelar");

        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnCancelar);

        add(painelBotoes, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {

            try {

                int id = Integer.parseInt(txtId.getText());

                Eletroposto eletroposto =
                        eletropostoController.buscarEletropostoPorId(id);

                if (eletroposto == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Eletroposto não encontrado!"
                    );

                    return;
                }

                txtNome.setText(eletroposto.getNome());
                txtLocalizacao.setText(eletroposto.getLocalizacao());
                txtCidadeId.setText(String.valueOf(eletroposto.getCidadeId()));
                txtConectores.setText(eletroposto.getTiposConectoresDisponiveis());
                txtPotencia.setText(String.valueOf(eletroposto.getPotenciaCargaKw()));
                txtPreco.setText(String.valueOf(eletroposto.getPrecoPorKwh()));
                txtVagas.setText(String.valueOf(eletroposto.getVagasDisponiveis()));
                txtNome.setEnabled(true);
                txtLocalizacao.setEnabled(true);
                txtCidadeId.setEnabled(true);
                txtConectores.setEnabled(true);
                txtPotencia.setEnabled(true);
                txtPreco.setEnabled(true);
                txtVagas.setEnabled(true);

                btnAtualizar.setEnabled(true);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "ID inválido!"
                );
            }

        });

        btnAtualizar.addActionListener(e -> {

            try {


                Eletroposto eletroposto = new Eletroposto(

                        Integer.parseInt(txtId.getText()),
                        txtNome.getText(),
                        txtLocalizacao.getText(),
                        Integer.parseInt(txtCidadeId.getText()),
                        txtConectores.getText(),
                        Double.parseDouble(txtPotencia.getText()),
                        Double.parseDouble(txtPreco.getText()),
                        Integer.parseInt(txtVagas.getText())

                );

                if (eletropostoController.atualizarEletroposto(eletroposto)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Eletroposto atualizado!"
                    );

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Eletroposto não encontrado!"
                    );

                }



            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao atualizar!"
                );

            }

        });

        btnCancelar.addActionListener(e -> dispose());

    }


}