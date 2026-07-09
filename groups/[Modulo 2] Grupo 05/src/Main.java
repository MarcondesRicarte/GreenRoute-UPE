import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import controller.*;
import model.*;
import exception.AutonomiaInsuficienteException;
import gemini.IAPlannerService;

public class Main extends JFrame {
    private static VeiculoController veiculoCtrl = new VeiculoController();
    private static EletropostoController eletropostoCtrl = new EletropostoController();
    private static CidadeController cidadeCtrl = new CidadeController();
    private static IAPlannerService iaService = new IAPlannerService();

    public Main() {
        setTitle("GreenRoute - Sistema Logístico com Inteligência Artificial");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBackground(new Color(46, 139, 87));
        JLabel lblTitulo = new JLabel("GreenRoute - Painel de Controle (Módulo 2 + IA)", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelTitulo.add(lblTitulo, BorderLayout.CENTER);
        add(painelTitulo, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();

        JPanel abaCrud = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        JButton btnListarVeiculos = new JButton("Visualizar Veículos");
        JButton btnListarPostos = new JButton("Visualizar Eletropostos");
        JButton btnListarCidades = new JButton("Visualizar Cidades");
        abaCrud.add(btnListarVeiculos);
        abaCrud.add(btnListarPostos);
        abaCrud.add(btnListarCidades);
        abas.addTab("Base de Dados (CRUD)", abaCrud);

        JPanel abaCadastroIA = new JPanel(new BorderLayout(10, 10));
        abaCadastroIA.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        abaCadastroIA.add(new JLabel("Cole o texto livre sobre o veículo (Ex: 'Tenho um BYD Dolphin de autonomia 300km e bateria em 80%'):"), BorderLayout.NORTH);
        
        JTextArea txtTextoLivre = new JTextArea(5, 40);
        txtTextoLivre.setLineWrap(true);
        abaCadastroIA.add(new JScrollPane(txtTextoLivre), BorderLayout.CENTER);
        
        JPanel painelAcaoIA = new JPanel(new BorderLayout(5, 5));
        JButton btnProcessarIA = new JButton("Analisar Texto com Gemini AI");
        JTextArea txtResultadoIA = new JTextArea(6, 40);
        txtResultadoIA.setEditable(false);
        txtResultadoIA.setBorder(BorderFactory.createTitledBorder("Atributos Estruturados extraídos pela IA:"));
        
        painelAcaoIA.add(btnProcessarIA, BorderLayout.NORTH);
        painelAcaoIA.add(new JScrollPane(txtResultadoIA), BorderLayout.CENTER);
        abaCadastroIA.add(painelAcaoIA, BorderLayout.SOUTH);
        abas.addTab("Cadastro Rápido por IA", abaCadastroIA);

        JPanel abaSimulador = new JPanel(new GridBagLayout());
        abaSimulador.setBorder(BorderFactory.createTitledBorder("Parâmetros do Percurso"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        abaSimulador.add(new JLabel("ID do Veículo Selecionado:"), gbc);
        gbc.gridx = 1;
        JTextField txtIdVeiculo = new JTextField(6);
        abaSimulador.add(txtIdVeiculo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        abaSimulador.add(new JLabel("ID da Cidade de Destino:"), gbc);
        gbc.gridx = 1;
        JTextField txtIdCidade = new JTextField(6);
        abaSimulador.add(txtIdCidade, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel painelBotoesSim = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton btnSimularCalculo = new JButton("Simulação Padrão (Exceções)");
        JButton btnSimularIA = new JButton("Plano de Rota Inteligente (Gemini AI)");
        painelBotoesSim.add(btnSimularCalculo);
        painelBotoesSim.add(btnSimularIA);
        abaSimulador.add(painelBotoesSim, gbc);
        
        abas.addTab("Simulador de Viagem & IA", abaSimulador);

        add(abas, BorderLayout.CENTER);


        btnListarVeiculos.addActionListener(e -> abrirModalListagem("Veículos", new String[]{"ID", "Modelo", "Autonomia Max", "Carga Atual"}));
        btnListarPostos.addActionListener(e -> abrirModalListagem("Eletropostos", new String[]{"ID", "Nome", "Localização", "Vagas"}));
        btnListarCidades.addActionListener(e -> abrirModalListagem("Cidades", new String[]{"ID", "Nome", "UF", "Distância"}));

        btnProcessarIA.addActionListener(e -> {
            String input = txtTextoLivre.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite alguma descrição do veículo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            btnProcessarIA.setText("Conectando ao Gemini...");
            btnProcessarIA.setEnabled(false);
            
            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() {
                    return iaService.processarCadastroRapido(input);
                }
                @Override
                protected void done() {
                    try {
                        txtResultadoIA.setText(get());
                    } catch (Exception ex) {
                        txtResultadoIA.setText("Erro ao processar requisição.");
                    }
                    btnProcessarIA.setText("Analisar Texto com Gemini AI");
                    btnProcessarIA.setEnabled(true);
                }
            };
            worker.execute();
        });

        btnSimularCalculo.addActionListener(e -> {
            try {
                int vId = Integer.parseInt(txtIdVeiculo.getText().trim());
                int cId = Integer.parseInt(txtIdCidade.getText().trim());
                
                Veiculo v = veiculoCtrl.buscar(vId);
                cidadeCtrl.simularViagem(v, cId, eletropostoCtrl);
                
                JOptionPane.showMessageDialog(this, "Sucesso! O veículo possui autonomia suficiente para concluir a viagem sem paradas imediatas.", "Viagem Autorizada", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preencha os campos com IDs numéricos válidos.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (AutonomiaInsuficienteException ex) {
                int cId = Integer.parseInt(txtIdCidade.getText().trim());
                StringBuilder postosSugeridos = new StringBuilder("\nEletropostos cadastrados no destino para recarga:\n");
                for (Eletroposto p : eletropostoCtrl.listar()) {
                    if (p.getCidadeId() == cId) {
                        postosSugeridos.append("- ").append(p.getNome()).append(" (").append(p.getLocalizacao()).append(")\n");
                    }
                }
                JOptionPane.showMessageDialog(this, ex.getMessage() + postosSugeridos, "Alerta de Autonomia (Exception)", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnSimularIA.addActionListener(e -> {
            try {
                int vId = Integer.parseInt(txtIdVeiculo.getText().trim());
                int cId = Integer.parseInt(txtIdCidade.getText().trim());
                
                Veiculo v = veiculoCtrl.buscar(vId);
                Cidade c = cidadeCtrl.buscar(cId);

                if (v == null || c == null) {
                    JOptionPane.showMessageDialog(this, "Veículo ou Cidade não localizados na nossa base de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                btnSimularIA.setText("Gerando Relatório de IA...");
                btnSimularIA.setEnabled(false);

                SwingWorker<String, Void> worker = new SwingWorker<>() {
                    @Override
                    protected String doInBackground() {
                        return iaService.planejarRotaInteligente(v.toString(), c.toString());
                    }
                    @Override
                    protected void done() {
                        try {
                            JTextArea areaRelatorio = new JTextArea(15, 50);
                            areaRelatorio.setText(get());
                            areaRelatorio.setLineWrap(true);
                            areaRelatorio.setWrapStyleWord(true);
                            areaRelatorio.setEditable(false);
                            
                            JScrollPane scroll = new JScrollPane(areaRelatorio);
                            JOptionPane.showMessageDialog(Main.this, scroll, "Relatório Preditivo Inteligente - Gemini", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this, "Falha ao gerar relatório preditivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                        btnSimularIA.setText("Plano de Rota Inteligente (Gemini AI)");
                        btnSimularIA.setEnabled(true);
                    }
                };
                worker.execute();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preencha os campos com IDs válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void abrirModalListagem(String titulo, String[] colunas) {
        JDialog dialog = new JDialog(this, "Listagem de " + titulo, true);
        dialog.setSize(550, 320);
        dialog.setLocationRelativeTo(this);

        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);

        if (titulo.equals("Veículos")) {
            for (Veiculo v : veiculoCtrl.listar()) {
                model.addRow(new Object[]{v.getId(), v.getModelo(), v.getAutonomiaMaxima() + " km", v.getCargaBateriaAtual() + "%"});
            }
        } else if (titulo.equals("Eletropostos")) {
            for (Eletroposto p : eletropostoCtrl.listar()) {
                model.addRow(new Object[]{p.getId(), p.getNome(), p.getLocalizacao(), p.getVagasDisponiveis()});
            }
        } else {
            for (Cidade c : cidadeCtrl.listar()) {
                model.addRow(new Object[]{c.getId(), c.getNome(), c.getEstado(), c.getDistanciaDaCapital() + " km"});
            }
        }

        dialog.add(new JScrollPane(tabela));
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        cidadeCtrl.cadastrar(new Cidade(1, "Campinas", "SP", 90.0));
        cidadeCtrl.cadastrar(new Cidade(2, "Ribeirão Preto", "SP", 315.0));
        veiculoCtrl.cadastrar(new VeiculoEletrico(1, "BYD Dolphin", 300.0, 35.0, 0.15, 400, "CCS2", 30));
        eletropostoCtrl.cadastrar(new Eletroposto(101, "Graal Bandeirantes", "Rod. Bandeirantes KM 120", 2, "CCS2", 150, 1.90, 4));

        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
