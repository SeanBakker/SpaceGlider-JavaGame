package com.company;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    //Constructor to initialize Main GUI
    public Main() {
        initUI();
    }

    //Initialize board and Jframe
    private void initUI() {

        add(new Board()); //Add board to Jframe
        setTitle("Space Glider"); //Set title
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Set program to close on exiting Jframe
        setLocation(50,100); //Set location of Jframe on screen
        setResizable(false); //Set Jframe to now be resizable
        setVisible(true); //Set Jframe to visible
        pack(); //Sizes all components of Jframe
    }

    //Run game
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Main game = new Main(); //Create new constructor for Main
            game.setVisible(true); //Set game to visible

        });
    }
}


/*

   * CREATED BY SEAN BAKKER *

 */
