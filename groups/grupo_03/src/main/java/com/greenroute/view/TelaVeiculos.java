package com.greenroute.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.greenroute.controller.VeiculoController;

public class TelaVeiculos extends JFrame {

    private VeiculoController controller;
    private JFrame telaAnterior;

    private JButton btnCadastrar;
    private JButton btnListar;
    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnRemover;
    private JButton btnVoltarTelaPrincipal;

    public TelaVeiculos(VeiculoController controller, JFrame telaAnterior) {
        this.controller = controller;
        this.telaAnterior = telaAnterior;

        setTitle("Cadastro de Veículo");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("CADASTRO DE VEÍCULO", JLabel.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        add(titulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();

        painelBotoes.setLayout(new GridLayout(6, 1, 10, 10));

        painelBotoes.setBorder(
                BorderFactory.createEmptyBorder(10, 60, 10, 60));

        btnCadastrar = new JButton("Cadastrar Veículos");
        btnCadastrar.addActionListener(e -> {
            new TelaCadastrarVeiculo(controller, this).setVisible(true);
        });

        btnListar = new JButton("Listar Veículos");
        btnListar.addActionListener(e -> {
            new TelaListarVeiculos(controller, this).setVisible(true);
        });

        btnBuscar = new JButton("Buscar Veículo");
        btnBuscar.addActionListener(e -> {
            new TelaBuscarVeiculo(controller, this).setVisible(true);
        });

        btnAtualizar = new JButton("Atualizar Veículo");
        btnAtualizar.addActionListener(e -> {
            new TelaAtualizarVeiculo(controller, this).setVisible(true);
        });

        btnRemover = new JButton("Remover Veículo");
        btnRemover.addActionListener(e -> {
            new TelaRemoverVeiculo(controller, this).setVisible(true);
        });

        btnVoltarTelaPrincipal = new JButton("Voltar ao Menu Principal");
        btnVoltarTelaPrincipal.addActionListener(e -> {
            dispose();
            if (telaAnterior != null) {
                telaAnterior.setVisible(true);
            }
        });

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnVoltarTelaPrincipal);

        add(painelBotoes, BorderLayout.CENTER);

        setVisible(true);
    }
}