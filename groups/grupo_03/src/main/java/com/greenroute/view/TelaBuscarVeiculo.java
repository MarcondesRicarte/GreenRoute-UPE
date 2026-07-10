package com.greenroute.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

import com.greenroute.controller.VeiculoController;
import com.greenroute.model.Veiculo;

public class TelaBuscarVeiculo extends JFrame {

    private VeiculoController controller;
    private JFrame telaAnterior;

    private JTextField txtId;
    private JButton btnBuscar;
    private JButton btnVoltar;

    private JTextArea areaResultado;

    public TelaBuscarVeiculo(VeiculoController controller, JFrame telaAnterior) {

        this.controller = controller;
        this.telaAnterior = telaAnterior;

        setTitle("Buscar Veículo");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelSuperior = new JPanel(new GridLayout(2,2,5,5));

        painelSuperior.add(new JLabel("ID do veículo:"));

        txtId = new JTextField();
        painelSuperior.add(txtId);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarVeiculo());

        painelSuperior.add(btnBuscar);

        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            dispose();
            if (this.telaAnterior != null) {
                this.telaAnterior.setVisible(true);
            }
        });

        painelSuperior.add(btnVoltar);

        add(painelSuperior, BorderLayout.NORTH);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);

        add(new JScrollPane(areaResultado), BorderLayout.CENTER);
    }

    private void buscarVeiculo() {

        try {

            int id = Integer.parseInt(txtId.getText());

            Veiculo v = controller.procurarId(id);

            if(v == null) {

                areaResultado.setText("Veículo não encontrado.");

            }else {

                areaResultado.setText(
                        "ID: " + v.getId() +
                                "\nModelo: " + v.getModelo() +
                                "\nAutonomia: " + v.getAutonomiaMaxima() +
                                "\nBateria: " + v.getCargaBateriaAtual() +
                                "\nConsumo: " + v.getConsumoKwhPorKm() +
                                "\nTempo de Recarga: " + v.getTempoRecargaCompleta()
                );

            }

        }catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Digite um ID válido!");

        }

    }

}