package com.company;

import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class SetValue {

    double x;
    double y;
    double imageWidth;
    double imageHeight;
    Image image;

    protected void setX(double x) {

        this.x = x;
    }

    double getX() {

        return x;
    }

    protected void setY(double y) {

        this.y = y;
    }

    double getY() {

        return y;
    }

    double getImageWidth() {

        return imageWidth;
    }

    double getImageHeight() {

        return imageHeight;
    }

    Image getImage() {

        return image;
    }

    Rectangle2D getRect() {

        return new Rectangle2D.Double(x, y,
                image.getWidth(null), image.getHeight(null));
    }

    void getImageDimensions() {

        imageWidth = image.getWidth(null);
        imageHeight = image.getHeight(null);
    }
}
