package com.greenroute.view.eletroposto;

import com.greenroute.controller.EletropostoController;

import javax.swing.*;
import java.awt.*;

public class TelaEletroposto extends JFrame {

    private EletropostoController eletropostoController;
    private JFrame telaPrincipal;
    private JButton btnCadastrar;
    private JButton btnBuscar;
    private JButton btnAtualizar;


    private JButton btnRemover;
    private JButton btnListar;
    private JButton btnVoltar;



    public TelaEletroposto(EletropostoController eletropostoController, JFrame telaPrincipal) {

        this.eletropostoController = eletropostoController;
        this.telaPrincipal = telaPrincipal;

        setTitle("GreenRoute - Gerenciamento de Eletropostos");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "GERENCIAMENTO DE ELETROPOSTOS", SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        add(titulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();

        painelBotoes.setLayout(new GridLayout(6, 1, 10, 10));

        painelBotoes.setBorder(
                BorderFactory.createEmptyBorder(10, 60, 10, 60));

        btnCadastrar = new JButton("Cadastrar Eletroposto");
        btnBuscar = new JButton("Buscar Eletroposto");
        btnAtualizar = new JButton("Atualizar Eletroposto");


        btnRemover = new JButton("Remover Eletroposto");
        btnListar = new JButton("Listar Eletropostos");
        btnVoltar = new JButton("Voltar");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.CENTER);

        btnCadastrar.addActionListener(e ->
                new TelaCadastrarEletroposto(eletropostoController).setVisible(true));



        btnBuscar.addActionListener(e ->
                new TelaBuscarEletroposto(eletropostoController).setVisible(true));

        btnAtualizar.addActionListener(e ->
                new TelaAtualizarEletroposto(eletropostoController).setVisible(true));

        btnRemover.addActionListener(e ->
                new TelaRemoverEletroposto(eletropostoController).setVisible(true));

        btnListar.addActionListener(e ->
                new TelaListarEletroposto(eletropostoController).setVisible(true));

        btnVoltar.addActionListener(e -> {
            dispose();
            if (telaPrincipal != null) {
                telaPrincipal.setVisible(true);
            }
        });


    }

}