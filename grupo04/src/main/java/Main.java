import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import controller.*;
import exception.AutonomiaInsuficienteException;
import gemini.ConexaoGemini;
import model.*;
import repository.*;

public class Main extends JFrame {

    private static VeiculoController veiculoController;
    private static CidadeController cidadeController;
    private static EletropostoController eletropostoController;
    private static SimulacaoController simulacaoController;
    private static ConexaoGemini conexaoGemini;

    private JTextField txtCidId;
    private JTextField txtCidNome;
    private JTextField txtCidDistancia;
    private JTextField txtIACidade;
    private JTable tabelaCidades;
    private DefaultTableModel modeloCidades;

    private JTextField txtVeiId;
    private JTextField txtVeiModelo;
    private JTextField txtVeiAutonomia;
    private JTextField txtVeiCarga;
    private JTextField txtVeiConsumo;
    private JTextField txtVeiTempo;
    private JTextField txtVeiConector;
    private JTextField txtVeiRapido;
    private JTextField txtIAVeiculo;
    private JTable tabelaVeiculos;
    private DefaultTableModel modeloVeiculos;

    private JTextField txtPostoId;
    private JTextField txtPostoNome;
    private JTextField txtPostoLocal;
    private JTextField txtPostoVagas;
    private JTextField txtIAEletroposto;
    private JTable tabelaEletropostos;
    private DefaultTableModel modeloEletropostos;

    private JTextField txtSimVeiId;
    private JTextField txtSimCidId;
    private JTextArea txtResultadoIA;

