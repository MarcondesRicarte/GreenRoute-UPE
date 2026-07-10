package com.greenroute.view.cidade;

import com.greenroute.controller.CidadeController;

import javax.swing.*;
import java.awt.*;

public class TelaRemoverCidade extends JFrame {

    private CidadeController cidadeController;

    private JTextField txtId;

    private JButton btnRemover;
    private JButton btnFechar;

    public TelaRemoverCidade(CidadeController cidadeController) {

        this.cidadeController = cidadeController;

        setTitle("Remover Cidade");

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

                if (cidadeController.buscarCidadePorId(id) == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Cidade não encontrada!"
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

                    cidadeController.removerCidadePorId(id);

                    JOptionPane.showMessageDialog(
                            this,
                            "Cidade removida."
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
