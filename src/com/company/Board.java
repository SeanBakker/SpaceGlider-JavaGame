package com.company;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

/*
Board class for all general game functions
 */

public class Board extends JPanel {

    //Variables
    public Timer timer;
    private String message = "null";
    private final String victory = "Victory!";
    private Player player;
    private Background background;
    public Asteroids[] asteroids;
    public Asteroids[] powerUp;
    private boolean inGame = true;
    private boolean gameInit = false;
    private final Random random = new Random();
    private final Stopwatch stopwatch = new Stopwatch();
    private final Stopwatch stopwatch2 = new Stopwatch();
    private int delay = 1000;
    private boolean firstAsteroid = true;
    private double asteroidMoveIntX = 3;
    private double asteroidMoveIntY = 1;
    private boolean startStopwatch = true;
    private boolean startGame = true;
    private int count = 0;
    private int wave = 1;
    private boolean newWave = false;
    private boolean first = true;
    private double newMidMin;
    private double newMidMax;
    private double playerMoveInt = 1;
    private boolean pressedButton = false;
    private boolean newAsteroid = false;
    private boolean newPowerUp = false;
    private boolean extraLife = false;
    private int lives = 3;
    private int powerType;
    private double powerMoveInt = 1.3;
    private float opacity = 0.0f;
    private int numMoveY = 2;
    private int pause = 0;
    private boolean restart = false;
    private JTextField waveText;
    private JButton pauseButton;
    private JButton startButton;
    private int extraLifeWave = 0;
    private int starWave = 0;
    private boolean star = false;
    private boolean starEffect = false;
    private boolean paintStar = false;
    private int starAmount = 0;
    private int lifeAmount = 0;

    public Board() {
        initBoard();
    }

    //Initialize Board properties
    private void initBoard() {

        addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(Features.WIDTH, Features.HEIGHT));
        setFocusable(true);
        setLayout(null);

        //Start Button
        startButton = new JButton("Start Game");
        startButton.setBounds(430,240,110,25);
        add(startButton);
        startButton.setVisible(true);
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusable(false);

        //Pause Button
        pauseButton = new JButton("Pause Game");
        pauseButton.setBounds(880,0,110,25);
        pauseButton.setVisible(true);
        pauseButton.setBackground(Color.BLACK);
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFocusable(false);

        //Wave TextBox
        waveText = new JTextField("Wave: " + wave,5);
        Font font = new Font("Arial Black", Font.BOLD, 10);
        waveText.setBounds(815,0,70,25);
        waveText.setFont(font);
        waveText.setOpaque(true);
        waveText.setBackground(Color.BLACK);
        waveText.setForeground(Color.white);
        waveText.setFocusable(false);
        waveText.setEditable(false);

        //Create timer for game cycle
        timer = new Timer(Features.PERIOD, new GameCycle());

        //Pause button action listener to stop/start timer when pressed
        pauseButton.addActionListener(e -> {
            pause++;
            if (pause % 2 != 0) {
                if (pressedButton){
                    timer.stop();
                }
            }
            else {
                if (pressedButton){
                    timer.start();
                }
            }
        });

        player = new Player();

