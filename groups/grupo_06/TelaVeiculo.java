import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import gemini.ConexaoGemini;

class Veiculo {
    int id;
    String modelo;
    double autonomiaMaxima;
    double cargaBateriaAtual;
    double consumoKwhPorKm;
    double tempoRecargaCompleta;
    String tipoConector;
    double tempoRecargaRapida;
    double capacidadeTanque;
    double consumoCombustivel;
    String tipoCombustivel;
    String tipoVeiculo;

    public Veiculo(int id, String modelo, double auto, double carga, double consumo, double tempoRecarga, 
                   String conector, double tempoRapido, double tanque, double consComb, String tipoComb, String tipo) {
        this.id = id; this.modelo = modelo; this.autonomiaMaxima = auto; this.cargaBateriaAtual = carga;
        this.consumoKwhPorKm = consumo; this.tempoRecargaCompleta = tempoRecarga;
        this.tipoConector = conector; this.tempoRecargaRapida = tempoRapido;
        this.capacidadeTanque = tanque; this.consumoCombustivel = consComb;
        this.tipoCombustivel = tipoComb; this.tipoVeiculo = tipo;
    }

    public String obterResSummary() {
        String resumo = "----------------------------------------\n" +
                        "ID: " + id + " | TIPO: " + tipoVeiculo.toUpperCase() + "\n" +
                        "Modelo: " + modelo + "\n" +
                        "Autonomia Máx: " + autonomiaMaxima + " km\n" +
                        "Carga Atual: " + cargaBateriaAtual + "%\n" +
                        "Consumo Elétrico: " + consumoKwhPorKm + " kWh/km\n" +
                        "Tempo Recarga: " + tempoRecargaCompleta + " h\n";
        
        if (tipoVeiculo.equals("ELÉTRICO")) {
            resumo += "Conector: " + (tipoConector.isEmpty() ? "NÃO INFORMADO" : tipoConector) + "\n" +
                      "Recarga Rápida: " + tempoRecargaRapida + " min\n";
        } else {
            resumo += "Capac. Tanque: " + capacidadeTanque + " L\n" +
                      "Consumo Comb.: " + consumoCombustivel + " km/L\n" +
                      "Combustível: " + (tipoCombustivel.isEmpty() ? "NÃO INFORMADO" : tipoCombustivel) + "\n";
        }
        return resumo;
    }
}

public class TelaVeiculo {
    public static ArrayList<Veiculo> listaVeiculos = new ArrayList<>();
    private static JTextArea txtVeiculosCadastrados = new JTextArea();
    private static int proximoId = 1; 
    private static Veiculo veiculoEmEdicao = null; 

    private static JTextField txtIA;
    private static JTextField txtModelo;
    private static JTextField txtAutonomia;
    private static JTextField txtCarga;
    private static JTextField txtConsumoKwh;
    private static JTextField txtTempoRecarga;
    private static JTextField txtConector;
    private static JTextField txtTempoRapido;
    private static JTextField txtTanque;
    private static JTextField txtConsumoComb;
    private static JTextField txtTipoComb;

