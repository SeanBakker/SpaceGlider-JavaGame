package com.company;

import javax.swing.*;
import java.util.Random;

public class Asteroids extends SetValue {

    private boolean destroyed;
    private boolean moving;
    private boolean movingY;
    private boolean movingUp;
    private boolean heart;
    private boolean star;
    private ImageIcon ii = new ImageIcon("src/resources/smallAsteroid.png");
    public double min = 1;
    public double midMin = 25;
    public double midMax = 40;
    public double max = 50;

    public Asteroids(int x, int y, double newMidMin, double newMidMax, boolean powerUp, int powerType) {
        setImagePercentage(newMidMin,newMidMax);
        initAsteroid(x, y, powerUp, powerType);
    }

    private void initAsteroid(int x, int y, boolean powerUp, int powerType) {

        this.x = x;
        this.y = y;

        destroyed = false;
        moving = false;
        movingY = false;
        movingUp = false;

        loadImage(powerUp, powerType);
        getImageDimensions();
    }

    private void setImagePercentage(double newMidMin, double newMidMax){
        midMin = midMin + newMidMin;
        midMax = midMax + newMidMax;
    }

    private void loadImage(boolean powerUp, int powerType) {

        if (powerUp && powerType == 0){
            ii = new ImageIcon("src/resources/extraLife.png");
        }
        else if (powerUp && powerType == 1){
            ii = new ImageIcon("src/resources/star.png");
        }
        else {
            double random = new Random().nextDouble();
            double num = min + (random * (max - min));

            if (num >= min && num < midMin){
                ii = new ImageIcon("src/resources/smallAsteroid.png");
            }
            if (num >= midMin && num <= midMax){
                ii = new ImageIcon("src/resources/midAsteroid.png");
            }
            if (num > midMax && num <= max){
                ii = new ImageIcon("src/resources/bigAsteroid.png");
            }
        }
        image = ii.getImage();
    }

    boolean isDestroyed() {
        return destroyed;
    }

    void setDestroyed(boolean val) {
        destroyed = val;
    }

    boolean isMoving() {
        return moving;
    }

    void setMoving(boolean val) {
        moving = val;
    }

    boolean isMovingY() {
        return movingY;
    }

    void setMovingY(boolean val) {
        movingY = val;
    }

    boolean isMovingUp() {
        return movingUp;
    }

    void setMovingUp(boolean val) {
        movingUp = val;
    }

    //PowerUps

    boolean isHeart() {
        return heart;
    }

    void setHeart(boolean val) {
        heart = val;
    }

    boolean isStar() {
        return star;
    }

    void setStar(boolean val) {
        star = val;
    }

}
