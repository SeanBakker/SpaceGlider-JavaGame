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
    public Timer timer; //Create new timer object
    private String message = "null"; //Create default string message
    private final String victory = "Victory!"; //Create final string victory message
    private Player player; //Create new player object
    private Background background; //Create new background object
    public Asteroids[] asteroids; //Create new asteroids object array
    public Asteroids[] powerUp; //Create new powerUp object array
    private final Random random = new Random(); //Create new random object
    private final Stopwatch stopwatch = new Stopwatch(); //Create new stopwatch object
    private final Stopwatch stopwatch2 = new Stopwatch(); //Create new stopwatch2 object
    private JTextField waveText; //Create new JTextField for displaying text in Jframe
    private JButton pauseButton; //Create new JButton for pausing game
    private JButton startButton; //Create new JButton for starting/restarting game

    //Booleans
    private boolean inGame = true; //If game is running
    private boolean gameInit = false; //If game has been initialized
    private boolean firstAsteroid = true; //If the first asteroid should begin moving
    private boolean newAsteroid = false; //If another asteroid should begin moving
    private boolean startStopwatch = true; //If stopwatch should be started
    private boolean startGame = true; //If game should be started
    private boolean newWave = false; //If a new wave should start
    private boolean first = true; //If loop for "newWave" has not currently been executed yet
    private boolean pressedButton = false; //If JButtons are pressed
    private boolean newPowerUp = false; //If a new power-up wave should be chosen
    private boolean extraLife = false; //If a heart power-up should be released
    private boolean star = false; //If a star power-up should be released
    private boolean restart = false; //If program has been restarted after completion
    private boolean starEffect = false; //If star power-up effect is currently active
    private boolean paintStar = false; //If star power-up should be painted in top left corner

    //Integers & Doubles
    private int delay = 0; //Delay in between choosing asteroids to move
    private int count = 0; //Value for count down at beginning of game
    private int wave = 0; //Wave number
    private int lives = 0; //Amount of player lives
    private int powerType; //Type of power-up
    private int numMoveY = 0; //Number of asteroids that will move in Y direction
    private int pause = 0; //Pausing game
    private int extraLifeWave = 0; //Wave at which an extra life will appear
    private int starWave = 0; //Wave at which the star power-up will appear
    private int starAmount = 0; //Keep track of number of star power-ups
    private int lifeAmount = 0; //Keep track of number of heart power-ups
    private double asteroidMoveIntX = 0; //Speed of asteroids in X direction
    private double asteroidMoveIntY = 0; //Speed of asteroids in Y direction
    private double powerMoveInt = 0; //Value for speed of power-ups
    private double newMidMin = 0; //Percentage of size of asteroids
    private double newMidMax = 0; //Percentage of size of asteroids
    private double playerMoveInt = 0; //Speed of player
    private float opacity = 0.0f; //Opacity variable for asteroids

    //Constructor to initialize board
    public Board() {
        initBoard();
    }

    //Initialize Board properties
    private void initBoard() {

        addKeyListener(new TAdapter()); //Add keyListener to take user input
        setPreferredSize(new Dimension(Features.WIDTH, Features.HEIGHT)); //Set size of board
        setFocusable(true); //Set board to be focusable
        setLayout(null); //Set layout of board

        //Start Button
        startButton = new JButton("Start Game"); //Set text for startButton
        startButton.setBounds(430,240,110,25); //Set bounds and position of button
        add(startButton); //Add button to Jframe
        startButton.setVisible(true); //Set to visible
        startButton.setBackground(Color.BLACK); //Set colour of background
        startButton.setForeground(Color.WHITE); //Set colour of text
        startButton.setFocusable(false); //Set button to not be focusable

        //Pause Button
        pauseButton = new JButton("Pause Game"); //Set text for pauseButton
        pauseButton.setBounds(880,0,110,25); //Set bounds and position of button
        pauseButton.setVisible(true); //Set to visible
        pauseButton.setBackground(Color.BLACK); //Set colour of background
        pauseButton.setForeground(Color.WHITE); //Set colour of text
        pauseButton.setFocusable(false); //Set button to not be focusable

        //Wave TextBox
        waveText = new JTextField("Wave: " + wave,5); //Set text for waveText
        Font font = new Font("Arial Black", Font.BOLD, 10); //Set font for waveText
        waveText.setBounds(815,0,70,25); //Set bounds and position of JTextField
        waveText.setFont(font); //Set font to specified font
        waveText.setOpaque(true); //Set background to opaque
        waveText.setBackground(Color.BLACK); //Set colour of background
        waveText.setForeground(Color.white); //Set colour of text
        waveText.setFocusable(false); //Set JTextField to not be focusable
        waveText.setEditable(false);  //Set JTextField to not be editable

        //Create timer for game cycle
        timer = new Timer(Features.PERIOD, new GameCycle());

        //Pause button action listener to stop/start timer when pressed
        pauseButton.addActionListener(e -> {
            pause++; //Increase pause everytime button is pressed
            if (pause % 2 != 0) { //When pause is even, stop timer and pause game
                if (pressedButton){
                    timer.stop();
                }
            }
            else {
                if (pressedButton){ //When pause is odd, resume timer and unpause game
                    timer.start();
                }
            }
        });

        player = new Player(); //Initialize player object

        //Start button action listener to start game once button is pressed
        if (!pressedButton) { //Only trigger if button has not yet been pressed
            startButton.addActionListener(e -> {
                pressedButton = true; //Set pressedButton to true
                startGame = true; //Start game
                resetState(); //Reset all state
                asteroids = new Asteroids[Features.N_OF_ROCKS]; //Initialize asteroids object array
                powerUp = new Asteroids[Features.N_OF_ROCKS]; //Initialize powerUp object array
                timer.start(); //Start game timer
                gameInit(); //Call initialize game method
                remove(startButton); //remove button from Jframe
                add(pauseButton); //Add button to Jframe
                add(waveText); //Add JTextField to Jframe
            });
        }
    }

    //Initialize asteroids and power-ups each round
    private void gameInit() {

        //Create integer "k" for looping over array elements
        int k = 0;

        //For loop to loop over 10 rows of asteroids
        for (int i = 0; i < 10; i++) {
            //For loop to loop over 2 columns of asteroids
            for (int j = 0; j < 2; j++) {

                //Create new asteroid at specific position
                asteroids[k] = new Asteroids(j + 900, i * 40 + 5, newMidMin, newMidMax, false, powerType);
                asteroids[k].setDestroyed(false); //Set asteroid to not be destroyed
                asteroids[k].setMoving(false); //Set asteroid to not be moving
                asteroids[k].setMovingY(false); //Set asteroid to not be moving in the Y direction
                asteroids[k].setMovingUp(false); //Set asteroid to not be moving upwards
                k++; //Increase k value
            }
        }

        k = 0; //Reset k value for power-ups

        //If there is a newPowerUp to be released
        if (newPowerUp){
            //For loop to loop over 10 rows of power-ups
            for (int i = 0; i < 10; i++) {
                //For loop to loop over 2 columns of power-ups
                for (int j = 0; j < 2; j++) {

                    //Create new power-up at specific position
                    powerUp[k] = new Asteroids(j + 900, i * 40 + 5, newMidMin, newMidMax, newPowerUp, powerType);
                    powerUp[k].setDestroyed(false); //Set power-up to not be destroyed
                    powerUp[k].setMoving(false); //Set power-up to not be moving
                    powerUp[k].setHeart(false); //Set power-up to not be a heart
                    powerUp[k].setStar(false); //Set power-up to not be a star
                    k++; //Increase k value
                }
            }
        }
        gameInit = true; //Game has now been initialized
    }

    //CountDown method for displaying countdown at the beginning of the game
    private void countDown() throws Exception {

        if (count == 0 && !restart){ //For first time loading game, display welcome message
            message = "Welcome to Space Glider"; //Print welcome message
            restart = true; //Set restart to true
        }
        //Allows count down to replay after restart button is clicked
        if (pressedButton) { //Once start button is pressed, begin count down
            if (count == 1) {
                message = "3"; //Set message to 3
                Thread.sleep(500); //Delay program
            }
            else if (count == 2) {
                message = "2"; //Set message to 2
                Thread.sleep(1000); //Delay program
            }
            else if (count == 3) {
                message = "1"; //Set message to 1
                Thread.sleep(1000); //Delay program
            }
            else if (count == 4) {
                message = "Wave 1 Starting"; //Start wave 1 message
                Thread.sleep(1000); //Delay program
            }
            else if (count == 5) {
                startGame = false; //Start game process is now over
                Thread.sleep(1000); //Delay program
            }
            count++; //Increase count each loop
        }
    }

    //NextWave method controls all required changes after each wave is complete
    private void nextWave() throws Exception {

        //Add extraLife power-up when wave equals extraLifeWave
        if (wave == extraLifeWave && !first){
            if (wave > 5){ //Increase speed of power-ups after wave 5
                powerMoveInt = powerMoveInt + 1.3;
            }
            newPowerUp = true; //Release new power-up
            extraLife = true; //Set new power-up to a heart
            powerType = 0; //Set powerType = 0 for heart
            lifeAmount++; //Increase amount of hearts that have been released
        }
        else if (wave == starWave && !first){ //Add star power-up when wave equals starWave
            newPowerUp = true; //Release new power-up
            star = true; //Set new power-up to a star
            powerType = 1; //Set powerType = 1 for star
            starAmount++; //Increase amount of stars that have been released
        }
        else { //Set variables to false if there are no power-ups
            newPowerUp = false; //Don't release any power-ups
            extraLife = false; //Set extraLife to false
            star = false; //Set star to false
        }
        //Pick new starWave after previous one has passed
        if (wave > starWave){
            pickWave(0); //Call pickWave method to pick new starWave
        }
        //Pick new extraLifeWave after previous one has passed
        if (wave > extraLifeWave){
            pickWave(1); //Call pickWave method to pick new extraLifeWave
        }
        //Change variables each wave
        if (!first) { //Execute on second loop through nextWave method
            if (wave % 2 == 0){ //Increase number of asteroids moving in Y direction every two waves that pass
                numMoveY++;
            }
            if (wave <= 15){ //Change all variables for asteroids and player up until wave 16
                delay = delay - 45;
                asteroidMoveIntY = asteroidMoveIntY + 0.2;
                playerMoveInt = playerMoveInt + 0.2;
                player.moveInt = playerMoveInt;
                asteroidMoveIntX = (asteroidMoveIntX * 1.0) + 0.7;
                newMidMin = (newMidMin - 1);
                newMidMax = (newMidMax - 1);
            }
            newWave = false; //Set newWave to false after execution
            Thread.sleep(2000); //Delay program
            gameInit(); //Call initialize game method
        }
        else {
            message = "Wave " + wave + " Starting"; //Set next wave starting text
            waveText.setText("Wave: " + wave); //Update JTextField wave text
            first = false; //After first loop, set "first" to false
        }
    }


    //PaintComponent method for controlling the painting of images
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g; //Create Graphics2D "g2d" to paint images

        //For Rendering images, hint is used for choosing rendering speed or quality of image
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        //Run at beginning of game to display background/countDown
        if (startGame){
            drawBackground(g2d); //Call drawBackground method to paint background at beginning of game
            try { //Try statement as it may throw exception for sleeping thread while in countDown method
                countDown(); //Call countDown method
            }
            catch (Exception e) {
                e.printStackTrace(); //Throw exception if error occurs in countDown method
            }
            paintText2D(g2d); //Call paintText2D method to paint messages to screen
        }
        //In between waves repaint background and display nextWave text
        else if (newWave){
            drawBackground(g2d); //Call drawBackground method to repaint background
            try{ //Try statement as it may throw exception for sleeping thread while in nextWave method
                nextWave(); //Call nextWave method
            } catch (Exception e) {
                e.printStackTrace(); //Throw exception if error occurs in nextWave method
            }
            paintText2D(g2d); //Call paintText2D method to paint messages to screen
        }
        //While in game drawObjects will paint images for game
        else if (inGame) {
            drawObjects(g2d); //Call drawObjects method
            first = true; //Set first to true
        }
        else { //When the game is not running
            drawBackground(g2d); //Draw specified background at end of game
            paintText2D(g2d); //Call paintText2D method to paint messages to screen
        }
        Toolkit.getDefaultToolkit().sync(); //Sync toolkit
    }

    //Draws background while in between waves or after victory
    private void drawBackground(Graphics2D g2d) {
        if (message.equals(victory)){ //If player has won the game
            background = new Background(100); //Create new background object with "victory" image
        }
        else { //If player has not won the game
            background = new Background(0); //Create new background object with "game over" image
        }
        //Draw background given all dimensions and properties
        g2d.drawImage(background.getImage(), (int) background.getX(), (int) background.getY(),
                (int) background.getImageWidth(), (int) background.getImageHeight(), this);
    }

    //Draws images for game
    private void drawObjects(Graphics2D g2d) {
        int j = 0; //Integer "j" used for keeping track of positions of hearts (used in star image)

        //Paint background for each wave
        background = new Background(wave); //Set background equal to specific wave background image
        g2d.drawImage(background.getImage(), (int) background.getX(), (int) background.getY(),
                (int) background.getImageWidth(), (int) background.getImageHeight(), this);

        //Paint hearts for how many lives remain
        for (int i = 1; i <= lives; i++){
            background = new Background(101); //Create new background object with heart image
            //Draw heart image in top left corner to visually track the number of lives of the player
            g2d.drawImage(background.getImage(), i * 5 + ((int) background.getImageWidth() * (i-1)), 5,
                    (int) background.getImageWidth(), (int) background.getImageHeight(), this);
            j = i * 5 + ((int) background.getImageWidth() * (i-1)); //Keep track of last position of heart

        }

        //Timer for how long starEffect lasts / controls flashing effect of star
        if (starEffect) {
            if (stopwatch2.getElapsedTime() < 7000) { //Only paint star in between 0-7 secs
                paintStar = true;
            }
            else if (stopwatch2.getElapsedTime() > 7500 && stopwatch2.getElapsedTime() < 8000) { //Only paint star in between 7.5-8 secs
                paintStar = true;
            }
            else if (stopwatch2.getElapsedTime() > 8500 && stopwatch2.getElapsedTime() < 9000) { //Only paint star in between 8.5-9 secs
                paintStar = true;
            }
            else if (stopwatch2.getElapsedTime() > 9500 && stopwatch2.getElapsedTime() < 10000){ //Only paint star in between 9.5-10 secs
                paintStar = true;
            }

            //Paint star power-up
            if (paintStar){
                background = new Background(102); //Create new background object with star image
                //Draw star beside the last heart in top left corner
                g2d.drawImage(background.getImage(), j + ((int) background.getImageWidth()), 2,
                        (int) background.getImageWidth(), (int) background.getImageHeight(), this);
            }
            paintStar = false; //Stop painting star
        }

        //Draw player spaceship
        g2d.drawImage(player.getImage(), (int) player.getX(), (int) player.getY(),
                (int) player.getImageWidth(), (int) player.getImageHeight(), this);

        //Paint asteroids
        for (int i = 0; i < Features.N_OF_ROCKS; i++) {

            //Set opacity of asteroids while moving
            if (!asteroids[i].isDestroyed() && asteroids[i].isMoving()){
                opacity = 1.0f; //Set opacity to opaque
            }
            //Set opacity of asteroids while not moving
            else if (!asteroids[i].isDestroyed() && !asteroids[i].isMoving()){
                opacity = 0.0f; //Set opacity to transparent
            }

            //Draw asteroids when not destroyed
            if (!asteroids[i].isDestroyed()) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity)); //Draw asteroids using specified opacity
                g2d.drawImage(asteroids[i].getImage(), (int) asteroids[i].getX(),
                        (int) asteroids[i].getY(), (int) asteroids[i].getImageWidth(),
                        (int) asteroids[i].getImageHeight(), this);

            }
        }

        //Repeat drawing of asteroids for power-ups
        if (newPowerUp && gameInit) { //Only draw power-ups when a newPowerUp is active in that wave
            for (int i = 0; i < Features.N_OF_ROCKS; i++) {

                //Once again, paint power-up to be opaque when the power-up is moving
                if (!powerUp[i].isDestroyed() && powerUp[i].isMoving()) {
                    opacity = 1.0f;
                }
                else if (!powerUp[i].isDestroyed() && !powerUp[i].isMoving()) {
                    opacity = 0.0f;
                }

                //Draw power-ups when they are not destroyed
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

        //Set font type and size of messages
        Font font = new Font("Arial Black", Font.BOLD, 30);
        FontMetrics fontMetrics = this.getFontMetrics(font);

        g2d.setColor(Color.WHITE); //Set colour of message text
        g2d.setFont(font); //Set font of message text
        //Draw message to screen with specified dimensions
        g2d.drawString(message,
                (Features.WIDTH - fontMetrics.stringWidth(message)) / 2,
                Features.HEIGHT / 2);
    }

    //Check for key input of player
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) { player.keyReleased(e); } //Check for release of key input from player
        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        } //Check for key input from player
    }

    //ActionListener to listen to input while game is running
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        } //Check for actions performed during game cycle
    }

    //Move all objects during gameCycle / check their collision / repaint images
    private void doGameCycle() {
        player.move(); //Move player
        asteroidMove(); //Move asteroids
        if (newPowerUp){ //Move power-ups when newPowerUp is true
            powerUpMove();
        }
        checkCollision(); //Check for collisions of player and asteroids/power-ups
        repaint(); //Repaint all images
    }

    //Stop game timer and display restart button
    private void stopGame() {
        add(startButton); //Add restart button to Jframe
        startButton.setVisible(true); //Set button to visible
        startButton.setText("Restart"); //Set text of restart button
        pressedButton = false; //Set pressedButton to false so that game won't restart till it is pressed
        inGame = false; //Set inGame to false
        timer.stop(); //Stop game timer
    }

    //Check for collision with asteroids/power-ups
    private void checkCollision() {
        int waveAmount = 20; //Variable for total number of waves

        //Check if all asteroids have been destroyed
        for (int i = 0, j = 0; i < Features.N_OF_ROCKS; i++) { //Loop over all asteroids in array

            if (asteroids[i].isDestroyed()) { //If an asteroid is destroyed, increase "j" variable
                j++;
            }
            //Stop game if all asteroids are destroyed and player is on last wave
            if (j == Features.N_OF_ROCKS && wave >= waveAmount) {
                message = victory; //Set message to victory
                stopGame(); //Call stopGame method
            }
            //Begin next wave if all asteroids are destroyed, but player is not on the last wave
            if (j == Features.N_OF_ROCKS && wave < waveAmount) {
                wave++; //Increase wave number
                newWave = true; //Set newWave to true
                gameInit = false; //Set gameInit to false to reinitialize game
                firstAsteroid = true; //Set firstAsteroid to false as first asteroid of next wave is not moving yet

                //Reset All Asteroids
                for (int k = 0; k < Features.N_OF_ROCKS; k++){
                    asteroids[k].setDestroyed(false); //Set all asteroids to not be destroyed
                    asteroids[k].setMovingY(false); //Set alla steroids to not be moving in Y direction
                }
            }
        }

        //Check collision of player and asteroids
        for (int i = 0; i < Features.N_OF_ROCKS; i++) {

            if (asteroids[i].isDestroyed()){ //Set new X value of asteroid when it has been destroyed
                asteroids[i].setX(Features.WIDTH);
            }

            //Check for collision between player rectangle and asteroid rectangle
            if ((player.getRect()).intersects(asteroids[i].getRect())) {

                if (!asteroids[i].isDestroyed()) { //If they collide, set asteroid to destroyed
                    asteroids[i].setDestroyed(true);
                }
                //Player looses a life if they do not have starEffect active
                if (!starEffect) {
                    if (lives - 1 > 0) { //As long as player has 2 or more lives, subtract 1 life from player
                        lives--;
                    }
                    //Game stops if player has no more lives
                    else {
                        message = "Game Over"; //Set game over message
                        stopGame(); //Call stopGame method
                    }
                }
            }

            //Asteroids are destroyed when they reach the side edge
            if (asteroids[i].getRect().getX() == Features.SIDE_EDGE){
                asteroids[i].setDestroyed(true);
            }

        }

        //Check for collision between player and power-up
        if (newPowerUp && gameInit) { //Loop only runs on a wave with a power-up due to newPowerUp variable
            for (int i = 0; i < Features.N_OF_ROCKS; i++) {

                if (powerUp[i].isDestroyed()) { //Set new X value of power-up when it has been destroyed
                    powerUp[i].setX(Features.WIDTH);
                }

                //Check for collision between player rectangle and power-up rectangle while power-up is moving
                if ((player.getRect()).intersects(powerUp[i].getRect()) && powerUp[i].isMoving()) {

                    //Player's lives increase if they collide with extraLife power-up
                    if (!powerUp[i].isDestroyed() && powerUp[i].isHeart()) {
                        powerUp[i].setDestroyed(true); //Power-up is destroyed after collision
                        lives++; //Player lives increase
                    }
                    //StarEffect is true if player collides with star power-up
                    else if (!powerUp[i].isDestroyed() && powerUp[i].isStar()){
                        powerUp[i].setDestroyed(true); //Power-up is destroyed after collision
                        starEffect = true; //StarEffect becomes active
                        stopwatch2.start(); //Stopwatch2 keeps track of time for star power-up
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
            stopwatch2.stop(); //Stop time for stopwatch of star power-up
            starEffect = false; //StarEffect is no longer active and therefore false
        }

    }

    //Method for moving asteroids
    private void asteroidMove(){
        int firstDelay = 3000; //Delay for first asteroid

        if (startStopwatch){ //Restart stopwatch after each asteroid is deployed
            stopwatch.start(); //Start stopwatch
            startStopwatch = false;
        }

        //Begin moving asteroid after delay of stopwatch
        if (stopwatch.getElapsedTime() >= delay && !firstAsteroid){
            newAsteroid = true; //Pick new asteroid to move
        }
        //If this is the first asteroid, delay is longer before asteroid will move
        else if (stopwatch.getElapsedTime() >= firstDelay && firstAsteroid){

            //Pick asteroids to move in Y direction using random number
            //Loop is dependent upon the number of asteroids moving in Y direction for that wave (numMoveY)
            for (int i = 0; i < numMoveY; i++) {
                int num = random.nextInt(Features.N_OF_ROCKS); //Pick random asteroid
                asteroids[num].setMovingY(true); //Set this asteroid to move in Y direction

                //Chance of asteroid moving up or down
                num = random.nextInt(Features.N_OF_ROCKS); //Pick new random number
                if (num > Features.N_OF_ROCKS / 2) { //If number is between 11-20, the asteroid will move upwards (default is down)
                    asteroids[num].setMovingUp(true);
                }
            }
            newAsteroid = true; //Pick new asteroid to move in X direction
            firstAsteroid = false; //FirstAsteroid is false after the first asteroid is now moving
        }

        if (newAsteroid){

            //Set random asteroid moving in X direction when newAsteroid = true
            int num = random.nextInt(Features.N_OF_ROCKS); //Pick random asteroid
            asteroids[num].setMoving(true);

            //Keep choosing new asteroid if previous has already been destroyed
            while (asteroids[num].isDestroyed()){
                num = random.nextInt(Features.N_OF_ROCKS); //Pick new random number
                asteroids[num].setMoving(true); //Set new asteroid to moving
            }

            stopwatch.stop(); //Stop stopwatch
            startStopwatch = true; //Reset timer for stopwatch for delay of asteroids
            newAsteroid = false; //newAsteroid becomes false so that only after the delay a new asteroid will be chosen
        }

        //For loop to move asteroids
        for (int i = 0; i < Features.N_OF_ROCKS; i++) {

            //Move asteroids in Y direction
            if (asteroids[i].isMovingY() && asteroids[i].isMoving()) {
                //Move asteroid up until top border
                if (asteroids[i].isMovingUp() && asteroids[i].getY() - asteroidMoveIntY >= 5) {
                    asteroids[i].setY(asteroids[i].getY() - asteroidMoveIntY); //Set new Y position for asteroid
                }
                //Change direction after hitting top border
                else if (asteroids[i].isMovingUp()){
                    asteroids[i].setMovingUp(false);
                }

                //Move asteroid down until bottom border
                if (!asteroids[i].isMovingUp() && asteroids[i].getY() + asteroidMoveIntY <= Features.HEIGHT - 40) {
                    asteroids[i].setY(asteroids[i].getY() + asteroidMoveIntY); //Set new position for asteroid
                }
                //Change direction after hitting bottom border
                else if (!asteroids[i].isMovingUp()){
                    asteroids[i].setMovingUp(true);
                }
            }

            //Move asteroids forward in X direction until the side edge
            if (asteroids[i].getX() - asteroidMoveIntX < Features.SIDE_EDGE) {
                asteroids[i].setDestroyed(true); //Once the asteroid collides with the side edge, the asteroid is destroyed
            }
            else if (asteroids[i].isMoving()) {
                asteroids[i].setX(asteroids[i].getX() - asteroidMoveIntX); //Set new X position for asteroid
            }
        }
    }

    //Method for moving power-ups
    private void powerUpMove() {

        if (gameInit) {
            //Set random asteroid to extraLife power-up
            if (extraLife) { //Select power-up when extraLife is true for that wave
                int num = random.nextInt(Features.N_OF_ROCKS); //Pick new asteroid
                powerUp[num].setMoving(true); //Set that asteroid to moving
                powerUp[num].setHeart(true); //Set that asteroid to a heart power-up
                extraLife = false; //Set extraLife false after the heart power-up has been selected
            }
            //Set random asteroid to star power-up
            if (star) { //Select power-up when star is true for that wave
                int num = random.nextInt(Features.N_OF_ROCKS); //Pick new asteroid
                powerUp[num].setMoving(true); //Set that asteroid to moving
                powerUp[num].setStar(true); //Set that asteroid to a star power-up
                star = false; //Set star to false after the star power-up has been selected
            }

            //Move power-ups until side edge
            for (int i = 0; i < Features.N_OF_ROCKS; i++) {
                if (powerUp[i].getX() - powerMoveInt < Features.SIDE_EDGE) {
                    powerUp[i].setDestroyed(true); //Set power-ups to destroyed when they collide with teh side edge
                } else if (powerUp[i].isMoving()) {
                    powerUp[i].setX(powerUp[i].getX() - powerMoveInt); //Set new X position for power-up
                }
            }
        }
    }

    //Method for randomizing power-up waves
    private void pickWave(int powerType) {
        int num = random.nextInt(20); //Pick random number
        int wave; //Integer wave to set power-up wave
        //Integer variables to set percentage for power-up waves
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
        if (powerType == 0) { //PowerType = 0 means a star power-up wave will be chosen
            //Add to starWave to have power-up arrive in later waves
            if (starAmount == 0) { //First wave that star power-up appears
                starWave = wave + 10; //Set wave for power-up
            } else if (starAmount == 1) { //Second wave that star power-up appears
                starWave = wave + 13; //Set wave for power-up
            }
        }
        else if (powerType == 1) { //PowerType = 1 means a heart power-up wave will be chosen
            if (lifeAmount == 0) { //First wave that heart power-up appears
                extraLifeWave = wave; //Set wave for power-up
            }
            //Add to extraLifeWave to have power-up arrive in later waves
            else if (lifeAmount == 1) { //Second wave that heart power-up appears
                extraLifeWave = wave + 5; //Set wave for power-up
            } else if (lifeAmount == 2) { //Third wave that heart power-up appears
                extraLifeWave = wave + 12; //Set wave for power-up
            }
        }
        //Check for power-ups on the same wave
        if (extraLifeWave == starWave) {
            extraLifeWave++; //Force heart power-up to appear after star power-up
        }
    }

    //Method for resetting state of all variables
    private void resetState() {
        player.resetState(); //Reset all player state
        delay = 1000; //Set delay for asteroids
        wave = 1; //Set wave to 1
        waveText.setText("Wave: " + wave); //Set waveText for JTextField
        lives = 3; //Set player lives to 3
        count = 0; //Set count to 0
        playerMoveInt = 1; //Set default player speed to 1
        numMoveY = 2; //Set number of asteroids moving in Y direction to 2
        asteroidMoveIntX = 3; //Set default asteroid speed in X direction to 3
        asteroidMoveIntY = 1; //Set default asteroid speed in Y direction to 1
        pause = 0; //Set pause to 0
        extraLifeWave = 0; //Set wave for heart to appear to 0
        starWave = 0; //Set wave to star to appear to 0
        first = true; //Set first true
        startGame = true; //Set startGame true
        startStopwatch = true; //Set startStopwatch to true
        inGame = true; //Set inGame to true
        gameInit = false; //Set gameInit to false as game has not yet been initialized
        firstAsteroid = true; //Set firstAsteroid to true
        newMidMin = 0; //Set newMidMin to 0
        newMidMax = 0; //Set newMidMax to 0
        powerMoveInt = 1.3; //Set default speed for power-ups
        starEffect = false; //Set starEffect to false as effect is not active
    }

}