    public Main() {

        setTitle("GreenRoute - Interface Módulo 2");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Gerenciar Cidades", criarPainelCidades());
        abas.addTab("Gerenciar Veículos", criarPainelVeiculos());
        abas.addTab("Gerenciar Eletropostos", criarPainelEletropostos());
        abas.addTab("Planejador de Rotas (LLM)", criarPainelSimulacao());

        add(abas);

        atualizarTabelaCidades();
        atualizarTabelaVeiculos();
        atualizarTabelaEletropostos();

        conexaoGemini = new ConexaoGemini();
    }

private JPanel criarPainelCidades() {

    JPanel painel = new JPanel(new BorderLayout(10, 10));

    JPanel formularioEsquerda = new JPanel(new BorderLayout(5, 5));

    JPanel campos = new JPanel(new GridLayout(4, 2, 5, 5));
    campos.setBorder(BorderFactory.createTitledBorder("Cadastro Tradicional"));

    campos.add(new JLabel("ID (IBGE):"));
    txtCidId = new JTextField();
    campos.add(txtCidId);

    campos.add(new JLabel("Nome:"));
    txtCidNome = new JTextField();
    campos.add(txtCidNome);

    campos.add(new JLabel("Distância (km):"));
    txtCidDistancia = new JTextField();
    campos.add(txtCidDistancia);

    JButton btnSalvar = new JButton("Salvar Cidade");
    campos.add(btnSalvar);

    formularioEsquerda.add(campos, BorderLayout.NORTH);

    JPanel painelIA = new JPanel(new BorderLayout(5, 5));
    painelIA.setBorder(BorderFactory.createTitledBorder("Cadastro Rápido por IA"));

    txtIACidade = new JTextField();

    JButton btnIACidade = new JButton("Processar com LLM");

    painelIA.add(new JLabel("Digite o texto livre:"), BorderLayout.NORTH);
    painelIA.add(txtIACidade, BorderLayout.CENTER);
    painelIA.add(btnIACidade, BorderLayout.SOUTH);

    formularioEsquerda.add(painelIA, BorderLayout.SOUTH);

    painel.add(formularioEsquerda, BorderLayout.WEST);

    modeloCidades = new DefaultTableModel(
            new String[]{"ID", "Nome", "Distância"}, 0);

    tabelaCidades = new JTable(modeloCidades);

    painel.add(new JScrollPane(tabelaCidades), BorderLayout.CENTER);

    btnSalvar.addActionListener(e -> salvarCidade());
    btnIACidade.addActionListener(e -> processarIACidade());

    return painel;
}

private void salvarCidade() {

    try {

        int id = Integer.parseInt(txtCidId.getText().trim());
        String nome = txtCidNome.getText().trim();
        double distancia = Double.parseDouble(txtCidDistancia.getText().trim());

        Cidade cidade = new Cidade(id, nome, distancia);

        if (cidadeController.cadastrarCidade(cidade)) {

            JOptionPane.showMessageDialog(this,
                    "Cidade cadastrada com sucesso!");

            atualizarTabelaCidades();

        } else {

            JOptionPane.showMessageDialog(this,
                    "Já existe uma cidade com esse ID.");

        }

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(this,
                "Preencha todos os campos corretamente.");

    }
}

private void processarIACidade() {

    String texto = txtIACidade.getText().trim();

    if (texto.isEmpty()) {

        JOptionPane.showMessageDialog(this,
                "Digite uma descrição da cidade.");

        return;
    }

    try {

        String prompt =
                "Extraia do texto abaixo apenas no formato:\n" +
                "ID;Nome;Distancia\n\n" +
                texto;

        String resposta = conexaoGemini.perguntarParaGemini(prompt);

        String[] dados = resposta.split(";");

        if (dados.length != 3) {
            throw new Exception("Resposta inválida da IA.");
        }

        txtCidId.setText(dados[0].trim());
        txtCidNome.setText(dados[1].trim());
        txtCidDistancia.setText(dados[2].trim());

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(this,
                "Erro ao processar a IA:\n" + ex.getMessage());

    }
}

private void atualizarTabelaCidades() {

    modeloCidades.setRowCount(0);

    for (Cidade cidade : cidadeController.listarCidades()) {
        modeloCidades.addRow(new Object[]{cidade.getId(), cidade.getNome(), cidade.getDistanciaDaCapital()});
    }}private JPanel criarPainelVeiculos() {

    JPanel painel = new JPanel(new BorderLayout(10, 10));

    JPanel formularioEsquerda = new JPanel(new BorderLayout(5, 5));

    JPanel campos = new JPanel(new GridLayout(9, 2, 5, 5));
    campos.setBorder(BorderFactory.createTitledBorder("Cadastro Tradicional"));

    campos.add(new JLabel("ID:"));
    txtVeiId = new JTextField();
    campos.add(txtVeiId);

    campos.add(new JLabel("Modelo:"));
    txtVeiModelo = new JTextField();
    campos.add(txtVeiModelo);

    campos.add(new JLabel("Autonomia Máx:"));
    txtVeiAutonomia = new JTextField();
    campos.add(txtVeiAutonomia);

    campos.add(new JLabel("Carga (%):"));
    txtVeiCarga = new JTextField();
    campos.add(txtVeiCarga);

    campos.add(new JLabel("Consumo:"));
    txtVeiConsumo = new JTextField();
    campos.add(txtVeiConsumo);

    campos.add(new JLabel("Tempo de Recarga:"));
    txtVeiTempo = new JTextField();
    campos.add(txtVeiTempo);

    campos.add(new JLabel("Conector:"));
    txtVeiConector = new JTextField();
    campos.add(txtVeiConector);

    campos.add(new JLabel("Recarga Rápida:"));
    txtVeiRapido = new JTextField();
    campos.add(txtVeiRapido);

    JButton btnSalvar = new JButton("Salvar Veículo");
    campos.add(btnSalvar);

    formularioEsquerda.add(campos, BorderLayout.NORTH);

    JPanel painelIA = new JPanel(new BorderLayout(5, 5));
    painelIA.setBorder(BorderFactory.createTitledBorder("Cadastro Rápido por IA"));

    txtIAVeiculo = new JTextField();

    JButton btnIAVeiculo = new JButton("Processar com LLM");

    painelIA.add(new JLabel("Digite a descrição do veículo:"), BorderLayout.NORTH);
    painelIA.add(txtIAVeiculo, BorderLayout.CENTER);
    painelIA.add(btnIAVeiculo, BorderLayout.SOUTH);

    formularioEsquerda.add(painelIA, BorderLayout.SOUTH);

    painel.add(formularioEsquerda, BorderLayout.WEST);

    modeloVeiculos = new DefaultTableModel(
            new String[]{"ID", "Modelo", "Autonomia", "Carga"}, 0);

    tabelaVeiculos = new JTable(modeloVeiculos);

    painel.add(new JScrollPane(tabelaVeiculos), BorderLayout.CENTER);

    btnSalvar.addActionListener(e -> salvarVeiculo());
    btnIAVeiculo.addActionListener(e -> processarIAVeiculo());

    return painel;
}

private void salvarVeiculo() {

    try {

        int id = Integer.parseInt(txtVeiId.getText().trim());
        String modelo = txtVeiModelo.getText().trim();
        double autonomia = Double.parseDouble(txtVeiAutonomia.getText().trim());
        double carga = Double.parseDouble(txtVeiCarga.getText().trim());
        double consumo = Double.parseDouble(txtVeiConsumo.getText().trim());
        int tempo = Integer.parseInt(txtVeiTempo.getText().trim());
        String conector = txtVeiConector.getText().trim();
        int rapido = Integer.parseInt(txtVeiRapido.getText().trim());

        VeiculoEletrico veiculo = new VeiculoEletrico(
                id,
                modelo,
                autonomia,
                carga,
                consumo,
                tempo,
                conector,
                rapido
        );

        if (veiculoController.cadastrarVeiculo(veiculo)) {

            JOptionPane.showMessageDialog(this,
                    "Veículo cadastrado com sucesso!");

            atualizarTabelaVeiculos();

        } else {

            JOptionPane.showMessageDialog(this,
                    "Já existe um veículo com esse ID.");

        }

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(this,
                "Preencha todos os campos corretamente.");

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(this,
                ex.getMessage());

    }
}

private void processarIAVeiculo() {
        String texto = txtIAVeiculo.getText().trim();
        if (texto.isEmpty()) return;
        // 4. IA INTEGRADA NO MÉTODO
        String resp = conexaoGemini.perguntarParaGemini("Extraia: ID;Modelo;Autonomia;Carga;Consumo;Tempo;Conector;Rapido de '" + texto + "'");
        try {
            String[] p = resp.split(";");
            txtVeiId.setText(p[0]); txtVeiModelo.setText(p[1]); txtVeiAutonomia.setText(p[2]); txtVeiCarga.setText(p[3]);
            txtVeiConsumo.setText(p[4]); txtVeiTempo.setText(p[5]); txtVeiConector.setText(p[6]); txtVeiRapido.setText(p[7]);
            VeiculoEletrico ve = new VeiculoEletrico(Integer.parseInt(p[0]), p[1], Double.parseDouble(p[2]), Double.parseDouble(p[3]), Double.parseDouble(p[4]), Integer.parseInt(p[5]), p[6], Integer.parseInt(p[7]));
            if (veiculoController.cadastrarVeiculo(ve)) { atualizarTabelaVeiculos(); txtIAVeiculo.setText(""); }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro IA: " + resp); }
    }

    private void atualizarTabelaVeiculos() {
        modeloVeiculos.setRowCount(0);
        for (Veiculo v : veiculoController.listarVeiculos()) {
            modeloVeiculos.addRow(new Object[]{v.getId(), v.getModelo(), v.getAutonomiaMaxima(), v.getCargaBateriaAtual()});
        }
    }

    private JPanel criarPainelEletropostos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JPanel formularioEsquerda = new JPanel(new BorderLayout(5, 5));
        JPanel campos = new JPanel(new GridLayout(5, 2, 5, 5));
        campos.setBorder(BorderFactory.createTitledBorder("Cadastro Tradicional"));
        campos.add(new JLabel(" ID:")); txtPostoId = new JTextField(); campos.add(txtPostoId);
        campos.add(new JLabel(" Nome:")); txtPostoNome = new JTextField(); campos.add(txtPostoNome);
        campos.add(new JLabel(" Local:")); txtPostoLocal = new JTextField(); campos.add(txtPostoLocal);
        campos.add(new JLabel(" Vagas:")); txtPostoVagas = new JTextField(); campos.add(txtPostoVagas);
        JButton btnSalvar = new JButton("Salvar Eletroposto");
        campos.add(btnSalvar);
        formularioEsquerda.add(campos, BorderLayout.NORTH);
        JPanel painelIA = new JPanel(new BorderLayout(5, 5));
        painelIA.setBorder(BorderFactory.createTitledBorder("Cadastro Rápido por IA"));
        txtIAEletroposto = new JTextField();
        JButton btnIAEletroposto = new JButton("Processar com LLM");
        painelIA.add(txtIAEletroposto, BorderLayout.CENTER);
        painelIA.add(btnIAEletroposto, BorderLayout.SOUTH);
        formularioEsquerda.add(painelIA, BorderLayout.SOUTH);
        painel.add(formularioEsquerda, BorderLayout.WEST);
        modeloEletropostos = new DefaultTableModel(new String[]{"ID", "Nome", "Localização", "Vagas"}, 0);
        tabelaEletropostos = new JTable(modeloEletropostos);
        painel.add(new JScrollPane(tabelaEletropostos), BorderLayout.CENTER);
        btnSalvar.addActionListener(e -> salvarEletroposto());
        btnIAEletroposto.addActionListener(e -> processarIAEletroposto());
        return painel;
    }

