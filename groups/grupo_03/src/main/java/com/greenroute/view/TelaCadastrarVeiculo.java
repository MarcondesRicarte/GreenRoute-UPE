package com.greenroute.view;

import java.awt.GridLayout;
//import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.greenroute.controller.VeiculoController;
import com.greenroute.gemini.ConexaoGemini;
import com.greenroute.model.VeiculoEletrico;
import com.greenroute.model.VeiculoHibrido;

public class TelaCadastrarVeiculo extends JFrame{

    private VeiculoController controller;
    private JFrame telaAnterior;

    private JLabel lblTitulo;

    private JLabel lblTipo;
    private JComboBox<String> cbTipo;

    private JLabel lblId;
    private JTextField txtId;

    private JLabel lblModelo;
    private JTextField txtModelo;

    private JLabel lblAutonomia;
    private JTextField txtAutonomia;

    private JLabel lblCarga;
    private JTextField txtCarga;

    private JLabel lblConsumo;
    private JTextField txtConsumo;

    private JLabel lblTempoRecarga;
    private JTextField txtTempoRecarga;

    // Elétrico
    private JLabel lblConector;
    private JTextField txtConector;

    private JLabel lblTempoRapida;
    private JTextField txtTempoRapida;

    // Híbrido
    private JLabel lblTanque;
    private JTextField txtTanque;

    private JLabel lblConsumoComb;
    private JTextField txtConsumoComb;

    private JLabel lblTipoComb;
    private JTextField txtTipoComb;

    private JLabel lblCombAtual;
    private JTextField txtCombAtual;

    private JButton btnCadastrar;
    private JButton btnLimpar;
    private JButton btnVoltar;

    private JButton btnCadastroIA;
    private JTextArea txtDescricao;

    public TelaCadastrarVeiculo(VeiculoController controller, JFrame telaAnterior) {
        this.controller = controller;
        this.telaAnterior = telaAnterior;

        setTitle("GreenRoute");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(11, 1, 10, 10));

        lblTitulo = new JLabel("CADASTRO DE VEÍCULO");

        lblTipo = new JLabel("Tipo do veículo:");
        cbTipo = new JComboBox<>();

        cbTipo.addItem("Veículo Elétrico");
        cbTipo.addItem("Veículo Híbrido");

        lblId = new JLabel("ID:");
        txtId = new JTextField(15);

        lblModelo = new JLabel("Modelo:");
        txtModelo = new JTextField(20);

        lblAutonomia = new JLabel("Autonomia Máxima:");
        txtAutonomia = new JTextField(15);

        lblCarga = new JLabel("Carga da Bateria:");
        txtCarga = new JTextField(15);

        lblConsumo = new JLabel("Consumo (kWh/km):");
        txtConsumo = new JTextField(15);

        lblTempoRecarga = new JLabel("Tempo de Recarga:");
        txtTempoRecarga = new JTextField(15);

        // Elétrico

        lblConector = new JLabel("Tipo de Conector:");
        txtConector = new JTextField(15);

        lblTempoRapida = new JLabel("Tempo Recarga Rápida:");
        txtTempoRapida = new JTextField(15);

        // Híbrido

        lblTanque = new JLabel("Capacidade do Tanque:");
        txtTanque = new JTextField(15);

        lblConsumoComb = new JLabel("Consumo Combustível:");
        txtConsumoComb = new JTextField(15);

        lblTipoComb = new JLabel("Tipo de Combustível:");
        txtTipoComb = new JTextField(15);

