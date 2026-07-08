package com.greenroute.view.eletroposto;

import com.greenroute.controller.EletropostoController;

import javax.swing.*;
import java.awt.*;


public class TelaRemoverEletroposto extends JFrame {

    private EletropostoController eletropostoController;
    private JTextField txtId;


    private JButton btnRemover;
    private JButton btnFechar;



    public TelaRemoverEletroposto(EletropostoController eletropostoController) {



        this.eletropostoController = eletropostoController;

        setTitle("Remover Eletroposto");

        setSize(700, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel painelCampo = new JPanel();

        painelCampo.add(new JLabel("ID:"));

        txtId = new JTextField(10);
        painelCampo.add(txtId);

        add(painelCampo, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();

        btnRemover = new JButton("Remover");
        btnFechar = new JButton("Fechar");

        painelBotoes.add(btnRemover);
        painelBotoes.add(btnFechar);

        add(painelBotoes, BorderLayout.SOUTH);

        btnRemover.addActionListener(e -> {

            try {

                int id = Integer.parseInt(txtId.getText());

                if (eletropostoController.buscarEletropostoPorId(id) == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Eletroposto não encontrado!"
                    );

                    return;
                }


                int opcao = JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente remover?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcao == JOptionPane.YES_OPTION) {

                    eletropostoController.removerEletropostoPorId(id);

                    JOptionPane.showMessageDialog(
                            this,
                            "Eletroposto removido."
                    );

                    dispose();

                }

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