package com.greenroute.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

import com.greenroute.controller.VeiculoController;
import com.greenroute.model.Veiculo;
import com.greenroute.model.VeiculoEletrico;
import com.greenroute.model.VeiculoHibrido;

public class TelaCalcularAutonomia extends JFrame {

    private VeiculoController controller;
    private JFrame telaAnterior;

    private JTextField txtId;
    private JButton btnCalcular;
    private JButton btnVoltar;

    private JTextArea areaResultado;

    public TelaCalcularAutonomia(VeiculoController controller, JFrame telaAnterior) {

        this.controller = controller;
        this.telaAnterior = telaAnterior;

        setTitle("Calcular Autonomia");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelSuperior = new JPanel(new GridLayout(2, 2, 5, 5));

        painelSuperior.add(new JLabel("ID do veículo:"));

        txtId = new JTextField();
        painelSuperior.add(txtId);

        btnCalcular = new JButton("Calcular");
        btnCalcular.addActionListener(e -> calcularAutonomia());

        painelSuperior.add(btnCalcular);

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

    private void calcularAutonomia() {

        try {

            int id = Integer.parseInt(txtId.getText());

            Veiculo v = controller.procurarId(id);

            if (v == null) {

                areaResultado.setText("Veículo não encontrado.");

                return;
            }

            String tipo = (v instanceof VeiculoEletrico) ? "Elétrico" : "Híbrido";

            StringBuilder resultado = new StringBuilder();

            resultado.append("ID: ").append(v.getId()).append("\n");
            resultado.append("Modelo: ").append(v.getModelo()).append("\n");
            resultado.append("Tipo: ").append(tipo).append("\n");
            resultado.append("Carga da Bateria: ").append(v.getCargaBateriaAtual()).append("%\n");

            if (v instanceof VeiculoHibrido) {

                VeiculoHibrido hibrido = (VeiculoHibrido) v;

                resultado.append("Combustível Atual: ")
                        .append(hibrido.getCombustivelAtual())
                        .append(" L (")
                        .append(hibrido.getTipoCombustivel())
                        .append(")\n");
            }

            resultado.append("\nAutonomia total estimada: ")
                    .append(String.format("%.2f", v.calcularAutonomia()))
                    .append(" km");

            areaResultado.setText(resultado.toString());

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Digite um ID válido!");

        }

    }

}
