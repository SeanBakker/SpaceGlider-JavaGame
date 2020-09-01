package com.company;

import javax.swing.*;

/*
Background class for setting and loading background images
 */

public class Background extends SetValue{

    //Constructor to initialize background (uses wave parameter to output specific wave image)
    public Background(int wave) {
        initBackground(wave);
    }

    //Initialize background image
    public void initBackground(int wave) {
        loadImage(wave); //Load background image using wave number
        getImageDimensions(); //Get image dimensions for background
        resetState(); //Reset state of background
    }

    //Load background image for each wave
    private void loadImage(int wave) {
        ImageIcon ii; //Create new image icon

        //Set corresponding background to wave num
        switch (wave){
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
            case 11:
                ii = new ImageIcon("src/resources/wave11.jpg");
                break;
            case 12:
                ii = new ImageIcon("src/resources/wave12.jpg");
                break;
            case 13:
                ii = new ImageIcon("src/resources/wave13.jpg");
                break;
            case 14:
                ii = new ImageIcon("src/resources/wave14.jpg");
                break;
            case 15:
                ii = new ImageIcon("src/resources/wave15.jpg");
                break;
            case 16:
                ii = new ImageIcon("src/resources/wave16.jpg");
                break;
            case 17:
                ii = new ImageIcon("src/resources/wave17.jpg");
                break;
            case 18:
                ii = new ImageIcon("src/resources/wave18.jpg");
                break;
            case 19:
                ii = new ImageIcon("src/resources/wave19.jpg");
                break;
            case 20:
                ii = new ImageIcon("src/resources/wave20.jpg");
                break;
            case 100: //Use case 100 for printing victory gif
                ii = new ImageIcon("src/resources/victorygif.gif");
                break;
            case 101: //Use case 101 for extraLife images in top left
                ii = new ImageIcon("src/resources/extraLife.png");
                break;
            case 102: //Use case 102 for star image in top left
                ii = new ImageIcon("src/resources/star.png");
                break;
            default: //Set default background to waiting screen
                ii = new ImageIcon("src/resources/wave0.jpg");
                break;
        }
        image = ii.getImage(); //Set "image" to selected background image
    }

    //Reset background state
    private void resetState() {
        x = Features.INIT_BACKGROUND_X; //Reset background x value
        y = Features.INIT_BACKGROUND_Y; //Reset background y value
    }
}
