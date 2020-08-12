package com.company;

import javax.swing.*;

public class Background extends SetValue{

    private ImageIcon ii = new ImageIcon("src/resources/wave0.jpg");

    public Background(int wave) {
        initBackground(wave);
    }

    public void initBackground(int wave) {
        loadImage(wave);
        getImageDimensions();
        resetState();
    }

    private void loadImage(int wave) {

        switch (wave){
            case 0:
                ii = new ImageIcon("src/resources/wave0.jpg");
                break;
            case 1:
                ii = new ImageIcon("src/resources/wave1.jpg");
                break;
            case 2:
                ii = new ImageIcon("src/resources/wave2.jpg");
                break;
            case 3:
                ii = new ImageIcon("src/resources/wave3.jpg");
                break;
            case 4:
                ii = new ImageIcon("src/resources/wave4.jpg");
                break;
            case 5:
                ii = new ImageIcon("src/resources/wave5.jpg");
                break;
            case 6:
                ii = new ImageIcon("src/resources/wave6.jpg");
                break;
            case 7:
                ii = new ImageIcon("src/resources/wave7.jpg");
                break;
            case 8:
                ii = new ImageIcon("src/resources/wave8.jpg");
                break;
            case 9:
                ii = new ImageIcon("src/resources/wave9.jpg");
                break;
            case 10:
                ii = new ImageIcon("src/resources/wave10.jpg");
                break;
            case 100:
                ii = new ImageIcon("src/resources/victory.jpg");
                break;
            case 101:
                ii = new ImageIcon("src/resources/extraLife.png");
                break;
            default:
                ii = new ImageIcon("src/resources/wave0.jpg");
                break;
        }
        image = ii.getImage();
    }

    private void resetState() {
        x = Features.INIT_BACKGROUND_X;
        y = Features.INIT_BACKGROUND_Y;
    }
}
