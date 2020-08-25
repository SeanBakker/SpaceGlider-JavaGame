package com.company;

/*
Stopwatch class for creating stopwatches
 */

public class Stopwatch {

    //Variables
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    //Start timer for stopwatch
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    //Stop timer for stopwatch
    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    //Elapsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }

}
