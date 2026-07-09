package view;

import controller.VeiculoController;
import controller.CidadeController;
import controller.RotaController;
import model.Veiculo;
import model.Cidade;
import exceptions.AutonomiaInsuficienteException;
import exceptions.ConectorIncompativelException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PainelRotas extends JPanel {

    private final VeiculoController veiculoController;
    private final CidadeController cidadeController;
    private final RotaController rotaController;

    private JComboBox<Veiculo> comboVeiculo;
    private JComboBox<Cidade> comboCidade;
    private JTextArea areaResultado;
    private JButton botaoSimular, botaoPlanejarIA;

    public PainelRotas(VeiculoController veiculoController, CidadeController cidadeController, RotaController rotaController) {
        this.veiculoController = veiculoController;
        this.cidadeController = cidadeController;
        this.rotaController = rotaController;

        setLayout(new BorderLayout(10, 10));
        add(construirFormulario(), BorderLayout.NORTH);
        add(construirAreaResultado(), BorderLayout.CENTER);
    }

    private JPanel construirFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Simular Rota / Planejar com IA"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        comboVeiculo = new JComboBox<>();
        comboCidade = new JComboBox<>();

        c.gridx = 0; c.gridy = 0;
        painel.add(new JLabel("Veículo:"), c);
        c.gridx = 1;
        painel.add(comboVeiculo, c);

        c.gridx = 0; c.gridy = 1;
        painel.add(new JLabel("Cidade de destino:"), c);
        c.gridx = 1;
        painel.add(comboCidade, c);

        JButton botaoAtualizarListas = new JButton("Atualizar listas");
        botaoAtualizarListas.addActionListener(e -> atualizarCombos());
        c.gridx = 0; c.gridy = 2;
        painel.add(botaoAtualizarListas, c);

        botaoSimular = new JButton("Simular Rota (algoritmo)");
        botaoSimular.addActionListener(e -> simular());
        c.gridx = 1; c.gridy = 2;
        painel.add(botaoSimular, c);

        botaoPlanejarIA = new JButton("Planejar com IA");
        botaoPlanejarIA.addActionListener(e -> planejarComIA());
        c.gridx = 1; c.gridy = 3;
        painel.add(botaoPlanejarIA, c);

        atualizarCombos();
        return painel;
    }

    private void atualizarCombos() {
        comboVeiculo.removeAllItems();
        ArrayList<Veiculo> veiculos = veiculoController.listarTodosVeiculosController();
        for (Veiculo v : veiculos) comboVeiculo.addItem(v);

        comboCidade.removeAllItems();
        ArrayList<Cidade> cidades = cidadeController.listarCidadesController();
        for (Cidade cid : cidades) comboCidade.addItem(cid);
    }

    private boolean selecaoValida() {
        if (comboVeiculo.getSelectedItem() == null || comboCidade.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Cadastre ao menos um veículo e uma cidade antes de continuar.",
                    "Dados insuficientes", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void simular() {
        if (!selecaoValida()) return;

        Veiculo veiculo = (Veiculo) comboVeiculo.getSelectedItem();
        Cidade cidade = (Cidade) comboCidade.getSelectedItem();

        try {
            String resultado = rotaController.simularRota(veiculo.getId(), cidade.getId());
            areaResultado.setText(resultado);

        } catch (AutonomiaInsuficienteException | ConectorIncompativelException e) {
            areaResultado.setText("");
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Não foi possível simular a rota", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void planejarComIA() {
        if (!selecaoValida()) return;

        Veiculo veiculo = (Veiculo) comboVeiculo.getSelectedItem();
        Cidade cidade = (Cidade) comboCidade.getSelectedItem();

        botaoPlanejarIA.setEnabled(false);
        botaoPlanejarIA.setText("Consultando Gemini...");
        areaResultado.setText("Analisando a viagem com IA, aguarde...");

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                return rotaController.planejarRotaComIA(veiculo.getId(), cidade.getId());
            }

            @Override
            protected void done() {
                botaoPlanejarIA.setEnabled(true);
                botaoPlanejarIA.setText("Planejar com IA");

                try {
                    areaResultado.setText(get());
                } catch (Exception ex) {
                    areaResultado.setText("");
                    JOptionPane.showMessageDialog(PainelRotas.this,
                            "Não foi possível consultar a IA: " + ex.getCause().getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private JScrollPane construirAreaResultado() {
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        return new JScrollPane(areaResultado);
    }
}