    public static void abrirTelaCadastro(JFrame frameMenuAnterior) {
        JFrame frameCadastro = new JFrame("GreenRoute - Cadastro de Veículos");
        frameCadastro.setSize(800, 600);
        frameCadastro.setLayout(null);
        frameCadastro.getContentPane().setBackground(Color.decode("#1e1e1e"));

        JPanel panel = new JPanel();
        panel.setLayout(null); 
        panel.setBounds(10, 10, 800, 650); 
        panel.setBackground(Color.decode("#1e1e1e"));

        JLabel lblIA = new JLabel("Cadastro Rápido por IA:");
        lblIA.setForeground(Color.CYAN); 
        lblIA.setBounds(20, 10, 200, 20); 
        panel.add(lblIA);
        
        txtIA = new JTextField();
        txtIA.setBounds(20, 35, 500, 25); 
        panel.add(txtIA);
        
        JButton btnIA = new JButton("Preencher por IA");
        btnIA.setBounds(530, 35, 135, 25);
        btnIA.setBackground(Color.decode("#00bcd4"));
        btnIA.setForeground(Color.BLACK);
        panel.add(btnIA);

        txtModelo = criarCampo(panel, "Modelo:", 80);
        txtAutonomia = criarCampo(panel, "Autonomia Máx (km):", 110);
        txtCarga = criarCampo(panel, "Carga Bateria (%):", 140);
        txtConsumoKwh = criarCampo(panel, "Consumo (kWh/km):", 170);
        txtTempoRecarga = criarCampo(panel, "Tempo Recarga (h):", 200);

        JLabel lblEletrico = new JLabel("--- ESPECÍFICO ELÉTRICO ---");
        lblEletrico.setForeground(Color.YELLOW); 
        lblEletrico.setBounds(20, 240, 200, 20); 
        panel.add(lblEletrico);
        
        txtConector = criarCampo(panel, "Tipo Conector:", 265);
        txtTempoRapido = criarCampo(panel, "Tempo Rec. Rápida (min):", 295);

        JLabel lblHibrido = new JLabel("--- ESPECÍFICO HÍBRIDO ---");
        lblHibrido.setForeground(Color.YELLOW); 
        lblHibrido.setBounds(20, 335, 200, 20); 
        panel.add(lblHibrido);
        
        txtTanque = criarCampo(panel, "Capac. Tanque (L):", 360);
        txtConsumoComb = criarCampo(panel, "Consumo Comb. (km/L):", 390);
        txtTipoComb = criarCampo(panel, "Tipo Combustível:", 420);

        btnIA.addActionListener(e -> {
            String textoUsuario = txtIA.getText().trim();
            if (textoUsuario.isEmpty()) {
                JOptionPane.showMessageDialog(frameCadastro, "Digite o nome ou modelo do veículo para a IA pesquisar.");
                return;
            }

            String prompt = "Você é um specialist em automóveis. Com base no modelo de veículo fornecido pelo usuário (\"" + textoUsuario + "\"), "
                    + "use sua base de conhecimento para encontrar os dados técnicos reais desse carro (especialmente se for elétrico ou híbrido). "
                    + "Você DEVE retornar estritamente os 10 valores correspondentes na mesma linha, separados unicamente por ponto e vírgula (;).\n"
                    + "Se o campo for completamente desconhecido ou não se aplicar, use '0' para números ou 'DESCONHECIDO' para textos.\n"
                    + "Campos:\n"
                    + "1.Modelo; 2.Autonomia Média(km); 3.Carga Atual Padrão(use 100); 4.Consumo Médio(kWh/km); 5.Tempo Recarga Total AC(h); 6.Tipo de Conector Padrão; 7.Tempo Recarga Rápida DC(min); 8.Capacidade do Tanque(L - se híbrido); 9.Consumo de Combustível(km/L - se híbrido); 10.Tipo Combustível(se híbrido)\n\n"
                    + "Exemplo de saída para veículo elétrico conhecido: BYD DOLPHIN PLUS;427;100;0.16;7;CCS2;30;0;0;DESCONHECIDO\n\n"
                    + "Modelo informado: " + textoUsuario;

            btnIA.setText("Pesquisando...");
            btnIA.setEnabled(false);

            new Thread(() -> {
                try {
                    String resposta = ConexaoGemini.chamarIA(prompt);

                    SwingUtilities.invokeLater(() -> {
                        btnIA.setText("Preencher por IA");
                        btnIA.setEnabled(true);

                        if (resposta == null || resposta.startsWith("ERRO")) {
                            JOptionPane.showMessageDialog(frameCadastro, "Erro na comunicação com a IA.");
                            return; 
                        }

                        try {
                            String limpo = resposta.replace("\n", "").replace("\r", "").replace("`", "").trim();
                            String[] dados = limpo.split(";");

                            if (dados.length >= 10) {
                                txtModelo.setText(dados[0].trim().equalsIgnoreCase("DESCONHECIDO") ? textoUsuario.toUpperCase() : dados[0].trim().toUpperCase());
                                txtAutonomia.setText(dados[1].trim().equals("0") ? "" : dados[1].trim());
                                txtCarga.setText(dados[2].trim().equals("0") ? "100" : dados[2].trim());
                                txtConsumoKwh.setText(dados[3].trim().equals("0") ? "" : dados[3].trim());
                                txtTempoRecarga.setText(dados[4].trim().equals("0") ? "" : dados[4].trim());
                                txtConector.setText(dados[5].trim().equalsIgnoreCase("DESCONHECIDO") ? "" : dados[5].trim().toUpperCase());
                                txtTempoRapido.setText(dados[6].trim().equals("0") ? "" : dados[6].trim());
                                txtTanque.setText(dados[7].trim().equals("0") ? "" : dados[7].trim());
                                txtConsumoComb.setText(dados[8].trim().equals("0") ? "" : dados[8].trim());
                                txtTipoComb.setText(dados[9].trim().equalsIgnoreCase("DESCONHECIDO") ? "" : dados[9].trim().toUpperCase());
                                
                                JOptionPane.showMessageDialog(frameCadastro, "Veículo localizado! Dados preenchidos automaticamente.");
                            } else {
                                JOptionPane.showMessageDialog(frameCadastro, "A IA não conseguiu estruturar os dados.");
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frameCadastro, "Erro ao processar as caixas de preenchimento.");
                        }
                    });

                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        btnIA.setText("Preencher por IA");
                        btnIA.setEnabled(true);
                    });
                }
            }).start();
        });

        JButton btnSalvar = new JButton("Salvar Veículo");
        btnSalvar.setBounds(290, 310, 375, 30);
        btnSalvar.setBackground(Color.decode("#4cdc39"));
        btnSalvar.setForeground(Color.BLACK);
        panel.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            String modelo = txtModelo.getText().trim().toUpperCase();
            if (modelo.isEmpty()) {
                JOptionPane.showMessageDialog(frameCadastro, "Erro: O campo 'Modelo' é obrigatório.");
                return;
            }

            for (Veiculo v : listaVeiculos) {
                if (v.modelo.equalsIgnoreCase(modelo) && (TelaVeiculo.veiculoEmEdicao == null || v.id != TelaVeiculo.veiculoEmEdicao.id)) {
                    JOptionPane.showMessageDialog(frameCadastro, "Erro: O veículo \"" + modelo + "\" já está cadastrado!");
                    return;
                }
            }

            double auto = 0, carga = 0, consumo = 0, tempoR = 0;
            double tempoRapido = 0, tanque = 0, consComb = 0;

            try {
                if (!txtAutonomia.getText().trim().isEmpty()) auto = Double.parseDouble(txtAutonomia.getText().trim().replace(",", "."));
                if (!txtCarga.getText().trim().isEmpty()) carga = Double.parseDouble(txtCarga.getText().trim().replace(",", "."));
                if (!txtConsumoKwh.getText().trim().isEmpty()) consumo = Double.parseDouble(txtConsumoKwh.getText().trim().replace(",", "."));
                if (!txtTempoRecarga.getText().trim().isEmpty()) tempoR = Double.parseDouble(txtTempoRecarga.getText().trim().replace(",", "."));
                if (!txtTempoRapido.getText().trim().isEmpty()) tempoRapido = Double.parseDouble(txtTempoRapido.getText().trim().replace(",", "."));
                if (!txtTanque.getText().trim().isEmpty()) tanque = Double.parseDouble(txtTanque.getText().trim().replace(",", "."));
                if (!txtConsumoComb.getText().trim().isEmpty()) consComb = Double.parseDouble(txtConsumoComb.getText().trim().replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frameCadastro, "Erro: Use apenas números nos campos correspondentes.");
                return;
            }

            String conector = txtConector.getText().trim().toUpperCase();
            String tipoComb = txtTipoComb.getText().trim().toUpperCase();
            String tipoVeiculo = (tanque > 0 || consComb > 0 || !tipoComb.isEmpty()) ? "HÍBRIDO" : "ELÉTRICO";

            if (TelaVeiculo.veiculoEmEdicao != null) {
                TelaVeiculo.veiculoEmEdicao.modelo = modelo;
                TelaVeiculo.veiculoEmEdicao.autonomiaMaxima = auto;
                TelaVeiculo.veiculoEmEdicao.cargaBateriaAtual = carga;
                TelaVeiculo.veiculoEmEdicao.consumoKwhPorKm = consumo;
                TelaVeiculo.veiculoEmEdicao.tempoRecargaCompleta = tempoR;
                TelaVeiculo.veiculoEmEdicao.tipoConector = conector;
                TelaVeiculo.veiculoEmEdicao.tempoRecargaRapida = tempoRapido;
                TelaVeiculo.veiculoEmEdicao.capacidadeTanque = tanque;
                TelaVeiculo.veiculoEmEdicao.consumoCombustivel = consComb;
                TelaVeiculo.veiculoEmEdicao.tipoCombustivel = tipoComb;
                TelaVeiculo.veiculoEmEdicao.tipoVeiculo = tipoVeiculo;
                
                TelaVeiculo.veiculoEmEdicao = null; 
                JOptionPane.showMessageDialog(frameCadastro, "Alterações gravadas com sucesso!");
            } else {
                Veiculo novo = new Veiculo(proximoId++, modelo, auto, carga, consumo, tempoR, conector, tempoRapido, tanque, consComb, tipoComb, tipoVeiculo);
                listaVeiculos.add(novo);
                JOptionPane.showMessageDialog(frameCadastro, "Veículo salvo com sucesso!");
            }
            
            atualizarLista();
            limparCampos(txtModelo, txtAutonomia, txtCarga, txtConsumoKwh, txtTempoRecarga, txtConector, txtTempoRapido, txtTanque, txtConsumoComb, txtTipoComb, txtIA);
        });

        JButton btnEditar = new JButton("Editar Veículo Completo por ID");
        btnEditar.setBounds(290, 355, 375, 30);
        btnEditar.setBackground(Color.decode("#ce955b"));
        btnEditar.setForeground(Color.WHITE);
        panel.add(btnEditar);

        btnEditar.addActionListener(e -> {
            String inputId = JOptionPane.showInputDialog(frameCadastro, "Digite o ID do veículo que deseja alterar:");
            if (inputId == null || inputId.trim().isEmpty()) return;

            try {
                int targetId = Integer.parseInt(inputId.trim());
                TelaVeiculo.veiculoEmEdicao = null;

                for (Veiculo v : listaVeiculos) {
                    if (v.id == targetId) {
                        TelaVeiculo.veiculoEmEdicao = v;
                        break;
                    }
                }

                if (TelaVeiculo.veiculoEmEdicao != null) {
                    txtModelo.setText(TelaVeiculo.veiculoEmEdicao.modelo);
                    txtAutonomia.setText(String.valueOf(TelaVeiculo.veiculoEmEdicao.autonomiaMaxima));
                    txtCarga.setText(String.valueOf(TelaVeiculo.veiculoEmEdicao.cargaBateriaAtual));
                    txtConsumoKwh.setText(String.valueOf(TelaVeiculo.veiculoEmEdicao.consumoKwhPorKm));
                    txtTempoRecarga.setText(String.valueOf(TelaVeiculo.veiculoEmEdicao.tempoRecargaCompleta));
                    txtConector.setText(TelaVeiculo.veiculoEmEdicao.tipoConector);
                    txtTempoRapido.setText(String.valueOf(TelaVeiculo.veiculoEmEdicao.tempoRecargaRapida));
                    txtTanque.setText(String.valueOf(TelaVeiculo.veiculoEmEdicao.capacidadeTanque));
                    txtConsumoComb.setText(String.valueOf(TelaVeiculo.veiculoEmEdicao.consumoCombustivel));
                    txtTipoComb.setText(TelaVeiculo.veiculoEmEdicao.tipoCombustivel);

                    JOptionPane.showMessageDialog(frameCadastro, "Alterações prontas! Altere os campos e clique em 'Salvar Veículo'.");
                } else {
                    JOptionPane.showMessageDialog(frameCadastro, "Nenhum veículo encontrado com o ID " + targetId);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frameCadastro, "ID Inválido!");
            }
        });

        JButton btnExcluir = new JButton("Excluir por ID");
        btnExcluir.setBounds(290, 395, 375, 30);
        btnExcluir.setBackground(Color.decode("#ce955b"));
        btnExcluir.setForeground(Color.WHITE);
        panel.add(btnExcluir);

        btnExcluir.addActionListener(e -> {
            String inputId = JOptionPane.showInputDialog(frameCadastro, "Digite o ID exato do carro que deseja remover:");
            if (inputId == null || inputId.trim().isEmpty()) return;

            try {
                int targetId = Integer.parseInt(inputId.trim());
                Veiculo veiculoParaRemover = null;

                for (Veiculo v : listaVeiculos) {
                    if (v.id == targetId) {
                        veiculoParaRemover = v;
                        break;
                    }
                }

                if (veiculoParaRemover != null) {
                    listaVeiculos.remove(veiculoParaRemover);

                    for (int i = 0; i < listaVeiculos.size(); i++) {
                        listaVeiculos.get(i).id = i + 1;
                    }
                    proximoId = listaVeiculos.size() + 1; 

                    atualizarLista();
                    JOptionPane.showMessageDialog(frameCadastro, "Veículo removido com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(frameCadastro, "Nenhum carro localizado com o ID " + targetId);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frameCadastro, "Digite apenas o número do ID.");
            }
        });

        JButton btnVoltar = new JButton("← Voltar Menu");
        btnVoltar.setBounds(20, 495, 130, 30);
        btnVoltar.setBackground(Color.decode("#ce955b"));
        btnVoltar.setFont(new Font("Label", Font.BOLD, 12));
        btnVoltar.setForeground(Color.WHITE);
        panel.add(btnVoltar);
        btnVoltar.addActionListener(e -> { frameCadastro.dispose(); frameMenuAnterior.setVisible(true); });

        txtVeiculosCadastrados.setEditable(false);
        txtVeiculosCadastrados.setBackground(Color.decode("#eaf8fc"));
        txtVeiculosCadastrados.setForeground(Color.BLACK);
        txtVeiculosCadastrados.setFont(new Font("Label", Font.BOLD, 11));
        JScrollPane scroll = new JScrollPane(txtVeiculosCadastrados); 
        scroll.setBounds(290, 80, 375, 210);
        panel.add(scroll);

        frameCadastro.add(panel);
        atualizarLista();
        frameCadastro.setLocationRelativeTo(null); 
        frameCadastro.setVisible(true);
    }

    private static void atualizarLista() {
        txtVeiculosCadastrados.setText("");
        for (Veiculo v : listaVeiculos) { 
            txtVeiculosCadastrados.append(v.obterResSummary() + "\n"); 
        }
    }

    private static void limparCampos(JTextField... campos) {
        for (JTextField c : campos) c.setText("");
    }

    private static JTextField criarCampo(JPanel p, String l, int y) {
        JLabel lbl = new JLabel(l); 
        lbl.setForeground(Color.WHITE); 
        lbl.setBounds(20, y, 140, 25); 
        p.add(lbl);
        
        JTextField txt = new JTextField(); 
        txt.setBounds(160, y, 110, 25); 
        p.add(txt); 
        return txt;
    }
}