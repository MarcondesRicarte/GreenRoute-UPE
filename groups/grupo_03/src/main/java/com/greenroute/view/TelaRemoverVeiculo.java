package com.greenroute.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

import com.greenroute.controller.VeiculoController;

public class TelaRemoverVeiculo extends JFrame {

    private VeiculoController controller;
    private JFrame telaAnterior;

    private JTextField txtId;
    private JButton btnRemover;
    private JButton btnVoltar;

    private JTextArea areaResultado;

    public TelaRemoverVeiculo(VeiculoController controller, JFrame telaAnterior) {

        this.controller = controller;
        this.telaAnterior = telaAnterior;

        setTitle("Remover Veículo");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridLayout(2,2,5,5));

        painel.add(new JLabel("ID do veículo:"));

        txtId = new JTextField();
        painel.add(txtId);

        btnRemover = new JButton("Remover");
        painel.add(btnRemover);

        btnVoltar = new JButton("Voltar");
        painel.add(btnVoltar);

        add(painel, BorderLayout.NORTH);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);

        add(new JScrollPane(areaResultado), BorderLayout.CENTER);

        btnRemover.addActionListener(e -> removerVeiculo());

        btnVoltar.addActionListener(e -> {
            dispose();
            if (this.telaAnterior != null) {
                this.telaAnterior.setVisible(true);
            }
        });
    }

    private void removerVeiculo() {

        try {

            int id = Integer.parseInt(txtId.getText());

            boolean removido = controller.remover(id);

            if(removido) {

                areaResultado.setText("Veículo removido com sucesso!");

            } else {

                areaResultado.setText("Nenhum veículo encontrado com esse ID.");

            }

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Digite um ID válido!");

        }

    }

}