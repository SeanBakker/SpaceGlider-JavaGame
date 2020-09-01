package com.company;

/*
Stopwatch class for creating stopwatches
 */

public class Stopwatch {

    //Variables
    private long startTime = 0; //Time value for starting stopwatch
    private long stopTime = 0; //Time value for stopping stopwatch
    private boolean running = false; //Boolean for if stopwatch is running

    //Start timer for stopwatch
    public void start() {
        this.startTime = System.currentTimeMillis(); //Set startTime to current time
        this.running = true; //Set running to true
    }

    //Stop timer for stopwatch
    public void stop() {
        this.stopTime = System.currentTimeMillis(); //Set stopTime to current time
        this.running = false; //Set running to false
    }

    //Elapsed time in milliseconds
    public long getElapsedTime() {
        long elapsed; //Variable for elapsed time
        if (running) { //When running, get elapsed time
            elapsed = (System.currentTimeMillis() - startTime);
        } else { //When not running, get previous elapsed time
            elapsed = (stopTime - startTime);
        }
        return elapsed; //Return elapsed time
    }

}
