package com.company;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player extends SetValue  {

    public double dx;
    public double dy;
    public double moveInt = 1;

    public Player() {
        initSpaceship();
    }

    private void initSpaceship() {
        loadImage();
        getImageDimensions();
        resetState();
    }

    private void loadImage() {
        ImageIcon ii = new ImageIcon("src/resources/spaceship.png");
        image = ii.getImage();
    }

    void move() {

        x += dx;
        y += dy;

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

    void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {

            dx = -moveInt;
        }

        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {

            dx = moveInt;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {

            dy = -moveInt;
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {

            dy = moveInt;
        }
    }

    void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
            dy = 0;
        }
    }

    private void resetState() {
        x = Features.INIT_PLAYER_X;
        y = Features.INIT_PLAYER_Y;
    }
}