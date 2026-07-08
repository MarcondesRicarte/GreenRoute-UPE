package com.greenroute.view.cidade;

import com.greenroute.controller.CidadeController;
import com.greenroute.model.Cidade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaListarCidade extends JFrame {

    private CidadeController cidadeController;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JButton btnAtualizarLista;
    private JButton btnFechar;

    public TelaListarCidade(CidadeController cidadeController) {



        this.cidadeController = cidadeController;

        setTitle("Cidades Cadastradas");

        setSize(700, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Estado");
        modeloTabela.addColumn("Distância");

        tabela = new JTable(modeloTabela);

        JScrollPane scroll = new JScrollPane(tabela);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Cidades Cadastradas"));

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

        for (Cidade cidade : cidadeController.listarCidades()) {

            modeloTabela.addRow(new Object[]{

                    cidade.getId(),
                    cidade.getNome(),
                    cidade.getEstado(),
                    cidade.getDistanciaDaCapital()

            });

        }

    }



}

