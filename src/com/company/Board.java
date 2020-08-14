package com.company;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Board extends JPanel {

    //Variables
    public Timer timer;
    private String message = "null";
    private String victory = "Victory";
    private String gameOver = "Game Over";
    private Player player;
    private Background background;
    public Asteroids[] asteroids;
    public Asteroids[] powerUp;
    private boolean inGame = true;
    private boolean gameInit = false;
    private Random random = new Random();
    private Stopwatch stopwatch = new Stopwatch();
    private int delay = 800;
    private int firstDelay = 3000;
    private boolean firstAsteroid = true;
    private double asteroidMoveIntX = 3;
    private double asteroidMoveIntY = 1;
    private boolean startStopwatch = true;
    private boolean startGame = true;
    private int count = 0;
    private int wave = 1;
    private int waveAmount = 10;
    private boolean newWave = false;
    private boolean first = true;
    private double newMidMin;
    private double newMidMax;
    private double playerMoveInt = 1;
    private boolean pressedButton = false;
    private boolean newAsteroid = false;
    private boolean newPowerUp = false;
    private boolean extraLife = false;
    private int lives = 2;
    private int powerType;
    private double powerMoveInt = 1.3;
    private float opacity = 0.0f;
    private int numMoveY = 2;
    private int pause = 0;
    private boolean restart = false;
    private JTextField waveText;
    private JButton pauseButton;
    private JButton startButton;
    private int powerWave = 0;
    private int powerWave2 = 0;

    public Board() {
        initBoard();
    }

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

        timer = new Timer(Features.PERIOD, new GameCycle());

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

    private void gameInit() {

        int k = 0;

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

        if (newPowerUp){
            for (int i = 0; i < 10; i++) {

                for (int j = 0; j < 2; j++) {

                    powerUp[k] = new Asteroids(j + 900, i * 40 + 5, newMidMin, newMidMax, newPowerUp, powerType);
                    powerUp[k].setDestroyed(false);
                    powerUp[k].setMoving(false);
                    k++;
                }
            }
        }
        gameInit = true;
    }

    private void countDown() throws Exception {

        if (count == 0 && !restart){
            message = "Welcome to Space Glider";
            restart = true;
        }
        if (pressedButton) {
            if (count == 1) {
                message = "3";
                Thread.sleep(500);

                powerWave2 = random.nextInt(20);
                pickWave(powerWave2, 5,10);
                powerWave2 = random.nextInt(20);
                pickWave(powerWave2,3,8);
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

    private void nextWave() throws Exception{

        if (wave == powerWave || wave == powerWave2){
            if (wave == powerWave2){
                powerMoveInt = powerMoveInt + 2;
            }
            newPowerUp = true;
            extraLife = true;
            powerType = 0;
        }
        else {
            newPowerUp = false;
            extraLife = false;
        }
        if (!first) {
            if (wave % 2 == 0){
                numMoveY++;
            }
            newWave = false;
            delay = delay - 45;
            asteroidMoveIntY = asteroidMoveIntY + 0.2;
            playerMoveInt = (playerMoveInt + 0.2);
            player.moveInt = playerMoveInt;
            asteroidMoveIntX = (asteroidMoveIntX * 1.1) + 0.8;
            newMidMin = (newMidMin - 2);
            newMidMax = (newMidMax - 3);
            Thread.sleep(2000);
            gameInit();
        }
        else {
            message = "Wave " + wave + " Starting";
            waveText.setText("Wave: " + wave);
            first = false;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

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
        else if (newWave){
            drawBackground(g2d);
            try{
                nextWave();
            } catch (Exception e) {
                e.printStackTrace();
            }
            paintText2D(g2d);
        }
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

    private void drawBackground(Graphics2D g2d) {
        if (message.equals(victory)){
            background = new Background(100);
            g2d.drawImage(background.getImage(), (int) background.getX(), (int) background.getY(),
                    (int) background.getImageWidth(), (int) background.getImageHeight(), this);
        }
        else {
            background = new Background(0);
            g2d.drawImage(background.getImage(), (int) background.getX(), (int) background.getY(),
                    (int) background.getImageWidth(), (int) background.getImageHeight(), this);
        }
    }

    private void drawObjects(Graphics2D g2d) {

        background = new Background(wave);
        g2d.drawImage(background.getImage(), (int) background.getX(), (int) background.getY(),
                (int) background.getImageWidth(), (int) background.getImageHeight(), this);

        for (int i = 1; i <= lives; i++){
            background = new Background(101);
            g2d.drawImage(background.getImage(), i * 5 + ((int) background.getImageWidth() * (i-1)), 5,
                    (int) background.getImageWidth(), (int) background.getImageHeight(), this);

        }

        g2d.drawImage(player.getImage(), (int) player.getX(), (int) player.getY(),
                (int) player.getImageWidth(), (int) player.getImageHeight(), this);

        for (int i = 0; i < Features.N_OF_ROCKS; i++) {

            if (!asteroids[i].isDestroyed() && asteroids[i].isMoving()){
                opacity = 1.0f;
            }
            else if (!asteroids[i].isDestroyed() && !asteroids[i].isMoving()){
                opacity = 0.0f;
            }

            if (!asteroids[i].isDestroyed()) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2d.drawImage(asteroids[i].getImage(), (int) asteroids[i].getX(),
                        (int) asteroids[i].getY(), (int) asteroids[i].getImageWidth(),
                        (int) asteroids[i].getImageHeight(), this);

            }
        }

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

    private void paintText2D(Graphics2D g2d) {

        Font font = new Font("Arial Black", Font.BOLD, 30);
        FontMetrics fontMetrics = this.getFontMetrics(font);

        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(message,
                (Features.WIDTH - fontMetrics.stringWidth(message)) / 2,
                Features.HEIGHT / 2);
    }

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

    private void doGameCycle() {
        player.move();
        asteroidMove();
        if (newPowerUp){
            powerUpMove();
        }
        checkCollision();
        repaint();
    }

    private void stopGame() {
        add(startButton);
        startButton.setVisible(true);
        startButton.setText("Restart");
        pressedButton = false;
        inGame = false;
        timer.stop();
    }

    private void checkCollision() {

        //Check if all asteroids have been destroyed
        for (int i = 0, j = 0; i < Features.N_OF_ROCKS; i++) {

            if (asteroids[i].isDestroyed()) {
                j++;
            }
            if (j == Features.N_OF_ROCKS && wave >= waveAmount) {

                message = victory;
                stopGame();
            }
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

            if ((player.getRect()).intersects(asteroids[i].getRect())) {

                if (!asteroids[i].isDestroyed()) {
                    asteroids[i].setDestroyed(true);
                }
                if (lives - 1 > 0){
                    lives--;
                }
                else {
                    message = gameOver;
                    stopGame();
                }
            }

            if (asteroids[i].getRect().getX() == Features.SIDE_EDGE){
                asteroids[i].setDestroyed(true);
            }

        }

        if (newPowerUp && gameInit) {
            for (int i = 0; i < Features.N_OF_ROCKS; i++) {

                if (powerUp[i].isDestroyed()) {
                    powerUp[i].setX(Features.WIDTH);
                }

                if ((player.getRect()).intersects(powerUp[i].getRect()) && powerUp[i].isMoving()) {

                    if (!powerUp[i].isDestroyed()) {
                        powerUp[i].setDestroyed(true);
                        lives++;
                    }
                }

                if (powerUp[i].getRect().getX() == Features.SIDE_EDGE) {
                    powerUp[i].setDestroyed(true);
                }

            }
        }
    }

    private void asteroidMove(){

        if (startStopwatch){
            stopwatch.start();
            startStopwatch = false;
        }

        //After delay, begin moving first asteroid
        if (stopwatch.getElapsedTime() >= delay && !firstAsteroid){
            newAsteroid = true;
        }
        else if (stopwatch.getElapsedTime() >= firstDelay && firstAsteroid){

            for (int i = 0; i < numMoveY; i++) {
                int num = random.nextInt(Features.N_OF_ROCKS);
                asteroids[num].setMovingY(true);

                num = random.nextInt(Features.N_OF_ROCKS);
                if (num > Features.N_OF_ROCKS / 2) {
                    asteroids[num].setMovingUp(true);
                }
            }
            newAsteroid = true;
            firstAsteroid = false;
        }

        if (newAsteroid){

            int num = random.nextInt(Features.N_OF_ROCKS);
            asteroids[num].setMoving(true);

            while (asteroids[num].isDestroyed()){
                num = random.nextInt(Features.N_OF_ROCKS);
                asteroids[num].setMoving(true);
            }

            stopwatch.stop();
            startStopwatch = true;
            newAsteroid = false;
        }

        for (int i = 0; i < Features.N_OF_ROCKS; i++) {

            if (asteroids[i].isMovingY() && asteroids[i].isMoving()) {
                if (asteroids[i].isMovingUp() && asteroids[i].getY() - asteroidMoveIntY >= 5) {
                    asteroids[i].setY(asteroids[i].getY() - asteroidMoveIntY);
                }
                else if (asteroids[i].isMovingUp()){
                    asteroids[i].setMovingUp(false);
                }

                if (!asteroids[i].isMovingUp() && asteroids[i].getY() + asteroidMoveIntY <= Features.HEIGHT - 40) {
                    asteroids[i].setY(asteroids[i].getY() + asteroidMoveIntY);
                }
                else if (!asteroids[i].isMovingUp()){
                    asteroids[i].setMovingUp(true);
                }
            }

            if (asteroids[i].getX() - asteroidMoveIntX < Features.SIDE_EDGE) {
                asteroids[i].setDestroyed(true);
            }
            else if (asteroids[i].isMoving()) {
                asteroids[i].setX(asteroids[i].getX() - asteroidMoveIntX);
            }
        }
    }

    private void powerUpMove() {

        if (gameInit) {
            if (extraLife) {
                int num = random.nextInt(Features.N_OF_ROCKS);
                powerUp[num].setMoving(true);
                extraLife = false;
            }

            for (int i = 0; i < Features.N_OF_ROCKS; i++) {
                if (powerUp[i].getX() - powerMoveInt < Features.SIDE_EDGE) {
                    powerUp[i].setDestroyed(true);
                } else if (powerUp[i].isMoving()) {
                    powerUp[i].setX(powerUp[i].getX() - powerMoveInt);
                }
            }
        }
    }

    private void pickWave(int num, int midMin, int midMax){
        int wave = 0;
        int min = 1;
        int max = 20;

        if (num >= min && num <= midMin){
            wave = 3;
        }
        else if (num >= midMin + 1 && num <= midMax){
            wave = 4;
        }
        else if (num >= midMax + 1 && num <= max){
            wave = 5;
        }
        if (powerWave == 0){
            powerWave = wave;
        }
        else {
            powerWave2 = wave + 4;
        }
    }

    private void resetState(){
        player.resetState();
        delay = 800;
        wave = 1;
        waveText.setText("Wave: " + wave);
        lives = 2;
        count = 0;
        playerMoveInt = 1;
        numMoveY = 2;
        asteroidMoveIntX = 3;
        asteroidMoveIntY = 1;
        pause = 0;
        powerWave = 0;
        powerWave2 = 0;
        first = true;
        startGame = true;
        startStopwatch = true;
        inGame = true;
        gameInit = false;
        firstAsteroid = true;
        newMidMin = 0;
        newMidMax = 0;
        powerMoveInt = 1.3;
    }

}
