package view;

import controller.CidadeController;
import controller.EletropostoController;
import controller.RotaController;
import controller.VeiculoController;
import Gemini.CadastroVeiculoIA;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(VeiculoController veiculoController,
                     CidadeController cidadeController,
                     EletropostoController eletropostoController,
                     RotaController rotaController,
                     CadastroVeiculoIA cadastroVeiculoIA) {

        super("GreenRoute");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // centraliza na tela

        JTabbedPane abas = new JTabbedPane();

        abas.addTab("Veículos", new PainelVeiculos(veiculoController, cadastroVeiculoIA));
        abas.addTab("Cidades", new PainelCidades(cidadeController));
        abas.addTab("Eletropostos", new PainelEletropostos(eletropostoController, cidadeController));
        abas.addTab("Simular Rota", new PainelRotas(veiculoController, cidadeController, rotaController));

        add(abas);
    }
}