import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import gemini.ConexaoGemini;

class CidadeObjeto {
    int id;
    String cep;
    String nome;
    String uf;
    double distanciaCapital;

    public CidadeObjeto(int id, String cep, String nome, String uf, double distanciaCapital) {
        this.id = id;
        this.cep = cep;
        this.nome = nome;
        this.uf = uf;
        this.distanciaCapital = distanciaCapital;
    }

    public String obterResSummary() {
        return "----------------------------------------\n" +
               "ID: " + id + " | CIDADE\n" +
               "CEP: " + cep + "\n" +
               "Nome: " + nome + "\n" +
               "Estado: " + uf + "\n" +
               "Distância Capital: " + distanciaCapital + " km\n";
    }
}

public class TelaCidades {
    public static ArrayList<CidadeObjeto> listaCidades = new ArrayList<>();
    private static JTextArea txtArea = new JTextArea();
    private static int proximoId = 1; 

    private static JTextField txtIA = new JTextField();
    private static JTextField txtCep = new JTextField();
    private static JTextField txtNome = new JTextField();
    private static JTextField txtUf = new JTextField();
    private static JTextField txtDistancia = new JTextField();

    public static void abrirTelaCidades(JFrame frameMenuAnterior) {
        JFrame frame = new JFrame("GreenRoute - Cadastro de Cidades");
        frame.setSize(800, 600); 
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.decode("#1e1e1e")); 

        // --- CADASTRO RÁPIDO POR IA ---
        JLabel lblIA = new JLabel("Cadastro Rápido por IA:");
        lblIA.setForeground(Color.CYAN); 
        lblIA.setBounds(20, 10, 200, 20); 
        lblIA.setFont(new Font("Arial", Font.BOLD, 14)); 
        frame.add(lblIA);

        txtIA.setBounds(20, 35, 530, 25); 
        frame.add(txtIA);
        
        JButton btnIA = new JButton("Preencher por IA");
        btnIA.setBounds(560, 35, 150, 25);
        btnIA.setBackground(Color.decode("#00bcd4"));
        btnIA.setForeground(Color.WHITE);
        frame.add(btnIA);

        // 
        configurarCampoGrafico(frame, "CEP:", txtCep, 80);
        configurarCampoGrafico(frame, "Nome Cidade:", txtNome, 120);
        configurarCampoGrafico(frame, "Estado (UF):", txtUf, 160);
        configurarCampoGrafico(frame, "Dist. Médio (km):", txtDistancia, 200);