    private void salvarEletroposto() {
        try {
            int id = Integer.parseInt(txtPostoId.getText().trim());
            String nome = txtPostoNome.getText().trim();
            String local = txtPostoLocal.getText().trim();
            int vagas = Integer.parseInt(txtPostoVagas.getText().trim());
            Eletroposto ep = new Eletroposto(id, nome, local, 0, "CCS2", 50.0, 1.50, vagas);
            if (eletropostoController.cadastrarEletroposto(ep)) {
                atualizarTabelaEletropostos();
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro nos campos."); }
    }

    private void processarIAEletroposto() {
        String texto = txtIAEletroposto.getText().trim();
        if (texto.isEmpty()) return;
        // 5. IA INTEGRADA NO MÉTODO
        String resp = conexaoGemini.perguntarParaGemini("Extraia: ID;Nome;Local;Vagas de '" + texto + "'");
        try {
            String[] p = resp.split(";");
            txtPostoId.setText(p[0]); txtPostoNome.setText(p[1]); txtPostoLocal.setText(p[2]); txtPostoVagas.setText(p[3]);
            Eletroposto ep = new Eletroposto(Integer.parseInt(p[0]), p[1], p[2], 0, "CCS2", 50.0, 1.50, Integer.parseInt(p[3]));
            if (eletropostoController.cadastrarEletroposto(ep)) { atualizarTabelaEletropostos(); txtIAEletroposto.setText(""); }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro IA: " + resp); }
    }

    private void atualizarTabelaEletropostos() {
        modeloEletropostos.setRowCount(0);
        for (Eletroposto ep : eletropostoController.listarEletropostos()) {
            modeloEletropostos.addRow(new Object[]{ep.getId(), ep.getNome(), ep.getLocalizacao(), ep.getVagasDisponiveis()});
        }
    }

    private JPanel criarPainelSimulacao() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JPanel topo = new JPanel(new GridLayout(3, 2, 5, 5));
        topo.add(new JLabel(" ID Veículo:")); txtSimVeiId = new JTextField(); topo.add(txtSimVeiId);
        topo.add(new JLabel(" ID Cidade:")); txtSimCidId = new JTextField(); topo.add(txtSimCidId);
        JButton btnSimular = new JButton("Processar");
        topo.add(btnSimular);
        painel.add(topo, BorderLayout.NORTH);
        txtResultadoIA = new JTextArea();
        painel.add(new JScrollPane(txtResultadoIA), BorderLayout.CENTER);
        btnSimular.addActionListener(e -> rodarPlanejadorIA());
        return painel;
    }

    private void rodarPlanejadorIA() {
        try {
            int vId = Integer.parseInt(txtSimVeiId.getText().trim());
            int cId = Integer.parseInt(txtSimCidId.getText().trim());
            
            // 1. Validação técnica (Se o carro não tiver bateria, ele para aqui e cai no catch)
            simulacaoController.rodarSimulacao(vId, cId);
            
            // 2. Se a autonomia for suficiente, pede para a IA planejar
            String prompt = "Planeje a rota para o veículo ID " + vId + " indo para a cidade ID " + cId;
            String respostaIA = conexaoGemini.perguntarParaGemini(prompt);
            
            // 3. Exibe a resposta da IA na sua JTextArea
            txtResultadoIA.setText(respostaIA);
            
        } catch (AutonomiaInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Alerta", JOptionPane.WARNING_MESSAGE);
            txtResultadoIA.setText("Erro de Autonomia: " + ex.getMessage());
        } catch (Exception ex) { 
            txtResultadoIA.setText("Erro na conexão com a IA: " + ex.getMessage()); 
        }
    }

    public static void main(String[] args) {
        veiculoController = new VeiculoController(new VeiculoRepository());
        cidadeController = new CidadeController(new CidadeRepository());
        eletropostoController = new EletropostoController(new EletropostoRepository());
        simulacaoController = new SimulacaoController(new VeiculoRepository(), new CidadeRepository(), new EletropostoRepository());
        
        conexaoGemini = new ConexaoGemini(); // 6. INICIALIZADO
        
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
