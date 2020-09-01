package com.company;

import javax.swing.*;
import java.util.Random;

/*
Asteroids class for initializing asteroids and power-ups
 */

public class Asteroids extends SetValue {

    //Variables
    private boolean destroyed; //Used when asteroid is destroyed
    private boolean moving; //Used when asteroid is moving
    private boolean movingY; //Used when asteroid is moving in Y direction
    private boolean movingUp; //Used when asteroid is moving upwards
    private boolean heart; //Used when asteroid is a heart power-up
    private boolean star; //Used when asteroid is a star power-up
    private ImageIcon ii = new ImageIcon("src/resources/smallAsteroid.png"); //Set default image icon for asteroid images
    //Values for percentage of which asteroid will get picked (small, medium, big)
    public double min = 1;
    public double midMin = 25;
    public double midMax = 40;
    public double max = 50;

    //Constructor to initialize asteroids each round
    public Asteroids(int x, int y, double newMidMin, double newMidMax, boolean powerUp, int powerType) {
        setImagePercentage(newMidMin,newMidMax); //Set new image percentages using new parameters
        initAsteroid(x, y, powerUp, powerType); //Initialize asteroids using parameters of x/y values and whether its a power-up/which type of power-up
    }

    //Initialize asteroids
    private void initAsteroid(int x, int y, boolean powerUp, int powerType) {

        this.x = x; //Set x value of asteroid
        this.y = y; //Set y value of asteroid

        destroyed = false; //Set asteroid not destroyed
        moving = false; //Set asteroid not moving
        movingY = false; //Set asteroid not moving in Y direction
        movingUp = false; //Set asteroid not moving upwards

        loadImage(powerUp, powerType);  //Load image for asteroid using power-up information
        getImageDimensions(); //Get image dimensions for asteroid using size of image
    }

    //Set new percentage for image sizes
    private void setImagePercentage(double newMidMin, double newMidMax){
        midMin = midMin + newMidMin; //Set new midMin percentage
        midMax = midMax + newMidMax; //Set new midMax percentage
    }

    //Load images for asteroids & power-ups
    private void loadImage(boolean powerUp, int powerType) {

        //Load power-up images based on powerType
        if (powerUp && powerType == 0){ //PowerType == 0 loads extraLife image
            ii = new ImageIcon("src/resources/extraLife.png");
        }
        else if (powerUp && powerType == 1){ //PowerType == 1 loads star image
            ii = new ImageIcon("src/resources/star.png");
        }
        //Load asteroids if there are no power-ups
        else {
            double random = new Random().nextDouble(); //Create random number generator
            double num = min + (random * (max - min)); //Pick random number between bounds of max and min

            //Random num gives chance for random sizes of asteroids
            if (num >= min && num < midMin){ //Smaller num gives smallAsteroid image
                ii = new ImageIcon("src/resources/smallAsteroid.png");
            }
            if (num >= midMin && num <= midMax){ //Medium num gives midAsteroid image
                ii = new ImageIcon("src/resources/midAsteroid.png");
            }
            if (num > midMax && num <= max){ //Large num gives bigAsteroid image
                ii = new ImageIcon("src/resources/bigAsteroid.png");
            }
        }
        image = ii.getImage(); //Set "image" to new image that was selected for asteroid
    }

    //Methods for setting state of asteroids
    //Check if asteroid is destroyed
    boolean isDestroyed() {
        return destroyed;
    }
    //Set asteroid to destroyed
    void setDestroyed(boolean val) {
        destroyed = val;
    }
    //Check if asteroid is moving
    boolean isMoving() {
        return moving;
    }
    //Set asteroid to moving
    void setMoving(boolean val) {
        moving = val;
    }
    //Check if asteroid is moving in Y direction
    boolean isMovingY() {
        return movingY;
    }
    //Set asteroid to moving in Y direction
    void setMovingY(boolean val) {
        movingY = val;
    }
    //Check if asteroid is moving upwards
    boolean isMovingUp() {
        return movingUp;
    }
    //Set asteroid to moving upwards
    void setMovingUp(boolean val) {
        movingUp = val;
    }

    //Methods for setting state of power-ups
    //Check if asteroid is a heart power-up
    boolean isHeart() {
        return heart;
    }
    //Set asteroid to a heart power-up
    void setHeart(boolean val) {
        heart = val;
    }
    //Check if asteroid is a star power-up
    boolean isStar() {
        return star;
    }
    //Set asteroid to a star power-up
    void setStar(boolean val) {
        star = val;
    }

}
