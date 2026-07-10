package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.table.JTableHeader;

public class TemaRosa {

    private static final Color rosaBebe =
            new Color(255, 228, 238);

    private static final Color rosaBotao =
            new Color(255, 192, 203);

    private static final Color preto =
            Color.BLACK;

    private static final Color branco =
            Color.WHITE;

    public static void aplicar(Component componente) {

        if (componente instanceof JPanel) {
            componente.setBackground(rosaBebe);
        }

        if (componente instanceof JLabel) {
            componente.setForeground(preto);
            componente.setFont(new Font("Arial", Font.BOLD, 13));
        }

        if (componente instanceof JButton) {
            JButton botao =
                    (JButton) componente;

            botao.setBackground(rosaBotao);
            botao.setForeground(preto);
            botao.setFocusPainted(false);
            botao.setFont(new Font("Arial", Font.BOLD, 13));
        }

        if (componente instanceof JTextField) {
            JTextField campo =
                    (JTextField) componente;

            campo.setBackground(branco);
            campo.setForeground(preto);
            campo.setFont(new Font("Arial", Font.PLAIN, 13));
        }

        if (componente instanceof JTextArea) {
            JTextArea area =
                    (JTextArea) componente;

            area.setBackground(branco);
            area.setForeground(preto);
            area.setFont(new Font("Arial", Font.PLAIN, 13));
        }

        if (componente instanceof JComboBox) {
            JComboBox combo =
                    (JComboBox) componente;

            combo.setBackground(branco);
            combo.setForeground(preto);
            combo.setFont(new Font("Arial", Font.PLAIN, 13));
        }

        if (componente instanceof JScrollPane) {
            JScrollPane scrollPane =
                    (JScrollPane) componente;

            scrollPane.setBackground(rosaBebe);

            JViewport viewport =
                    scrollPane.getViewport();

            if (viewport != null) {
                viewport.setBackground(branco);
            }
        }

        if (componente instanceof JTable) {
            aplicarTabela((JTable) componente);
        }

        if (componente instanceof Container) {

            Component[] componentes =
                    ((Container) componente).getComponents();

            for (int i = 0; i < componentes.length; i++) {
                aplicar(componentes[i]);
            }
        }
    }

    public static void aplicarTabela(JTable tabela) {

        tabela.setBackground(branco);
        tabela.setForeground(preto);
        tabela.setGridColor(rosaBotao);
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));
        tabela.setRowHeight(24);

        JTableHeader cabecalho =
                tabela.getTableHeader();

        if (cabecalho != null) {
            cabecalho.setBackground(rosaBotao);
            cabecalho.setForeground(preto);
            cabecalho.setFont(new Font("Arial", Font.BOLD, 12));
        }
    }
}
