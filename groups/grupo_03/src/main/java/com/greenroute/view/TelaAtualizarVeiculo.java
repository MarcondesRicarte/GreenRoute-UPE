package com.greenroute.view;

import java.awt.GridLayout;

import javax.swing.*;

import com.greenroute.controller.VeiculoController;
import com.greenroute.model.Veiculo;

public class TelaAtualizarVeiculo extends JFrame {

    private VeiculoController controller;
    private JFrame telaAnterior;

    private JTextField txtId;
    private JTextField txtModelo;
    private JTextField txtAutonomia;
    private JTextField txtBateria;
    private JTextField txtConsumo;
    private JTextField txtRecarga;

    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnVoltar;

    private Veiculo veiculoAtual;

    public TelaAtualizarVeiculo(VeiculoController controller, JFrame telaAnterior) {

        this.controller = controller;
        this.telaAnterior = telaAnterior;

        setTitle("Atualizar Veículo");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridLayout(8,2,5,5));

        painel.add(new JLabel("ID"));
        txtId = new JTextField();
        painel.add(txtId);

        btnBuscar = new JButton("Buscar");
        painel.add(btnBuscar);
        painel.add(new JLabel());

        painel.add(new JLabel("Modelo"));
        txtModelo = new JTextField();
        painel.add(txtModelo);

        painel.add(new JLabel("Autonomia"));
        txtAutonomia = new JTextField();
        painel.add(txtAutonomia);

        painel.add(new JLabel("Bateria"));
        txtBateria = new JTextField();
        painel.add(txtBateria);

        painel.add(new JLabel("Consumo"));
        txtConsumo = new JTextField();
        painel.add(txtConsumo);

        painel.add(new JLabel("Tempo de Recarga"));
        txtRecarga = new JTextField();
        painel.add(txtRecarga);

        btnAtualizar = new JButton("Atualizar");
        painel.add(btnAtualizar);

        btnVoltar = new JButton("Voltar");
        painel.add(btnVoltar);

        add(painel);

        btnBuscar.addActionListener(e -> buscar());

        btnAtualizar.addActionListener(e -> atualizar());

        btnVoltar.addActionListener(e -> {
            dispose();
            if (this.telaAnterior != null) {
                this.telaAnterior.setVisible(true);
            }
        });
    }

    private void buscar() {

        try {

            int id = Integer.parseInt(txtId.getText());

            veiculoAtual = controller.procurarId(id);

            if(veiculoAtual == null) {

                JOptionPane.showMessageDialog(this,
                        "Veículo não encontrado.");

                return;
            }

            txtModelo.setText(veiculoAtual.getModelo());
            txtAutonomia.setText(String.valueOf(veiculoAtual.getAutonomiaMaxima()));
            txtBateria.setText(String.valueOf(veiculoAtual.getCargaBateriaAtual()));
            txtConsumo.setText(String.valueOf(veiculoAtual.getConsumoKwhPorKm()));
            txtRecarga.setText(String.valueOf(veiculoAtual.getTempoRecargaCompleta()));

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "ID inválido.");

        }

    }

    private void atualizar() {

        if(veiculoAtual == null) {

            JOptionPane.showMessageDialog(this,
                    "Busque um veículo primeiro.");

            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());

            veiculoAtual.setModelo(txtModelo.getText());
            veiculoAtual.setAutonomiaMaxima(Double.parseDouble(txtAutonomia.getText()));
            veiculoAtual.setCargaBateriaAtual(Double.parseDouble(txtBateria.getText()));
            veiculoAtual.setConsumoKwhPorKm(Double.parseDouble(txtConsumo.getText()));
            veiculoAtual.setTempoRecargaCompleta(Integer.parseInt(txtRecarga.getText()));

            controller.atualizarVeiculo(id, veiculoAtual);

            JOptionPane.showMessageDialog(this,
                    "Veículo atualizado com sucesso!");

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Preencha os campos corretamente.");
        }
    }
}