package com.company;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/*
Player class for initializing player spaceship
 */

public class Player extends SetValue  {

    //Variables
    public double dx; //Variable for change in x of player
    public double dy; //Variable for change in y of player
    public double moveInt = 1; //Variable for speed of player

    //Constructor to initialize player
    public Player() {
        initSpaceship();
    }

    //Initialize player spaceship
    private void initSpaceship() {
        loadImage(); //Load image of player
        getImageDimensions(); //Get image dimensions of player
        resetState(); //Reset state of player
    }

    //Load image for spaceship
    private void loadImage() {
        ImageIcon ii = new ImageIcon("src/resources/spaceship.png"); //Load image spaceship
        image = ii.getImage(); //Set "image" to new selected image
    }

    //Move player according to user input
    void move() {

        //set new player x/y equal to x/y plus change in x/y
        x += dx; //Add change in x
        y += dy; //Add change in y

        //Limit player to movement within the board in all directions
        if (x <= 0) {
            x = 0; //Player is restricted to only positive values for x
        }
        if (x >= Features.WIDTH - imageWidth) {
            x = Features.WIDTH - imageWidth; //Player is restricted by right border
        }
        if (y <= 0) {
            y = 0; //Player is restricted to only positive values for y
        }
        if (y >= Features.HEIGHT - imageHeight) {
            y = Features.HEIGHT - imageHeight; //Player is restricted by bottom border
        }
    }

    //Check key input from player
    void keyPressed(KeyEvent e) {

        int key = e.getKeyCode(); //Integer key gives key code for player input

        //Negative for left
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            dx = -moveInt; //Move player left (negative) when "A" or left arrow key are pressed
        }
        //Positive for right
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            dx = moveInt; //Move player right (positive) when "D" or right arrow key are pressed
        }
        //Negative for down
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            dy = -moveInt; //Move player up (negative) when "W" or up arrow key are pressed
        }
        //Positive for up
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            dy = moveInt; //Move player down (positive) when "S" or down arrow key are pressed
        }
    }

    //Reset dx/dy when key is released
    void keyReleased(KeyEvent e) {

        int key = e.getKeyCode(); //Integer key gives key code for player input

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
            dx = 0; //Set change in x to zero when any right or left keys are released
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
            dy = 0; //Set change in y to zero when any up or down keys are released
        }
    }

    //Reset state of player
    public void resetState() {
        x = Features.INIT_PLAYER_X; //Reset player x value
        y = Features.INIT_PLAYER_Y; //Reset player y value
        moveInt = 1; //Reset speed of player
    }
}