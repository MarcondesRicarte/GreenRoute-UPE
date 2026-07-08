package com.greenroute.view.eletroposto;

import com.greenroute.controller.EletropostoController;
import com.greenroute.model.Eletroposto;

import javax.swing.*;
import java.awt.*;

public class TelaBuscarEletroposto extends JFrame {

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
    private JButton btnFechar;



    public TelaBuscarEletroposto(EletropostoController eletropostoController) {

        this.eletropostoController = eletropostoController;

        setTitle("Buscar Eletroposto");

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

        JPanel painelResultado = new JPanel();

        painelResultado.setLayout(new GridLayout(7, 2, 10, 10));

        painelResultado.setBorder(
                BorderFactory.createTitledBorder("Dados do Eletroposto"));



        painelResultado.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        txtNome.setEnabled(false);
        painelResultado.add(txtNome);

        painelResultado.add(new JLabel("Localização:"));
        txtLocalizacao = new JTextField();
        txtLocalizacao.setEnabled(false);
        painelResultado.add(txtLocalizacao);

        painelResultado.add(new JLabel("ID da Cidade:"));
        txtCidadeId = new JTextField();
        txtCidadeId.setEnabled(false);
        painelResultado.add(txtCidadeId);

        painelResultado.add(new JLabel("Conectores Disponíveis:"));
        txtConectores = new JTextField();
        txtConectores.setEnabled(false);
        painelResultado.add(txtConectores);

        painelResultado.add(new JLabel("Potência de Carga (kW):"));
        txtPotencia = new JTextField();
        txtPotencia.setEnabled(false);
        painelResultado.add(txtPotencia);

        painelResultado.add(new JLabel("Preço por kWh (R$):"));
        txtPreco = new JTextField();
        txtPreco.setEnabled(false);
        painelResultado.add(txtPreco);

        painelResultado.add(new JLabel("Vagas Disponíveis:"));
        txtVagas = new JTextField();
        txtVagas.setEnabled(false);
        painelResultado.add(txtVagas);



        add(painelResultado, BorderLayout.CENTER);

        btnFechar = new JButton("Fechar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnFechar);

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

                    txtNome.setText("");
                    txtLocalizacao.setText("");
                    txtCidadeId.setText("");
                    txtConectores.setText("");
                    txtPotencia.setText("");
                    txtPreco.setText("");
                    txtVagas.setText("");

                    return;
                }


                txtNome.setText(eletroposto.getNome());
                txtLocalizacao.setText(eletroposto.getLocalizacao());
                txtCidadeId.setText(String.valueOf(eletroposto.getCidadeId()));
                txtConectores.setText(eletroposto.getTiposConectoresDisponiveis());
                txtPotencia.setText(String.valueOf(eletroposto.getPotenciaCargaKw()));
                txtPreco.setText(String.valueOf(eletroposto.getPrecoPorKwh()));
                txtVagas.setText(String.valueOf(eletroposto.getVagasDisponiveis()));

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "ID inválido!"
                );
            }

        });

        btnFechar.addActionListener(e -> dispose());

    }


}
