package com.company;

import java.awt.Image;
import java.awt.geom.Rectangle2D;

/*
SetValue class for dealing with image dimensions and setting/getting x and y values
 */

public class SetValue {

    //Variables
    double x;
    double y;
    double imageWidth;
    double imageHeight;
    Image image;

    //Set x value
    protected void setX(double x) {
        this.x = x;
    }

    //Get x value
    double getX() {
        return x;
    }

    //Set y value
    protected void setY(double y) {
        this.y = y;
    }

    //Get y value
    double getY() {
        return y;
    }

    //Get image width
    double getImageWidth() {
        return imageWidth;
    }

    //Get image height
    double getImageHeight() {
        return imageHeight;
    }

    //Get image
    Image getImage() {
        return image;
    }

    //Get rectangle around image
    Rectangle2D getRect() {
        return new Rectangle2D.Double(x, y,
                image.getWidth(null), image.getHeight(null));
    }

    //Get image dimensions
    void getImageDimensions() {
        imageWidth = image.getWidth(null);
        imageHeight = image.getHeight(null);
    }

}
