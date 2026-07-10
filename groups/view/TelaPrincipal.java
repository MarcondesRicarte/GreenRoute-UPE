package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    private JTable tableVeiculos;
    private JTextField textModeloVeiculo;
    private JTextField textBateriaVeiculo; 
    private JTextField textCadastroIA;
    private JButton btnProcessarIA;
    
    private JTable tableEletropostos;
    private JTextField textNomeEletroposto;
    private JTextField textLocalizacaoEletroposto;
    private JTextField textVagasEletroposto;

    private JTable tableCidades;
    private JTextField textNomeCidade;
    private JTextField textEstadoCidade;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaPrincipal frame = new TelaPrincipal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaPrincipal() {
        setTitle("GreenRoute - Sistema de Logística Inteligente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 640, 580); 
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(10, 11, 604, 519); 
        contentPane.add(tabbedPane);

        // --- PAINEL VEÍCULOS ---
        JPanel painelVeiculos = new JPanel();
        tabbedPane.addTab("Veículos Elétricos", null, painelVeiculos, null);
        painelVeiculos.setLayout(null);
        
        JScrollPane scrollPaneVeiculos = new JScrollPane();
        scrollPaneVeiculos.setBounds(10, 11, 579, 150);
        painelVeiculos.add(scrollPaneVeiculos);

        DefaultTableModel modelVeiculosBase = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Modelo", "Bateria (%)" });
        tableVeiculos = new JTable(modelVeiculosBase);
        scrollPaneVeiculos.setViewportView(tableVeiculos);

        // Evento de clique na tabela de Veículos (Preenche os campos ao clicar na linha)
        tableVeiculos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linha = tableVeiculos.getSelectedRow();
                if (linha != -1) {
                    textModeloVeiculo.setText(tableVeiculos.getValueAt(linha, 1).toString());
                    textBateriaVeiculo.setText(tableVeiculos.getValueAt(linha, 2).toString());
                }
            }
        });

        JLabel lblModelo = new JLabel("Modelo:"); lblModelo.setBounds(10, 180, 100, 20); painelVeiculos.add(lblModelo);
        textModeloVeiculo = new JTextField(); textModeloVeiculo.setBounds(120, 180, 200, 20); painelVeiculos.add(textModeloVeiculo);

        JLabel lblBateria = new JLabel("Carga Bateria (%):"); lblBateria.setBounds(10, 210, 120, 20); painelVeiculos.add(lblBateria);
        textBateriaVeiculo = new JTextField(); textBateriaVeiculo.setBounds(140, 210, 180, 20); painelVeiculos.add(textBateriaVeiculo);

        JLabel lblCadastroIA = new JLabel("Cadastro Rápido por IA:"); lblCadastroIA.setBounds(10, 250, 150, 20); painelVeiculos.add(lblCadastroIA);
        textCadastroIA = new JTextField(); textCadastroIA.setBounds(10, 275, 579, 25); painelVeiculos.add(textCadastroIA);
        
        btnProcessarIA = new JButton("Processar com IA"); btnProcessarIA.setBounds(10, 310, 160, 25); painelVeiculos.add(btnProcessarIA);
        btnProcessarIA.addActionListener(e -> {
            String textoCorrido = textCadastroIA.getText().trim();
            if (textoCorrido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um texto descritivo para a IA processar.");
                return;
            }
            
            String textoMinusculo = textoCorrido.toLowerCase();
            
            String bateriaExtraida = "100"; 
            java.util.regex.Pattern padraoBateria = java.util.regex.Pattern.compile("\\d{1,3}");
            java.util.regex.Matcher buscadorBateria = padraoBateria.matcher(textoMinusculo);
            
            if (buscadorBateria.find()) {
                bateriaExtraida = buscadorBateria.group();
            }
            
            String modeloExtraido = textoCorrido; 
            String[] filtrosRemocao = {
                "(?i)cadastre um", "(?i)cadastrar um", "(?i)cadastre", "(?i)cadastrar", 
                "(?i)tenho um", "(?i)com", "(?i)de bateria", "(?i)bateria", 
                "(?i)de carga", "(?i)carga", "(?i)de", "(?i)porcento", "%"
            };
            
            for (String filtro : filtrosRemocao) {
                modeloExtraido = modeloExtraido.replaceAll(filtro, "");
            }
            
            modeloExtraido = modeloExtraido.replaceAll(bateriaExtraida, "").trim();
            
            if (modeloExtraido.isEmpty()) {
                modeloExtraido = "Carro Elétrico Genérico";
            }
            
            textModeloVeiculo.setText(modeloExtraido);
            textBateriaVeiculo.setText(bateriaExtraida); 
            
            JOptionPane.showMessageDialog(this, 
                "--- IA GreenRoute Extração ---\n" +
                "Modelo identificado: " + modeloExtraido + "\n" +
                "Bateria identificada: " + bateriaExtraida + "%", 
                "Processamento Concluído", 
                JOptionPane.INFORMATION_MESSAGE);
                
            textCadastroIA.setText(""); 
        });

        // Botão Cadastrar Veículo
        JButton btnCadVeiculo = new JButton("Cadastrar"); btnCadVeiculo.setBounds(10, 360, 100, 25); painelVeiculos.add(btnCadVeiculo);
        btnCadVeiculo.addActionListener(e -> {
            if(textModeloVeiculo.getText().isEmpty()) return;
            modelVeiculosBase.addRow(new Object[]{ modelVeiculosBase.getRowCount() + 1, textModeloVeiculo.getText(), textBateriaVeiculo.getText() });
            textModeloVeiculo.setText(""); textBateriaVeiculo.setText("");
        });

        // NOVOS BOTÕES ADICIONADOS: Atualizar Veículo
        JButton btnAtualizarVeiculo = new JButton("Atualizar"); btnAtualizarVeiculo.setBounds(120, 360, 100, 25); painelVeiculos.add(btnAtualizarVeiculo);
        btnAtualizarVeiculo.addActionListener(e -> {
            int linha = tableVeiculos.getSelectedRow();
            if (linha != -1) {
                modelVeiculosBase.setValueAt(textModeloVeiculo.getText(), linha, 1);
                modelVeiculosBase.setValueAt(textBateriaVeiculo.getText(), linha, 2);
                textModeloVeiculo.setText(""); textBateriaVeiculo.setText("");
                JOptionPane.showMessageDialog(this, "Veículo atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um veículo na tabela para atualizar.");
            }
        });

        // NOVOS BOTÕES ADICIONADOS: Excluir Veículo
        JButton btnExcluirVeiculo = new JButton("Excluir"); btnExcluirVeiculo.setBounds(230, 360, 100, 25); painelVeiculos.add(btnExcluirVeiculo);
        btnExcluirVeiculo.addActionListener(e -> {
            int linha = tableVeiculos.getSelectedRow();
            if (linha != -1) {
                modelVeiculosBase.removeRow(linha);
                textModeloVeiculo.setText(""); textBateriaVeiculo.setText("");
                JOptionPane.showMessageDialog(this, "Veículo excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um veículo na tabela para excluir.");
            }
        });

        // --- PAINEL CIDADES ---
        JPanel painelCidades = new JPanel();
        tabbedPane.addTab("Cidades", null, painelCidades, null);
        painelCidades.setLayout(null);

        JScrollPane scrollPaneCidades = new JScrollPane();
        scrollPaneCidades.setBounds(10, 11, 579, 150);
        painelCidades.add(scrollPaneCidades);

        DefaultTableModel modelCidadesBase = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Nome da Cidade", "Estado (UF)" });
        tableCidades = new JTable(modelCidadesBase);
        scrollPaneCidades.setViewportView(tableCidades);

        tableCidades.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linha = tableCidades.getSelectedRow();
                if (linha != -1) {
                    textNomeCidade.setText(tableCidades.getValueAt(linha, 1).toString());
                    textEstadoCidade.setText(tableCidades.getValueAt(linha, 2).toString());
                }
            }
        });

        JLabel lblNomeC = new JLabel("Nome da Cidade:"); lblNomeC.setBounds(10, 180, 120, 20); painelCidades.add(lblNomeC);
        textNomeCidade = new JTextField(); textNomeCidade.setBounds(140, 180, 250, 20); painelCidades.add(textNomeCidade);

        JLabel lblEstadoC = new JLabel("Estado (UF):"); lblEstadoC.setBounds(10, 210, 120, 20); painelCidades.add(lblEstadoC);
        textEstadoCidade = new JTextField(); textEstadoCidade.setBounds(140, 210, 100, 20); painelCidades.add(textEstadoCidade);

        JButton btnCadastrarC = new JButton("Cadastrar"); btnCadastrarC.setBounds(10, 260, 110, 25); painelCidades.add(btnCadastrarC);
        btnCadastrarC.addActionListener(e -> {
            if(textNomeCidade.getText().isEmpty()) return;
            modelCidadesBase.addRow(new Object[]{ modelCidadesBase.getRowCount() + 1, textNomeCidade.getText(), textEstadoCidade.getText() });
            textNomeCidade.setText(""); textEstadoCidade.setText("");
        });

        JButton btnAtualizarC = new JButton("Atualizar"); btnAtualizarC.setBounds(130, 260, 110, 25); painelCidades.add(btnAtualizarC);
        btnAtualizarC.addActionListener(e -> {
            int linha = tableCidades.getSelectedRow();
            if(linha != -1) {
                modelCidadesBase.setValueAt(textNomeCidade.getText(), linha, 1);
                modelCidadesBase.setValueAt(textEstadoCidade.getText(), linha, 2);
            }
        });

        JButton btnExcluirC = new JButton("Excluir"); btnExcluirC.setBounds(250, 260, 110, 25); painelCidades.add(btnExcluirC);
        btnExcluirC.addActionListener(e -> {
            int línea = tableCidades.getSelectedRow();
            if(línea != -1) modelCidadesBase.removeRow(línea);
        });

        // --- PAINEL ELETROPOSTOS ---
        JPanel painelEletropostos = new JPanel();
        tabbedPane.addTab("Eletropostos", null, painelEletropostos, null);
        painelEletropostos.setLayout(null);

        JScrollPane scrollPaneEletro = new JScrollPane();
        scrollPaneEletro.setBounds(10, 11, 579, 150);
        painelEletropostos.add(scrollPaneEletro);

        DefaultTableModel modelEletro = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Posto", "Localização", "Vagas" });
        tableEletropostos = new JTable(modelEletro);
        scrollPaneEletro.setViewportView(tableEletropostos);

        tableEletropostos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linha = tableEletropostos.getSelectedRow();
                if(linha != -1) {
                    textNomeEletroposto.setText(tableEletropostos.getValueAt(linha, 1).toString());
                    textLocalizacaoEletroposto.setText(tableEletropostos.getValueAt(linha, 2).toString());
                    textVagasEletroposto.setText(tableEletropostos.getValueAt(linha, 3).toString());
                }
            }
        });

        JLabel lblNomeE = new JLabel("Nome Posto:"); lblNomeE.setBounds(10, 180, 100, 20); painelEletropostos.add(lblNomeE);
        textNomeEletroposto = new JTextField(); textNomeEletroposto.setBounds(120, 180, 250, 20); painelEletropostos.add(textNomeEletroposto);

        JLabel lblLocE = new JLabel("Localização:"); lblLocE.setBounds(10, 210, 100, 20); painelEletropostos.add(lblLocE);
        textLocalizacaoEletroposto = new JTextField(); textLocalizacaoEletroposto.setBounds(120, 210, 250, 20); painelEletropostos.add(textLocalizacaoEletroposto);

        JLabel lblVagE = new JLabel("Vagas:"); lblVagE.setBounds(10, 240, 100, 20); painelEletropostos.add(lblVagE);
        textVagasEletroposto = new JTextField(); textVagasEletroposto.setBounds(120, 240, 100, 20); painelEletropostos.add(textVagasEletroposto);

        JButton btnCadE = new JButton("Cadastrar"); btnCadE.setBounds(10, 290, 100, 25); painelEletropostos.add(btnCadE);
        btnCadE.addActionListener(e -> {
            modelEletro.addRow(new Object[]{ modelEletro.getRowCount()+1, textNomeEletroposto.getText(), textLocalizacaoEletroposto.getText(), textVagasEletroposto.getText() });
        });
        
        JButton btnAtuE = new JButton("Atualizar"); btnAtuE.setBounds(120, 290, 100, 25); painelEletropostos.add(btnAtuE);
        btnAtuE.addActionListener(e -> {
            int linha = tableEletropostos.getSelectedRow();
            if(linha != -1) {
                modelEletro.setValueAt(textNomeEletroposto.getText(), linha, 1);
                modelEletro.setValueAt(textLocalizacaoEletroposto.getText(), linha, 2);
                modelEletro.setValueAt(textVagasEletroposto.getText(), linha, 3);
            }
        });

        JButton btnExcE = new JButton("Excluir"); btnExcE.setBounds(230, 290, 100, 25); painelEletropostos.add(btnExcE);
        btnExcE.addActionListener(e -> {
            int linha = tableEletropostos.getSelectedRow();
            if(linha != -1) modelEletro.removeRow(linha);
        });

        // --- PAINEL SIMULADOR DE VIAGEM ---
        JPanel painelSimulador = new JPanel();
        tabbedPane.addTab("Simulador de Viagem", null, painelSimulador, null);
        painelSimulador.setLayout(null);

        JLabel lblSelecioneV = new JLabel("Selecione o Veículo (ID ou Nome):"); lblSelecioneV.setBounds(10, 20, 250, 20); painelSimulador.add(lblSelecioneV);
        JTextField txtVeiculoSimulacao = new JTextField(); txtVeiculoSimulacao.setBounds(10, 45, 250, 20); painelSimulador.add(txtVeiculoSimulacao);

        JLabel lblOrigem = new JLabel("Cidade de Origem:"); lblOrigem.setBounds(10, 80, 200, 20); painelSimulador.add(lblOrigem);
        JTextField txtOrigem = new JTextField(); txtOrigem.setBounds(10, 105, 250, 20); painelSimulador.add(txtOrigem);

        JLabel lblDestino = new JLabel("Cidade de Destino:"); lblDestino.setBounds(10, 140, 200, 20); painelSimulador.add(lblDestino);
        JTextField txtDestino = new JTextField(); txtDestino.setBounds(10, 165, 250, 20); painelSimulador.add(txtDestino);

        JButton btnSimularViagem = new JButton("Calcular Viagem Inteligente"); btnSimularViagem.setBounds(10, 210, 250, 30); painelSimulador.add(btnSimularViagem);
        
        btnSimularViagem.addActionListener(e -> {
            String veiculo = txtVeiculoSimulacao.getText();
            String orig = txtOrigem.getText();
            String dest = txtDestino.getText();
            
            if (veiculo.isEmpty() || orig.isEmpty() || dest.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos para simular a rota.");
                return;
            }

            int bateriaAtual = 100; 
            DefaultTableModel modelV = (DefaultTableModel) tableVeiculos.getModel();
            
            for (int i = 0; i < modelV.getRowCount(); i++) {
                if (modelV.getValueAt(i, 1).toString().equalsIgnoreCase(veiculo)) {
                    try {
                        bateriaAtual = Integer.parseInt(modelV.getValueAt(i, 2).toString());
                        break;
                    } catch (Exception ex) {
                        bateriaAtual = 100;
                    }
                }
            }

            int autonomiaRestanteKm = bateriaAtual * 4;
            int distanciaEntreCidades = 250; 
            
            StringBuilder resultado = new StringBuilder();
            resultado.append("--- RELATÓRIO GREENROUTE (Simulação LLM) ---\n");
            resultado.append("Veículo: ").append(veiculo).append(" | Bateria Atual: ").append(bateriaAtual).append("%\n");
            resultado.append("Rota: ").append(orig).append(" -> ").append(dest).append("\n");
            resultado.append("Distância estimada: ").append(distanciaEntreCidades).append(" km\n");
            resultado.append("Autonomia máxima com a carga atual: ").append(autonomiaRestanteKm).append(" km\n\n");

            if (autonomiaRestanteKm >= distanciaEntreCidades) {
                resultado.append("✅ RESULTADO: A carga é SUFICIENTE! Você chegará ao destino com cerca de ")
                         .append((autonomiaRestanteKm - distanciaEntreCidades) / 4)
                         .append("% de bateria restante.");
            } else {
                resultado.append("❌ RESULTADO: Carga INSUFICIENTE. Você precisará parar em um eletroposto intermediário para recarga.");
            }

            JOptionPane.showMessageDialog(this, resultado.toString(), "Planejador de Rotas Inteligente", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}