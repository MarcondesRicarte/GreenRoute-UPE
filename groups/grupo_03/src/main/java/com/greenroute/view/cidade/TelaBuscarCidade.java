package com.greenroute.view.cidade;

import com.greenroute.controller.CidadeController;
import com.greenroute.model.Cidade;

import javax.swing.*;
import java.awt.*;

public class TelaBuscarCidade extends JFrame {

    private CidadeController cidadeController;

    private JTextField txtId;

    private JTextField txtNome;
    private JTextField txtEstado;
    private JTextField txtDistancia;

    private JButton btnBuscar;
    private JButton btnFechar;

    public TelaBuscarCidade(CidadeController cidadeController) {

        this.cidadeController = cidadeController;

        setTitle("Buscar Cidade");

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

        painelResultado.setLayout(new GridLayout(3, 2, 10, 10));

        painelResultado.setBorder(
                BorderFactory.createTitledBorder("Dados da Cidade"));

        painelResultado.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        txtNome.setEnabled(false);
        painelResultado.add(txtNome);

        painelResultado.add(new JLabel("Estado:"));
        txtEstado = new JTextField();
        txtEstado.setEnabled(false);
        painelResultado.add(txtEstado);

        painelResultado.add(new JLabel("Distância da Capital (km):"));
        txtDistancia = new JTextField();
        txtDistancia.setEnabled(false);
        painelResultado.add(txtDistancia);

        add(painelResultado, BorderLayout.CENTER);

        btnFechar = new JButton("Fechar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnFechar);

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

                    txtNome.setText("");
                    txtEstado.setText("");
                    txtDistancia.setText("");

                    return;
                }

                txtNome.setText(cidade.getNome());
                txtEstado.setText(cidade.getEstado());
                txtDistancia.setText(
                        String.valueOf(cidade.getDistanciaDaCapital())
                );

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
