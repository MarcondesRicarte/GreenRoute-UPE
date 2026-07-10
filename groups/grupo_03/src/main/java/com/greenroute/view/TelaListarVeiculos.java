package com.greenroute.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.greenroute.controller.VeiculoController;
import com.greenroute.model.Veiculo;
import com.greenroute.model.VeiculoEletrico;

public class TelaListarVeiculos extends JFrame{
    private VeiculoController controller;
    private JFrame telaAnterior;

    private JTable tabelaVeiculos;
    private JScrollPane scrollTabela;
    private JButton btnAtualizarLista;
    private JButton btnVoltar;

    public TelaListarVeiculos(VeiculoController controller, JFrame telaAnterior) {
        this.controller = controller;
        this.telaAnterior = telaAnterior;

        tabelaVeiculos = new JTable();
        scrollTabela = new JScrollPane(tabelaVeiculos);
        scrollTabela.setPreferredSize(new Dimension(600, 300)); // largura x altura


        setTitle("Listar Veículos");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("LISTAR VEÍCULOS", JLabel.CENTER);

        btnAtualizarLista = new JButton("Atualizar");
        btnAtualizarLista.addActionListener(e -> atualizarLista());

        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            dispose();
            if (this.telaAnterior != null) {
                this.telaAnterior.setVisible(true);
            }
        });

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(scrollTabela, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAtualizarLista);
        painelBotoes.add(btnVoltar);
        add(painelBotoes, BorderLayout.SOUTH);
        add(painel);

        carregarTabela();
    }

    private void carregarTabela() {

        String[] colunas = {
                "ID",
                "Modelo",
                "Tipo",
                "Autonomia",
                "Carga"
        };

        DefaultTableModel modeloTabela =
                new DefaultTableModel(colunas, 0);

        for (Veiculo v : controller.listar()) {

            String tipo;

            if (v instanceof VeiculoEletrico) {
                tipo = "Elétrico";
            } else {
                tipo = "Híbrido";
            }

            modeloTabela.addRow(new Object[]{

                    v.getId(),
                    v.getModelo(),
                    tipo,
                    v.getAutonomiaMaxima(),
                    v.getCargaBateriaAtual()

            });

        }

        tabelaVeiculos.setModel(modeloTabela);
    }

    private void atualizarLista() {
        carregarTabela();
    }
}