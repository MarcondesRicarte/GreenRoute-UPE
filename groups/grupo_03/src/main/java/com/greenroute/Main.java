package com.greenroute;

import javax.swing.SwingUtilities;
import com.greenroute.view.TelaPrincipal;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal();
        });

    }

}