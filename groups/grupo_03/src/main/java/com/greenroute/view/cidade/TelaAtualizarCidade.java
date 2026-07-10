package com.greenroute.view.cidade;

import com.greenroute.controller.CidadeController;
import com.greenroute.model.Cidade;

import javax.swing.*;
import java.awt.*;

public class TelaAtualizarCidade extends JFrame {

    private CidadeController cidadeController;

    private JTextField txtId;
    private JButton btnBuscar;

    private JTextField txtNome;
    private JTextField txtEstado;
    private JTextField txtDistancia;

    private JButton btnAtualizar;
    private JButton btnCancelar;

    public TelaAtualizarCidade(CidadeController cidadeController) {

        this.cidadeController = cidadeController;

        setTitle("Atualizar Cidade");

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

        painelFormulario.setLayout(new GridLayout(3, 2, 10, 10));

        painelFormulario.setBorder(
                BorderFactory.createTitledBorder("Novos Dados"));

        painelFormulario.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        txtNome.setEnabled(false);
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("Estado:"));
        txtEstado = new JTextField();
        txtEstado.setEnabled(false);
        painelFormulario.add(txtEstado);

        painelFormulario.add(new JLabel("Distância da Capital (km):"));
        txtDistancia = new JTextField();
        txtDistancia.setEnabled(false);
        painelFormulario.add(txtDistancia);

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

                Cidade cidade =
                        cidadeController.buscarCidadePorId(id);

                if (cidade == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Cidade não encontrada!"
                    );

                    return;
                }

                txtNome.setText(cidade.getNome());
                txtEstado.setText(cidade.getEstado());
                txtDistancia.setText(
                        String.valueOf(cidade.getDistanciaDaCapital())
                );

                txtNome.setEnabled(true);
                txtEstado.setEnabled(true);
                txtDistancia.setEnabled(true);

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

                Cidade cidade = new Cidade(

                        Integer.parseInt(txtId.getText()),
                        txtNome.getText(),
                        txtEstado.getText(),
                        Double.parseDouble(txtDistancia.getText())

                );

                if (cidadeController.atualizarCidade(cidade)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Cidade atualizada!"
                    );

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Cidade não encontrada!"
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