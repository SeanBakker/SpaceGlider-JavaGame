package com.company;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/*
Player class for initializing player spaceship
 */

public class Player extends SetValue  {

    //Variables
    public double dx;
    public double dy;
    public double moveInt = 1;

    public Player() {
        initSpaceship();
    }

    //Initialize player spaceship
    private void initSpaceship() {
        loadImage();
        getImageDimensions();
        resetState();
    }

    //Load image for spaceship
    private void loadImage() {
        ImageIcon ii = new ImageIcon("src/resources/spaceship.png");
        image = ii.getImage();
    }

    //Move player according to user input
    void move() {

        //set new player x/y equal to x/y plus change in x/y
        x += dx;
        y += dy;

        //Limit player to movement within the board in all directions
        if (x <= 0) {

            x = 0;
        }
        if (x >= Features.WIDTH - imageWidth) {

            x = Features.WIDTH - imageWidth;
        }
        if (y <= 0) {

            y = 0;
        }
        if (y >= Features.HEIGHT - imageHeight) {

            y = Features.HEIGHT - imageHeight;
        }
    }

    //Check key input from player
    void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        //Negative for left
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {

            dx = -moveInt;
        }
        //Positive for right
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {

            dx = moveInt;
        }
        //Negative for down
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {

            dy = -moveInt;
        }
        //Positive for up
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {

            dy = moveInt;
        }
    }

    //Reset dx/dy when key is released
    void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
            dy = 0;
        }
    }

    //Reset state of player
    public void resetState() {
        x = Features.INIT_PLAYER_X;
        y = Features.INIT_PLAYER_Y;
        moveInt = 1;
    }
}