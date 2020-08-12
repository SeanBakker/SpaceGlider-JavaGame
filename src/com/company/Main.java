package com.company;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        initUI();
    }

    private void initUI() {

        add(new Board());
        setTitle("Space Glider");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(350,250);
        setResizable(false);
        setVisible(true);
        pack();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Main game = new Main();
            game.setVisible(true);

        });
    }
}
