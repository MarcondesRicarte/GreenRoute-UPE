package com.greenroute.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

import com.greenroute.controller.VeiculoController;
import com.greenroute.controller.CidadeController;
import com.greenroute.gemini.PlanejadorRotaIA;
import com.greenroute.model.Cidade;
import com.greenroute.model.Veiculo;

public class TelaPlanejamentoViagem extends JFrame {

    private VeiculoController veiculoController;
    private CidadeController cidadeController;
    private PlanejadorRotaIA planejadorRotaIA;
    private JFrame telaAnterior;

    private JTextField txtIdVeiculo;
    private JTextField txtIdCidade;
    private JButton btnPlanejar;
    private JButton btnVoltar;

    private JTextArea areaResultado;

    public TelaPlanejamentoViagem(VeiculoController veiculoController,
                                  CidadeController cidadeController,
                                  JFrame telaAnterior) {

        this.veiculoController = veiculoController;
        this.cidadeController = cidadeController;
        this.telaAnterior = telaAnterior;
        this.planejadorRotaIA = new PlanejadorRotaIA();

        setTitle("Planejamento de Viagem");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelSuperior = new JPanel(new GridLayout(3, 2, 5, 5));

        painelSuperior.add(new JLabel("ID do veículo:"));

        txtIdVeiculo = new JTextField();
        painelSuperior.add(txtIdVeiculo);

        painelSuperior.add(new JLabel("ID da cidade de destino:"));

        txtIdCidade = new JTextField();
        painelSuperior.add(txtIdCidade);

        btnPlanejar = new JButton("Planejar Rota");
        btnPlanejar.addActionListener(e -> planejarRota());

        painelSuperior.add(btnPlanejar);

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
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);

        add(new JScrollPane(areaResultado), BorderLayout.CENTER);
    }

    private void planejarRota() {

        int idVeiculo;
        int idCidade;

        try {

            idVeiculo = Integer.parseInt(txtIdVeiculo.getText().trim());
            idCidade = Integer.parseInt(txtIdCidade.getText().trim());

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this, "Digite IDs válidos!");
            return;
        }

        Veiculo veiculo = veiculoController.procurarId(idVeiculo);
        Cidade cidade = cidadeController.procurarId(idCidade);

        if (veiculo == null) {
            JOptionPane.showMessageDialog(this, "Veículo não encontrado.");
            return;
        }

        if (cidade == null) {
            JOptionPane.showMessageDialog(this, "Cidade não encontrada.");
            return;
        }

        // Desabilita o botão e avisa o usuário enquanto a IA responde,
        // sem travar a interface (requisito do edital)
        btnPlanejar.setEnabled(false);
        areaResultado.setText("Consultando a IA, aguarde...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() {
                return planejadorRotaIA.planejarRota(veiculo, cidade);
            }

            @Override
            protected void done() {

                btnPlanejar.setEnabled(true);

                try {

                    String resultado = get();
                    areaResultado.setText(resultado);

                } catch (Exception ex) {

                    Throwable causa = ex.getCause() != null ? ex.getCause() : ex;

                    areaResultado.setText("");

                    JOptionPane.showMessageDialog(
                            TelaPlanejamentoViagem.this,
                            "Erro ao planejar a rota: " + causa.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }
}