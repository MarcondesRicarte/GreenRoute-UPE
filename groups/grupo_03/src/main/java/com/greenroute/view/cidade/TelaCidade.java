package com.greenroute.view.cidade;

import com.greenroute.controller.CidadeController;

import javax.swing.*;
import java.awt.*;

public class TelaCidade extends JFrame {

    private CidadeController cidadeController;
    private JFrame telaPrincipal;

    private JButton btnCadastrar;
    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnRemover;
    private JButton btnListar;
    private JButton btnVoltar;



    public TelaCidade(CidadeController cidadeController, JFrame telaPrincipal) {

        this.cidadeController = cidadeController;
        this.telaPrincipal = telaPrincipal;

        setTitle("GreenRoute - Gerenciamento de Cidades");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "GERENCIAMENTO DE CIDADES", SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        add(titulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();

        painelBotoes.setLayout(new GridLayout(6, 1, 10, 10));

        painelBotoes.setBorder(
                BorderFactory.createEmptyBorder(10, 60, 10, 60));

        btnCadastrar = new JButton("Cadastrar Cidade");
        btnBuscar = new JButton("Buscar Cidade");
        btnAtualizar = new JButton("Atualizar Cidade");
        btnRemover = new JButton("Remover Cidade");
        btnListar = new JButton("Listar Cidades");
        btnVoltar = new JButton("Voltar");



        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.CENTER);

        btnCadastrar.addActionListener(e ->
                new TelaCadastrarCidade(cidadeController).setVisible(true));

        btnBuscar.addActionListener(e ->
                new TelaBuscarCidade(cidadeController).setVisible(true));

        btnAtualizar.addActionListener(e ->
                new TelaAtualizarCidade(cidadeController).setVisible(true));

        btnRemover.addActionListener(e ->
                new TelaRemoverCidade(cidadeController).setVisible(true));

        btnListar.addActionListener(e ->
                new TelaListarCidade(cidadeController).setVisible(true));

        btnVoltar.addActionListener(e -> {
            dispose();
            if (telaPrincipal != null) {
                telaPrincipal.setVisible(true);
            }
        });

    }

}