        lblCombAtual = new JLabel("Combustível Atual:");
        txtCombAtual = new JTextField(15);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> cadastrarVeiculo());

        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());

        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            dispose();
            if (this.telaAnterior != null) {
                this.telaAnterior.setVisible(true);
            }
        });

        painel.add(new JLabel(""));

        painel.add(lblTitulo);


        painel.add(new JLabel(""));
        painel.add(new JLabel(""));

        painel.add(lblTipo);
        painel.add(cbTipo);

        painel.add(lblId);
        painel.add(txtId);

        painel.add(lblModelo);
        painel.add(txtModelo);

        painel.add(lblAutonomia);
        painel.add(txtAutonomia);

        painel.add(lblCarga);
        painel.add(txtCarga);

        painel.add(lblConsumo);
        painel.add(txtConsumo);

        painel.add(lblTempoRecarga);
        painel.add(txtTempoRecarga);

        painel.add(lblConector);
        painel.add(txtConector);

        painel.add(lblTempoRapida);
        painel.add(txtTempoRapida);

        painel.add(lblTanque);
        painel.add(txtTanque);

        painel.add(lblConsumoComb);
        painel.add(txtConsumoComb);

        painel.add(lblTipoComb);
        painel.add(txtTipoComb);

        painel.add(lblCombAtual);
        painel.add(txtCombAtual);

        painel.add(new JLabel(""));
        painel.add(new JLabel(""));

        painel.add(btnCadastrar);
        painel.add(btnLimpar);
        painel.add(btnVoltar);

        btnCadastroIA = new JButton("Inserir dados por IA");
        btnCadastroIA.addActionListener(e -> {
            String descricao = txtDescricao.getText();

            String prompt =
                    """
                    Extraia as informações do veículo abaixo.
    
                    Responda SOMENTE neste formato:
    
                    id=
                    modelo=
                    autonomia=
                    bateria=
                    consumo=
                    tempo=
                    tipo de veículo=
                    tipo de conector=
                    tempo de recarga rápida=
                    
                    E se caso o tipo de veículo for híbrido, acrescente essas informações:
    
                    capacidade do Tanque=
                    consumo do combustível=
                    tipo de combustível= 
                    nível de combustível atual=
    
                    Descrição:
                    """ + descricao;

            String resposta = new ConexaoGemini().perguntar(prompt);

            preencherCampos(resposta);

            System.out.println(resposta);
        });

        painel.add(new JLabel(""));

        painel.add(new JLabel("Cadastro rápido por IA: "));

        txtDescricao = new JTextArea(5, 50);
        painel.add(new JScrollPane(txtDescricao));

        painel.add(btnCadastroIA);

        add(painel);
    }

    private void cadastrarVeiculo() {
        try {
            int id = Integer.parseInt(txtId.getText());

            if (controller.existeId(id)) {

                JOptionPane.showMessageDialog(this,
                        "Já existe um veículo com esse ID.");

                return;
            }

            String modelo = txtModelo.getText();

            if (modelo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o modelo do veículo.");
                return;
            }
            double autonomiaMaxima = Double.parseDouble(txtAutonomia.getText());
            double cargaBateria = Double.parseDouble(txtCarga.getText());
            double consumo = Double.parseDouble(txtConsumo.getText());
            int tempoRecarga = Integer.parseInt(txtTempoRecarga.getText());

            String tipo = cbTipo.getSelectedItem().toString();

            if (tipo.equals("Veículo Elétrico")) {

                String conector = txtConector.getText();
                if (conector.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o tipo de conector do veículo.");
                    return;
                }

                int tempoRapida = Integer.parseInt(txtTempoRapida.getText());

                VeiculoEletrico eletrico = new VeiculoEletrico(
                        id,
                        modelo,
                        autonomiaMaxima,
                        cargaBateria,
                        consumo,
                        tempoRecarga,
                        conector,
                        tempoRapida
                );

                controller.cadastrar(eletrico);

            } else {

                double tanque = Double.parseDouble(txtTanque.getText());
                double consumoComb = Double.parseDouble(txtConsumoComb.getText());
                String tipoComb = txtTipoComb.getText();
                if (tipoComb.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o tipo de combustível do veículo.");
                    return;
                }

                double combAtual = Double.parseDouble(txtCombAtual.getText());

                VeiculoHibrido hibrido = new VeiculoHibrido(
                        id,
                        modelo,
                        autonomiaMaxima,
                        cargaBateria,
                        consumo,
                        tempoRecarga,
                        tanque,
                        consumoComb,
                        tipoComb,
                        combAtual
                );

                controller.cadastrar(hibrido);

            }

            JOptionPane.showMessageDialog(this,
                    "Veículo cadastrado com sucesso!");

            limparCampos();
            dispose();
            if (this.telaAnterior != null) {
                this.telaAnterior.setVisible(true);
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Digite apenas números nos campos numéricos.");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());

        }


    }

    private void limparCampos() {

        txtId.setText("");
        txtModelo.setText("");
        txtAutonomia.setText("");
        txtCarga.setText("");
        txtConsumo.setText("");
        txtTempoRecarga.setText("");

        txtConector.setText("");
        txtTempoRapida.setText("");

        txtTanque.setText("");
        txtConsumoComb.setText("");
        txtTipoComb.setText("");
        txtCombAtual.setText("");

        cbTipo.setSelectedIndex(0);
    }

    private void preencherCampos(String resposta) {

        String[] linhas = resposta.split("\n");

        for (String linha : linhas) {

            String[] partes = linha.split("=");

            if (partes.length < 2)
                continue;

            String valor = partes[1].trim();

            switch (partes[0]) {

                case "id":
                    txtId.setText(valor);
                    break;

                case "modelo":
                    txtModelo.setText(valor);
                    break;

                case "autonomia":
                    valor = valor.replace("km", "").trim();
                    txtAutonomia.setText(valor);
                    break;

                case "bateria":
                    valor = valor.replace("%", "").trim();
                    txtCarga.setText(valor);
                    break;

                case "consumo":
                    valor = valor.replace("kWh/km", "").trim();
                    txtConsumo.setText(valor);
                    break;

                case "tempo":
                    valor = valor.replace("horas", "")
                            .replace("hora", "")
                            .trim();
                    txtTempoRecarga.setText(valor);
                    break;

                case "tipo de veículo":
                    if (valor.equalsIgnoreCase("Elétrico") ||
                            valor.equalsIgnoreCase("Eletrico")) {

                        cbTipo.setSelectedIndex(0);

                    } else {

                        cbTipo.setSelectedIndex(1);
                    }

                    break;
                case "tipo de conector":
                    txtConector.setText(valor);

                    break;

                case "tempo de recarga rápida":
                    valor = valor.replace("horas", "")
                            .replace("hora", "")
                            .trim();
                    txtTempoRapida.setText(valor);
                    break;

                case "capacidade do Tanque":
                    valor = valor.replace("litros", "")
                            .replace("litro", "")
                            .trim();
                    txtTanque.setText(valor);

                    break;

                case "consumo do combustível":
                    valor = valor.replace("km/l", "").trim();
                    txtConsumoComb.setText(valor);

                    break;

                case "tipo de combustível":
                    txtTipoComb.setText(valor);

                    break;

                case "nível de combustível atual":
                    valor = valor.replace("litros", "")
                            .replace("litro", "")
                            .trim();
                    txtCombAtual.setText(valor);
                    break;
            }
        }
    }
}