        //Start button action listener to start game once button is pressed
        if (!pressedButton) {
            startButton.addActionListener(e -> {
                pressedButton = true;
                startGame = true;
                resetState();
                asteroids = new Asteroids[Features.N_OF_ROCKS];
                powerUp = new Asteroids[Features.N_OF_ROCKS];
                timer.start();
                gameInit();
                remove(startButton);
                add(pauseButton);
                add(waveText);
            });
        }
    }

    //Initialize asteroids and power-ups each round
    private void gameInit() {

        int k = 0;

        //For loops to set asteroid positions
        for (int i = 0; i < 10; i++) {

            for (int j = 0; j < 2; j++) {

                asteroids[k] = new Asteroids(j + 900, i * 40 + 5, newMidMin, newMidMax, false, powerType);
                asteroids[k].setDestroyed(false);
                asteroids[k].setMoving(false);
                asteroids[k].setMovingY(false);
                asteroids[k].setMovingUp(false);
                k++;
            }
        }

        k = 0;

        //For loops to have power-up replace asteroid when newPowerUp is true
        if (newPowerUp){
            for (int i = 0; i < 10; i++) {

                for (int j = 0; j < 2; j++) {

                    powerUp[k] = new Asteroids(j + 900, i * 40 + 5, newMidMin, newMidMax, newPowerUp, powerType);
                    powerUp[k].setDestroyed(false);
                    powerUp[k].setMoving(false);
                    powerUp[k].setHeart(false);
                    powerUp[k].setStar(false);
                    k++;
                }
            }
        }
        gameInit = true;
    }

    //CountDown method for displaying countdown at the beginning of the game
    private void countDown() throws Exception {

        if (count == 0 && !restart){
            message = "Welcome to Space Glider";
            restart = true;
        }
        //Allows countdown to replay after restart button is clicked
        if (pressedButton) {
            if (count == 1) {
                message = "3";
                Thread.sleep(500);
            }
            else if (count == 2) {
                message = "2";
                Thread.sleep(1000);
            }
            else if (count == 3) {
                message = "1";
                Thread.sleep(1000);
            }
            else if (count == 4) {
                message = "Wave 1 Starting";
                Thread.sleep(1000);
            }
            else if (count == 5) {
                startGame = false;
                Thread.sleep(1000);
            }
            count++;
        }
    }

    //NextWave method controls all required changes after each wave is complete
    private void nextWave() throws Exception {

        //Add extraLife power-up when wave equals extraLifeWave
        if (wave == extraLifeWave && !first){
            //Increase speed of power-ups after wave 5
            if (wave > 5){
                powerMoveInt = powerMoveInt + 1.3;
            }
            newPowerUp = true;
            extraLife = true;
            powerType = 0;
            lifeAmount++;
        }
        //Add star power-up when wave equals starWave
        else if (wave == starWave && !first){
            newPowerUp = true;
            star = true;
            powerType = 1;
            starAmount++;
        }
        //Set variables to false if there are no power-ups
        else {
            newPowerUp = false;
            extraLife = false;
            star = false;
        }
        //Pick new starWave after previous one has passed
        if (wave > starWave){
            pickWave(0);
        }
        //Pick new extraLifeWave after previous one has passed
        if (wave > extraLifeWave){
            pickWave(1);
        }
        //Change variables each wave
        if (!first) {
            if (wave % 2 == 0){
                numMoveY++;
            }
            if (wave <= 15){
                delay = delay - 45;
                asteroidMoveIntY = asteroidMoveIntY + 0.2;
                playerMoveInt = playerMoveInt + 0.2;
                player.moveInt = playerMoveInt;
                asteroidMoveIntX = (asteroidMoveIntX * 1.0) + 0.7;
                newMidMin = (newMidMin - 1);
                newMidMax = (newMidMax - 1);
            }
            newWave = false;
            Thread.sleep(2000);
            gameInit();
        }
        //set text for next wave starting
        else {
            message = "Wave " + wave + " Starting";
            waveText.setText("Wave: " + wave);
            first = false;
        }
    }

    //PaintComponent method for controlling the painting of images
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        //Run at beginning of game to display background/countDown
        if (startGame){
            drawBackground(g2d);
            try {
                countDown();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            paintText2D(g2d);
        }
        //In between waves repaint background and display nextWave text
        else if (newWave){
            drawBackground(g2d);
            try{
                nextWave();
            } catch (Exception e) {
                e.printStackTrace();
            }
            paintText2D(g2d);
        }
        //While in game drawObjects will paint images for game
        else if (inGame) {
            drawObjects(g2d);
            first = true;
        }
        else {
            drawBackground(g2d);
            paintText2D(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    //Draws background while in between waves or after victory
    private void drawBackground(Graphics2D g2d) {
        if (message.equals(victory)){
            background = new Background(100);
        }
        else {
            background = new Background(0);
        }
        g2d.drawImage(background.getImage(), (int) background.getX(), (int) background.getY(),
                (int) background.getImageWidth(), (int) background.getImageHeight(), this);
    }

    //Draws images for game
    private void drawObjects(Graphics2D g2d) {
        int j = 0;

        //Paint background for each wave
        background = new Background(wave);
        g2d.drawImage(background.getImage(), (int) background.getX(), (int) background.getY(),
                (int) background.getImageWidth(), (int) background.getImageHeight(), this);

        //Paint hearts for how many lives remain
        for (int i = 1; i <= lives; i++){
            background = new Background(101);
            g2d.drawImage(background.getImage(), i * 5 + ((int) background.getImageWidth() * (i-1)), 5,
                    (int) background.getImageWidth(), (int) background.getImageHeight(), this);
            j = i * 5 + ((int) background.getImageWidth() * (i-1));

        }

        //Timer for how long starEffect lasts / controls flashing effect of star
        if (starEffect) {
            if (stopwatch2.getElapsedTime() < 7000) {
                paintStar = true;
            }
            else if (stopwatch2.getElapsedTime() > 7500 && stopwatch2.getElapsedTime() < 8000) {
                paintStar = true;
            }
            else if (stopwatch2.getElapsedTime() > 8500 && stopwatch2.getElapsedTime() < 9000) {
                paintStar = true;
            }
            else if (stopwatch2.getElapsedTime() > 9500 && stopwatch2.getElapsedTime() < 10000){
                paintStar = true;
            }

            //Paint star power-up
            if (paintStar){
                background = new Background(102);
                g2d.drawImage(background.getImage(), j + ((int) background.getImageWidth()), 2,
                        (int) background.getImageWidth(), (int) background.getImageHeight(), this);
            }
            paintStar = false;
        }

        //Draw player spaceship
        g2d.drawImage(player.getImage(), (int) player.getX(), (int) player.getY(),
                (int) player.getImageWidth(), (int) player.getImageHeight(), this);

        //Paint asteroids
        for (int i = 0; i < Features.N_OF_ROCKS; i++) {

            //Set opacity of asteroids while moving
            if (!asteroids[i].isDestroyed() && asteroids[i].isMoving()){
                opacity = 1.0f;
            }
            //Set opacity of asteroids while not moving
            else if (!asteroids[i].isDestroyed() && !asteroids[i].isMoving()){
                opacity = 0.0f;
            }

            //Draw asteroids when not destroyed
            if (!asteroids[i].isDestroyed()) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2d.drawImage(asteroids[i].getImage(), (int) asteroids[i].getX(),
                        (int) asteroids[i].getY(), (int) asteroids[i].getImageWidth(),
                        (int) asteroids[i].getImageHeight(), this);

            }
        }

        //Repeat drawing of asteroids for power-ups
        if (newPowerUp && gameInit) {
            for (int i = 0; i < Features.N_OF_ROCKS; i++) {

                if (!powerUp[i].isDestroyed() && powerUp[i].isMoving()) {
                    opacity = 1.0f;
                } else if (!powerUp[i].isDestroyed() && !powerUp[i].isMoving()) {
                    opacity = 0.0f;
                }

                if (!powerUp[i].isDestroyed()) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.drawImage(powerUp[i].getImage(), (int) powerUp[i].getX(),
                            (int) powerUp[i].getY(), (int) powerUp[i].getImageWidth(),
                            (int) powerUp[i].getImageHeight(), this);

                }
            }
        }
    }

    //Display message in center of jframe
    private void paintText2D(Graphics2D g2d) {

        Font font = new Font("Arial Black", Font.BOLD, 30);
        FontMetrics fontMetrics = this.getFontMetrics(font);

        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(message,
                (Features.WIDTH - fontMetrics.stringWidth(message)) / 2,
                Features.HEIGHT / 2);
    }

    //Check for key input of player
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    //Move all objects during gameCycle / check their collision / repaint images
    private void doGameCycle() {
        player.move();
        asteroidMove();
        if (newPowerUp){
            powerUpMove();
        }
        checkCollision();
        repaint();
    }

    //Stop game timer and display restart button
    private void stopGame() {
        add(startButton);
        startButton.setVisible(true);
        startButton.setText("Restart");
        pressedButton = false;
        inGame = false;
        timer.stop();
    }

    //Check for collision with asteroids/power-ups
    private void checkCollision() {
        int waveAmount = 20;

        //Check if all asteroids have been destroyed
        for (int i = 0, j = 0; i < Features.N_OF_ROCKS; i++) {

            if (asteroids[i].isDestroyed()) {
                j++;
            }
            //Stop game if all asteroids are destroyed and player is on last wave
            if (j == Features.N_OF_ROCKS && wave >= waveAmount) {
                message = victory;
                stopGame();
            }
            //Begin next wave if all asteroids are destroyed, but player is not on last wave
            if (j == Features.N_OF_ROCKS && wave < waveAmount) {

                wave++;
                newWave = true;
                gameInit = false;
                firstAsteroid = true;

                //Reset All Asteroids
                for (int k = 0; k < Features.N_OF_ROCKS; k++){
                    asteroids[k].setDestroyed(false);
                    asteroids[k].setMovingY(false);
                }
            }
        }

        for (int i = 0; i < Features.N_OF_ROCKS; i++) {

            if (asteroids[i].isDestroyed()){
                asteroids[i].setX(Features.WIDTH);
            }

            //Check for collision between player and asteroids
            if ((player.getRect()).intersects(asteroids[i].getRect())) {

                if (!asteroids[i].isDestroyed()) {
                    asteroids[i].setDestroyed(true);
                }
                //Player looses a life if they do not have starEffect active
                if (!starEffect) {
                    if (lives - 1 > 0) {
                        lives--;
                    }
                    //Game stops if player has no more lives
                    else {
                        message = "Game Over";
                        stopGame();
                    }
                }
            }

            //Asteroids are destroyed when they reach the side edge
            if (asteroids[i].getRect().getX() == Features.SIDE_EDGE){
                asteroids[i].setDestroyed(true);
            }

        }

        //Check for collision between player and power-up
        if (newPowerUp && gameInit) {
            for (int i = 0; i < Features.N_OF_ROCKS; i++) {

                if (powerUp[i].isDestroyed()) {
                    powerUp[i].setX(Features.WIDTH);
                }

                if ((player.getRect()).intersects(powerUp[i].getRect()) && powerUp[i].isMoving()) {

                    //Player's lives increase if they collide with extraLife power-up
                    if (!powerUp[i].isDestroyed() && powerUp[i].isHeart()) {
                        powerUp[i].setDestroyed(true);
                        lives++;
                    }
                    //StarEffect is true if player collides with star power-up
                    else if (!powerUp[i].isDestroyed() && powerUp[i].isStar()){
                        powerUp[i].setDestroyed(true);
                        starEffect = true;
                        //stopwatch2 keeps track of time for star power-up
                        stopwatch2.start();
                    }
                }

                //Power-ups are destroyed when they reach the side edge
                if (powerUp[i].getRect().getX() == Features.SIDE_EDGE) {
                    powerUp[i].setDestroyed(true);
                }

            }
        }

        //Star power-up is no longer active after 10 seconds
        if (stopwatch2.getElapsedTime() >= 10000){
            stopwatch2.stop();
            starEffect = false;
        }

    }

    //Method for moving asteroids
    private void asteroidMove(){
        int firstDelay = 3000;

        if (startStopwatch){
            stopwatch.start();
            startStopwatch = false;
        }

        //Begin moving asteroid after delay of stopwatch
        if (stopwatch.getElapsedTime() >= delay && !firstAsteroid){
            newAsteroid = true;
        }
        else if (stopwatch.getElapsedTime() >= firstDelay && firstAsteroid){

            //Pick asteroid to move using random num
            for (int i = 0; i < numMoveY; i++) {
                int num = random.nextInt(Features.N_OF_ROCKS);
                asteroids[num].setMovingY(true);

                //Chance of asteroid moving up or down
                num = random.nextInt(Features.N_OF_ROCKS);
                if (num > Features.N_OF_ROCKS / 2) {
                    asteroids[num].setMovingUp(true);
                }
            }
            newAsteroid = true;
            firstAsteroid = false;
        }

        if (newAsteroid){

            //Set random asteroid moving in X direction when newAsteroid = true
            int num = random.nextInt(Features.N_OF_ROCKS);
            asteroids[num].setMoving(true);

            //Keep choosing new asteroid if previous has already been destroyed
            while (asteroids[num].isDestroyed()){
                num = random.nextInt(Features.N_OF_ROCKS);
                asteroids[num].setMoving(true);
            }

            stopwatch.stop();
            startStopwatch = true;
            newAsteroid = false;
        }

        //For loop to move asteroids
        for (int i = 0; i < Features.N_OF_ROCKS; i++) {

            //Move asteroids in Y direction
            if (asteroids[i].isMovingY() && asteroids[i].isMoving()) {
                //Move asteroid up until top border
                if (asteroids[i].isMovingUp() && asteroids[i].getY() - asteroidMoveIntY >= 5) {
                    asteroids[i].setY(asteroids[i].getY() - asteroidMoveIntY);
                }
                //Change direction after hitting top border
                else if (asteroids[i].isMovingUp()){
                    asteroids[i].setMovingUp(false);
                }

                //Move asteroid down until bottom border
                if (!asteroids[i].isMovingUp() && asteroids[i].getY() + asteroidMoveIntY <= Features.HEIGHT - 40) {
                    asteroids[i].setY(asteroids[i].getY() + asteroidMoveIntY);
                }
                //Change direction after hitting bottom border
                else if (!asteroids[i].isMovingUp()){
                    asteroids[i].setMovingUp(true);
                }
            }

            //Move asteroids forward in X direction until the side edge
            if (asteroids[i].getX() - asteroidMoveIntX < Features.SIDE_EDGE) {
                asteroids[i].setDestroyed(true);
            }
            else if (asteroids[i].isMoving()) {
                asteroids[i].setX(asteroids[i].getX() - asteroidMoveIntX);
            }
        }
    }

    //Method for moving power-ups
    private void powerUpMove() {

        if (gameInit) {
            //Set random asteroid to extraLife power-up
            if (extraLife) {
                int num = random.nextInt(Features.N_OF_ROCKS);
                powerUp[num].setMoving(true);
                powerUp[num].setHeart(true);
                extraLife = false;
            }
            //Set random asteroid to star power-up
            if (star) {
                int num = random.nextInt(Features.N_OF_ROCKS);
                powerUp[num].setMoving(true);
                powerUp[num].setStar(true);
                star = false;
            }

            //Move power-ups until side edge
            for (int i = 0; i < Features.N_OF_ROCKS; i++) {
                if (powerUp[i].getX() - powerMoveInt < Features.SIDE_EDGE) {
                    powerUp[i].setDestroyed(true);
                } else if (powerUp[i].isMoving()) {
                    powerUp[i].setX(powerUp[i].getX() - powerMoveInt);
                }
            }
        }
    }

    //Method for randomizing power-up waves
    private void pickWave(int powerType) {
        int num = random.nextInt(20);
        int wave;
        int powerMin = 6;
        int powerMax = 12;

        //Pick wave based on random num
        if (num < powerMin) {
            wave = 3;
        }
        else if (num >= powerMin + 1 && num <= powerMax) {
            wave = 4;
        }
        else {
            wave = 5;
        }

        //Choose power-up type based on powerType
        if (powerType == 0) {
            //Add to starWave to have power-up arrive in later waves
            if (starAmount == 0) {
                starWave = wave + 10;
            } else if (starAmount == 1) {
                starWave = wave + 13;
            }
        }
        else if (powerType == 1) {
            if (lifeAmount == 0) {
                extraLifeWave = wave;
            }
            //Add to extraLifeWave to have power-up arrive in later waves
            else if (lifeAmount == 1) {
                extraLifeWave = wave + 5;
            } else if (lifeAmount == 2) {
                extraLifeWave = wave + 12;
            }
        }
        //Check for power-ups on the same wave
        if (extraLifeWave == starWave) {
            extraLifeWave++;
        }
    }

    //Method for resetting state of all variables
    private void resetState() {
        player.resetState();
        delay = 1000;
        wave = 1;
        waveText.setText("Wave: " + wave);
        lives = 3;
        count = 0;
        playerMoveInt = 1;
        numMoveY = 2;
        asteroidMoveIntX = 3;
        asteroidMoveIntY = 1;
        pause = 0;
        extraLifeWave = 0;
        starWave = 0;
        first = true;
        startGame = true;
        startStopwatch = true;
        inGame = true;
        gameInit = false;
        firstAsteroid = true;
        newMidMin = 0;
        newMidMax = 0;
        powerMoveInt = 1.3;
        starEffect = false;
    }

}
