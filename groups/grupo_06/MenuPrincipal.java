import javax.swing.*;
import java.awt.*;

public class MenuPrincipal {

    public static void criarMenu() {
        // Configuração da Janela
        JFrame frameMenu = new JFrame("GreenRoute - Sistema Inteligente");
        frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMenu.setSize(450, 450);
        
        // Layout Nulo para permitir controle total das posições com setBounds
        frameMenu.setLayout(null); 
        frameMenu.getContentPane().setBackground(Color.decode("#4cdc39"));

        // Título do sistema
        JLabel lblTitulo = new JLabel("GREENROUTE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(50, 20, 350, 30);
        frameMenu.add(lblTitulo);

        // Criando botões com estilo padronizado
        JButton btnVeiculos = criarBotaoMenu("Menu Veículos");
        JButton btnEletropostos = criarBotaoMenu("Menu Eletropostos");
        JButton btnCidades = criarBotaoMenu("Menu Cidades");
        JButton btnPlanejador = criarBotaoMenu("Planejador Inteligente");

        // Definindo posições (X, Y, Largura, Altura) para ficarem um abaixo do outro
        btnVeiculos.setBounds(100, 80, 250, 50);
        btnEletropostos.setBounds(100, 145, 250, 50);
        btnCidades.setBounds(100, 210, 250, 50);
        btnPlanejador.setBounds(100, 275, 250, 50);

        // Ações dos botões (Chamando os métodos corretos)
        btnVeiculos.addActionListener(e -> { 
            frameMenu.setVisible(false); 
            TelaVeiculo.abrirTelaCadastro(frameMenu); 
        });

        btnEletropostos.addActionListener(e -> { 
            frameMenu.setVisible(false); 
            TelaEletropostos.abrirTelaEletropostos(frameMenu); 
        });

        btnCidades.addActionListener(e -> { 
            frameMenu.setVisible(false); 
            TelaCidades.abrirTelaCidades(frameMenu); 
        });

        // Chamada corrigida para o método abrirTela
        btnPlanejador.addActionListener(e -> { 
            frameMenu.setVisible(false); 
            TelaPlanejadorRotas.abrirTela(frameMenu); 
        });

        // Adicionando botões ao frame
        frameMenu.add(btnVeiculos);
        frameMenu.add(btnEletropostos);
        frameMenu.add(btnCidades);
        frameMenu.add(btnPlanejador);

        frameMenu.setLocationRelativeTo(null);
        frameMenu.setVisible(true);
    }

    // Método auxiliar para manter a estética (cor, fonte, estilo)
    private static JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(Color.decode("#ce955b"));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuPrincipal::criarMenu);
    }
}