        // Botão IA
        btnIA.addActionListener(e -> {
            String textoUsuario = txtIA.getText().trim();
            if (textoUsuario.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Digite um texto para a IA processar.");
                return;
            }

            String prompt = "Extraia os dados de cidade do seguinte texto e retorne APENAS os valores na ordem exata descrita abaixo, "
                    + "separados unicamente por ponto e vírgula (;). Não inclua nenhuma frase, nenhuma palavra explicativa ou markdown.\n"
                    + "Ordem dos campos:\n"
                    + "1. CEP (Formato 00000-000)\n"
                    + "2. Nome da Cidade (String)\n"
                    + "3. UF (Apenas as 2 letras do Estado)\n"
                    + "4. Distância aproximada até a capital (Apenas número decimal. Padrão 0)\n\n"
                    + "Texto do usuário: " + textoUsuario;

            btnIA.setText("Processando...");
            btnIA.setEnabled(false);

            new Thread(() -> {
                String resposta = ConexaoGemini.chamarIA(prompt); 
                
                SwingUtilities.invokeLater(() -> {
                    btnIA.setText("Preencher por IA");
                    btnIA.setEnabled(true);

                    if (resposta == null || resposta.startsWith("ERRO")) {
                        JOptionPane.showMessageDialog(frame, "Erro na comunicação com a IA.");
                        return;
                    }

                    try {
                        String[] dados = resposta.replace("\n", "").replace("\r", "").split(";");
                        if (dados.length >= 4) {
                            txtCep.setText(dados[0].trim());
                            txtNome.setText(dados[1].trim().toUpperCase());
                            txtUf.setText(dados[2].trim().toUpperCase());
                            txtDistancia.setText(dados[3].trim());
                            JOptionPane.showMessageDialog(frame, "Campos preenchidos! Revise e clique em 'Salvar Cidade'.");
                        } else {
                            JOptionPane.showMessageDialog(frame, "A IA não localizou os parâmetros necessários.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Erro ao processar o retorno de dados da IA.");
                    }
                });
            }).start();
        });

        //  Botão Salvar
        JButton btnSalvar = new JButton("Salvar Cidade");
        btnSalvar.setBounds(20, 330, 250, 30); 
        btnSalvar.setBackground(Color.decode("#4cdc39"));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            try {
                String cep = txtCep.getText().trim();
                String nome = txtNome.getText().trim().toUpperCase();
                String uf = txtUf.getText().trim().toUpperCase();
                
                if (nome.isEmpty() || cep.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Erro: CEP e Nome são obrigatórios.");
                    return;
                }

                double dist = 0;
                if (!txtDistancia.getText().trim().isEmpty()) {
                    dist = Double.parseDouble(txtDistancia.getText().trim().replace(",", "."));
                }

                // [CORREÇÃO AQUI] Bloqueia totalmente se o CEP já existir na lista
                for (CidadeObjeto c : listaCidades) {
                    if (c.cep.replace("-", "").equals(cep.replace("-", ""))) {
                        JOptionPane.showMessageDialog(frame, "Erro: Esta cidade já está cadastrada com este CEP!");
                        return; // Cancela e sai da função sem salvar nada nem alterar
                    }
                }

                // Se o CEP não existe, faz um cadastro totalmente novo
                CidadeObjeto nova = new CidadeObjeto(proximoId++, cep, nome, uf, dist);
                listaCidades.add(nova);
                JOptionPane.showMessageDialog(frame, "Cidade cadastrada com sucesso!");
                
                atualizarListaVisual();
                txtCep.setText(""); txtNome.setText(""); txtUf.setText(""); txtDistancia.setText(""); txtIA.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Erro: Insira um número válido no campo distância.");
            }
        });

        // Botão Editar
        JButton btnEditar = new JButton("Editar por CEP");
        btnEditar.setBounds(20, 370, 250, 30); 
        btnEditar.setBackground(Color.decode("#ce955b"));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(btnEditar);

        btnEditar.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Digite o CEP da cidade para editar:");
            if (input != null && !input.trim().isEmpty()) {
                String cepProc = input.trim().replace("-", "");
                for (CidadeObjeto c : listaCidades) {
                    if (c.cep.replace("-", "").equals(cepProc)) {
                        String n = JOptionPane.showInputDialog(frame, "Novo Nome da Cidade:", c.nome);
                        if (n != null && !n.trim().isEmpty()) c.nome = n.trim().toUpperCase();

                        String estado = JOptionPane.showInputDialog(frame, "Nova UF:", c.uf);
                        if (estado != null && !estado.trim().isEmpty()) c.uf = estado.trim().toUpperCase();

                        String distStr = JOptionPane.showInputDialog(frame, "Nova Distância à Capital (km):", c.distanciaCapital);
                        if (distStr != null && !distStr.trim().isEmpty()) c.distanciaCapital = Double.parseDouble(distStr.replace(",", "."));

                        atualizarListaVisual();
                        JOptionPane.showMessageDialog(frame, "Cidade atualizada com sucesso!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "CEP não encontrado.");
            }
        });

        //  Botão Excluir
        JButton btnExcluir = new JButton("Excluir por CEP");
        btnExcluir.setBounds(20, 410, 250, 30); 
        btnExcluir.setBackground(Color.decode("#ce955b"));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(btnExcluir);

        btnExcluir.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Digite o CEP para remover:");
            if (input != null && !input.trim().isEmpty()) {
                String cepProc = input.trim().replace("-", "");
                boolean removido = listaCidades.removeIf(c -> c.cep.replace("-", "").equals(cepProc));
                if (removido) {
                    for (int i = 0; i < listaCidades.size(); i++) { listaCidades.get(i).id = i + 1; }
                    proximoId = listaCidades.size() + 1;
                    atualizarListaVisual();
                    JOptionPane.showMessageDialog(frame, "Cidade removida com sucesso!");
                } else { 
                    JOptionPane.showMessageDialog(frame, "CEP não localizado."); 
                }
            }
        });

        // Botão voltar
        JButton btnVoltar = new JButton("← Voltar Menu");
        btnVoltar.setBounds(20, 495, 130, 30); 
        btnVoltar.setBackground(Color.decode("#ce955b"));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFont(new Font("Label", Font.BOLD, 12));
        frame.add(btnVoltar);
        btnVoltar.addActionListener(e -> { frame.dispose(); frameMenuAnterior.setVisible(true); });

        //Painel 
        txtArea.setEditable(false);
        txtArea.setBackground(Color.decode("#eaf8fc"));
        txtArea.setForeground(Color.BLACK);
        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtArea); 
        scroll.setBounds(300, 80, 410, 410); 
        frame.add(scroll);

        atualizarListaVisual();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    private static void atualizarListaVisual() {
        txtArea.setText("");
        for (CidadeObjeto c : listaCidades) { 
            txtArea.append(c.obterResSummary() + "\n"); 
        }
    }

    private static void configurarCampoGrafico(JFrame f, String labelText, JTextField campo, int y) {
        JLabel lbl = new JLabel(labelText); 
        lbl.setForeground(Color.WHITE); 
        lbl.setBounds(20, y, 110, 25); 
        f.add(lbl);
        
        campo.setBounds(140, y, 130, 25); 
        f.add(campo); 
    }
}