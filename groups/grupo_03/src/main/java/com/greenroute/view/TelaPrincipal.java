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
import com.greenroute.controller.CidadeController;
import com.greenroute.controller.EletropostoController;

import com.greenroute.view.cidade.TelaCidade;
import com.greenroute.view.eletroposto.TelaEletroposto;

public class TelaPrincipal extends JFrame {

    private VeiculoController controller = new VeiculoController();
    private CidadeController cidadeController = new CidadeController();
    private EletropostoController eletropostoController = new EletropostoController();

    private JButton btnVeiculos;
    private JButton btnAutonomia;
    private JButton btnCidades;
    private JButton btnEletropostos;
    private JButton btnPlanejamento;
    private JButton btnSair;

    public TelaPrincipal() {

        setTitle("GreenRoute");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("GREEN ROUTE", JLabel.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        add(titulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();

        painelBotoes.setLayout(new GridLayout(6, 1, 10, 10));

        painelBotoes.setBorder(
                BorderFactory.createEmptyBorder(10, 60, 10, 60));

        btnVeiculos = new JButton("Gerenciar Veículos");

        btnVeiculos.addActionListener(e -> {
            new TelaVeiculos(controller, this).setVisible(true);
            setVisible(false);
        });

        btnAutonomia = new JButton("Calcular Autonomia");

        btnAutonomia.addActionListener(e -> {
            new TelaCalcularAutonomia(controller, this).setVisible(true);
        });

        btnCidades = new JButton("Gerenciar Cidades");

        btnCidades.addActionListener(e -> {
            new TelaCidade(cidadeController, this).setVisible(true);
            setVisible(false);
        });

        btnEletropostos = new JButton("Gerenciar Eletropostos");

        btnEletropostos.addActionListener(e -> {
            new TelaEletroposto(eletropostoController, this).setVisible(true);
            setVisible(false);
        });

        btnPlanejamento = new JButton("Planejamento de Viagem");

        btnPlanejamento.addActionListener(e -> {
            // A TelaPrincipal permanece aberta em segundo plano;
            // a tela de planejamento abre por cima, como uma sobreposição.
            new TelaPlanejamentoViagem(controller, cidadeController, this).setVisible(true);
        });

        btnSair = new JButton("Sair");

        painelBotoes.add(btnVeiculos);
        painelBotoes.add(btnAutonomia);
        painelBotoes.add(btnCidades);
        painelBotoes.add(btnEletropostos);
        painelBotoes.add(btnPlanejamento);
        painelBotoes.add(btnSair);

        add(painelBotoes, BorderLayout.CENTER);

        btnSair.addActionListener(e -> System.exit(0));

        setVisible(true);

    }

}