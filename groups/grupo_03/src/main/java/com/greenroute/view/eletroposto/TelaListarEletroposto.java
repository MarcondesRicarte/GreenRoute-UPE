package com.greenroute.view.eletroposto;

import com.greenroute.controller.EletropostoController;
import com.greenroute.model.Eletroposto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaListarEletroposto extends JFrame {

    private EletropostoController eletropostoController;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JButton btnAtualizarLista;
    private JButton btnFechar;

    public TelaListarEletroposto(EletropostoController eletropostoController) {

        this.eletropostoController = eletropostoController;

        setTitle("Eletropostos Cadastrados");

        setSize(700, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel();

        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Localização");
        modeloTabela.addColumn("Cidade ID");
        modeloTabela.addColumn("Conectores");
        modeloTabela.addColumn("Potência (kW)");
        modeloTabela.addColumn("Preço/kWh");
        modeloTabela.addColumn("Vagas");


        tabela = new JTable(modeloTabela);

        JScrollPane scroll = new JScrollPane(tabela);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Eletropostos Cadastrados"));

        add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();

        btnAtualizarLista = new JButton("Atualizar Lista");
        btnFechar = new JButton("Fechar");

        painelBotoes.add(btnAtualizarLista);
        painelBotoes.add(btnFechar);

        add(painelBotoes, BorderLayout.SOUTH);


        btnAtualizarLista.addActionListener(e -> atualizarTabela());

        btnFechar.addActionListener(e -> dispose());

        atualizarTabela();

    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        for (Eletroposto eletroposto : eletropostoController.getEletropostos()) {


            modeloTabela.addRow(new Object[]{

                    eletroposto.getId(),
                    eletroposto.getNome(),
                    eletroposto.getLocalizacao(),
                    eletroposto.getCidadeId(),
                    eletroposto.getTiposConectoresDisponiveis(),
                    eletroposto.getPotenciaCargaKw(),
                    eletroposto.getPrecoPorKwh(),
                    eletroposto.getVagasDisponiveis()

            });


        }

    }

}