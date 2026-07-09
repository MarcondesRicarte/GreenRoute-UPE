import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import gemini.ConexaoGemini;

class Eletroposto {
    int id;
    String nome, localizacao, tipoConector;
    int vagasDisponiveis;
    double tempoMedioCarga, precoPorKwh;

    public Eletroposto(int id, String nome, String localizacao, String tipoConector, int vagas, double tempo, double preco) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.tipoConector = tipoConector;
        this.vagasDisponiveis = vagas;
        this.tempoMedioCarga = tempo;
        this.precoPorKwh = preco;
    }

    public String obterResumoCompleto() {
        return "----------------------------------------\n" +
               "ID: " + id + " | NOME: " + nome + "\n" +
               "Local: " + localizacao + " | Conector: " + tipoConector + "\n" +
               "Vagas: " + vagasDisponiveis + " | Tempo: " + tempoMedioCarga + "h\n" +
               "Preço: R$ " + precoPorKwh + "/kWh\n";
    }
}

public class TelaEletropostos {
    public static ArrayList<Eletroposto> listaEletropostos = new ArrayList<>();
    private static JTextArea txtArea = new JTextArea();
    
    // Campos globais
    private static JTextField txtId = new JTextField();
    private static JTextField txtNome = new JTextField();
    private static JTextField txtLocal = new JTextField();
    private static JTextField txtConector = new JTextField();
    private static JTextField txtVagas = new JTextField();
    private static JTextField txtTempo = new JTextField();
    private static JTextField txtPreco = new JTextField();

    public static void abrirTelaEletropostos(JFrame menuAnterior) {
        JFrame frame = new JFrame("GreenRoute - Cadastro de Eletropostos");
        frame.setSize(800, 600);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.decode("#1e1e1e"));

        // Cadastro IA
        JLabel lblIA = new JLabel("Cadastro Rápido por IA:");
        lblIA.setForeground(Color.CYAN); lblIA.setBounds(20, 10, 200, 20); frame.add(lblIA);
        
        JTextField txtIA = new JTextField();
        txtIA.setBounds(20, 35, 530, 25); frame.add(txtIA);
        
        JButton btnIA = new JButton("Preencher por IA");
        btnIA.setBounds(560, 35, 150, 25);
        btnIA.setBackground(Color.decode("#00bcd4"));
        btnIA.setForeground(Color.WHITE);
        frame.add(btnIA);

        // Campos Padronizados
        configurarCampo(frame, "ID:", txtId, 80);
        configurarCampo(frame, "Nome Posto:", txtNome, 120);
        configurarCampo(frame, "Local:", txtLocal, 160);
        configurarCampo(frame, "Conector:", txtConector, 200);
        configurarCampo(frame, "Vagas:", txtVagas, 240);
        configurarCampo(frame, "Tempo (h):", txtTempo, 280);
        configurarCampo(frame, "Preço (R$):", txtPreco, 320);

        // Botões
        JButton btnSalvar = criarBotao(frame, "Salvar Eletroposto", 20, 370, "#4cdc39");
        JButton btnEditar = criarBotao(frame, "Editar Eletroposto", 20, 410, "#ce955b");
        JButton btnExcluir = criarBotao(frame, "Excluir Eletroposto", 20, 450, "#ce955b");
        JButton btnVoltar = criarBotao(frame, "← Voltar Menu", 20, 490, "#ce955b");

        // Ação Salvar com Trava de ID
        btnSalvar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                for (Eletroposto ep : listaEletropostos) {
                    if (ep.id == id) {
                        JOptionPane.showMessageDialog(frame, "Erro: Este ID já existe!");
                        return;
                    }
                }
                listaEletropostos.add(new Eletroposto(id, txtNome.getText(), txtLocal.getText(), txtConector.getText(), 
                                      Integer.parseInt(txtVagas.getText()), Double.parseDouble(txtTempo.getText().replace(",", ".")), 
                                      Double.parseDouble(txtPreco.getText().replace(",", "."))));
                atualizarListaVisual();
                limparCampos(txtId, txtNome, txtLocal, txtConector, txtVagas, txtTempo, txtPreco);
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Erro: Verifique os campos numéricos!"); }
        });

        // Botão Editar
        btnEditar.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Digite o ID para editar:");
            if (input == null) return;
            try {
                int idProc = Integer.parseInt(input.trim());
                for (Eletroposto ep : listaEletropostos) {
                    if (ep.id == idProc) {
                        ep.nome = JOptionPane.showInputDialog(frame, "Novo Nome:", ep.nome);
                        ep.localizacao = JOptionPane.showInputDialog(frame, "Nova Localização:", ep.localizacao);
                        ep.tipoConector = JOptionPane.showInputDialog(frame, "Novo Tipo de Conector:", ep.tipoConector);
                        ep.vagasDisponiveis = Integer.parseInt(JOptionPane.showInputDialog(frame, "Novas Vagas:", ep.vagasDisponiveis));
                        ep.tempoMedioCarga = Double.parseDouble(JOptionPane.showInputDialog(frame, "Novo Tempo Médio (h):", ep.tempoMedioCarga));
                        ep.precoPorKwh = Double.parseDouble(JOptionPane.showInputDialog(frame, "Novo Preço (R$):", ep.precoPorKwh));
                        
                        atualizarListaVisual();
                        JOptionPane.showMessageDialog(frame, "Eletroposto editado com sucesso!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "ID não encontrado.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Erro: Verifique os dados inseridos na edição."); }
        });

        // Botão Excluir
        btnExcluir.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Digite o ID para excluir:");
            if (input != null && listaEletropostos.removeIf(ep -> ep.id == Integer.parseInt(input.trim()))) {
                atualizarListaVisual();
                JOptionPane.showMessageDialog(frame, "Eletroposto removido!");
            }
        });

        btnVoltar.addActionListener(e -> { frame.dispose(); menuAnterior.setVisible(true); });

        // Lista
        txtArea.setEditable(false);
        txtArea.setBackground(Color.decode("#eaf8fc"));
        JScrollPane scroll = new JScrollPane(txtArea);
        scroll.setBounds(300, 80, 410, 440);
        frame.add(scroll);

        frame.setLocationRelativeTo(null); frame.setVisible(true);
    }

    private static void configurarCampo(JFrame f, String label, JTextField txt, int y) {
        JLabel lbl = new JLabel(label); lbl.setForeground(Color.WHITE); 
        lbl.setBounds(20, y, 110, 25); f.add(lbl);
        txt.setBounds(140, y, 130, 25); f.add(txt);
    }

    private static JButton criarBotao(JFrame f, String texto, int x, int y, String corHex) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, 250, 30);
        btn.setBackground(Color.decode(corHex));
        btn.setForeground(Color.WHITE);
        f.add(btn);
        return btn;
    }

    private static void limparCampos(JTextField... campos) {
        for (JTextField c : campos) c.setText("");
    }

    private static void atualizarListaVisual() {
        txtArea.setText("");
        for (Eletroposto ep : listaEletropostos) txtArea.append(ep.obterResumoCompleto() + "\n");
    }
}