import javax.swing.*;

import gemini.ConexaoGemini;

import java.awt.*;

public class TelaPlanejadorRotas {

    private static Veiculo buscarVeiculoPorModelo(String modelo) {
        for (Veiculo v : TelaVeiculo.listaVeiculos) {
            if (v.modelo.equalsIgnoreCase(modelo)) return v;
        }
        return null;
    }

    public static void abrirTela(JFrame menuAnterior) {
        JFrame frame = new JFrame("GreenRoute - Planejador Inteligente");
        frame.setSize(850, 600);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.decode("#1e1e1e"));

        // Campos de entrada
        JLabel lblOrigem = new JLabel("Cidade Origem:"); lblOrigem.setForeground(Color.WHITE);
        lblOrigem.setBounds(20, 20, 200, 25); frame.add(lblOrigem);
        JTextField txtOrigem = new JTextField();
        txtOrigem.setBounds(20, 45, 250, 25); frame.add(txtOrigem);

        JLabel lblDestino = new JLabel("Cidade Destino:"); lblDestino.setForeground(Color.WHITE);
        lblDestino.setBounds(20, 80, 200, 25); frame.add(lblDestino);
        JComboBox<String> comboCidades = new JComboBox<>();
        for (CidadeObjeto c : TelaCidades.listaCidades) comboCidades.addItem(c.nome);
        comboCidades.setBounds(20, 105, 250, 25); frame.add(comboCidades);

        JLabel lblV = new JLabel("Selecione o Veículo:"); lblV.setForeground(Color.WHITE);
        lblV.setBounds(20, 140, 200, 25); frame.add(lblV);
        JComboBox<String> comboVeiculos = new JComboBox<>();
        for (Veiculo v : TelaVeiculo.listaVeiculos) comboVeiculos.addItem(v.modelo);
        comboVeiculos.setBounds(20, 165, 250, 25); frame.add(comboVeiculos);

        JButton btnCalcular = new JButton("Calcular Rota Inteligente");
        btnCalcular.setBounds(20, 220, 250, 40);
        btnCalcular.setBackground(Color.decode("#4cdc39"));
        btnCalcular.setFont(new Font("Label", Font.BOLD, 14));
        btnCalcular.setForeground(Color.WHITE);

        frame.add(btnCalcular);

        JTextArea txtIA = new JTextArea();
        txtIA.setEditable(false);
        txtIA.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(txtIA);
        scroll.setBounds(300, 20, 500, 450);
        frame.add(scroll);

        btnCalcular.addActionListener(e -> {
        // 1. Coletar dados da tela
        String origem = txtOrigem.getText(); // Certifique-se que você tem este campo
        String destino = (String)comboCidades.getSelectedItem();
        String modelo = (String)comboVeiculos.getSelectedItem();
        
        // 2. Buscar o objeto real (autonomia, carga, etc)
        Veiculo v = buscarVeiculoPorModelo(modelo);

        if (v != null && !origem.isEmpty()) {
            // 3. Montar o prompt COMPLETO aqui, com os dados reais
            String promptFinal = "Atue como especialista em logística. Planeje a rota de " + origem + " para " + destino + 
                                ". Veículo: " + v.modelo + 
                                ". Autonomia: " + v.autonomiaMaxima + " km, Consumo: " + v.consumoKwhPorKm + " kWh/km, " +
                                "Bateria Atual: " + v.cargaBateriaAtual + "%. " +
                                "Considere trânsito e clima. Indique necessidade de recarga e tempo total.";
            
            txtIA.setText("Processando análise logística...");

            // 4. Aqui você chama o método existente. 
            // O MÉTODO enviarPrompt precisa APENAS receber a String e retornar a resposta.
            // Se o seu método atual lança erro, ele precisa ser reescrito para apenas 
            // fazer a conexão HTTP com a API, sem ter lógica de construção de prompt dentro dele.
            
            try {
                String resposta = ConexaoGemini.chamarIA(promptFinal);
                txtIA.setText(resposta);
            } catch (Exception ex) {
                txtIA.setText("Erro na conexão: Verifique se o método 'enviarPrompt' na classe ConexaoGemini está implementado para realizar a chamada HTTP.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Preencha origem e selecione um veículo.");
        }
    });

        JButton btnVoltar = new JButton("← Voltar Menu");
        btnVoltar.setBounds(20, 500, 130, 30);
        btnVoltar.setBackground(Color.decode("#ce955b"));
        btnVoltar.setFont(new Font("Label", Font.BOLD, 12));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.addActionListener(e -> { frame.dispose(); menuAnterior.setVisible(true); });
        frame.add(btnVoltar);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}