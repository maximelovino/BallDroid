package ch.hepia.lovino.balldroid.controllers;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public class TimerThread extends Thread {
    private int timerMs;
    private boolean run;
    private GameController controller;
    private long currentTime = 0;
    private long startTime = 0;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable notifyGame = new Runnable() {
        @Override
        public void run() {
            controller.endGame();
        }
    };

    public TimerThread(int timerMs, GameController controller) {
        super();
        this.timerMs = timerMs;
        this.controller = controller;
        this.run = true;
    }

    public void stopTimer() {
        this.run = false;
    }

    public long getTime() {
        return currentTime - startTime;
    }

    @Override
    public void run() {
        Log.v("TIMER", "Timer started");
        startTime = System.currentTimeMillis();
        currentTime = startTime;

        while (this.run && (currentTime - startTime) <= timerMs) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
            }
            currentTime = System.currentTimeMillis();
        }
        if (run) {
            //if nobody stopped it, we post
            mainHandler.post(notifyGame);
        }

    }
}