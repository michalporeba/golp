package com.michalporeba.golp;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {

    /**
     * observer definition for the observer pattern.
     */
    interface TickObserver {
        void tick();
    }

    private Timer clock = new Timer();
    private TimerTask tick = null;
    private int currentDelay = 500;
    private Publisher tickPublisher = new Publisher();

    private static Clock instance;

    private Clock() {
    }

    synchronized public static Clock getInstance() {
        if (instance == null)
            instance = new Clock();
        return instance;
    }

    public void createUiWith(MenuBuilder menuBuilder) {
        menuBuilder.addOption("Slow", () -> setDelay(1000));
        menuBuilder.addOption("Medium", () -> setDelay(500));
        menuBuilder.addOption("Fast", () -> setDelay(100));
        menuBuilder.addStart(() -> start());
        menuBuilder.addPause(() -> pause());
    }

    public void setDelay(int delayInMilliseconds) {
        currentDelay = delayInMilliseconds;
        if (tick == null) {
            // it is not started, we are simply changing the tempo
            return;
        }

        cancelTick();
        newTick();
    }

    public void start() {
        if (tick != null) {
            // it is already started, there is nothing to do
            return;
        }
        newTick();
    }

    public void pause() {
        cancelTick();
    }

    public void addObserver(TickObserver observer) {
        tickPublisher.subscribe(observer);
    }

    private void cancelTick() {
        if (tick != null) {
            tick.cancel();
            tick = null;
        }
    }

    private void newTick() {
        if (currentDelay > 0) {
            tick = new TimerTask() {
                @Override
                public void run() {
                    tick();
                }
            };
            clock.scheduleAtFixedRate(tick, 0, currentDelay);
        }
    }

    private void tick() {
        tickPublisher.publish(new Publisher.Distributor() {
            @Override
            public void deliverTo(Object subscriber) {
                ((TickObserver)subscriber).tick();
            }
        });
    }

    /**
     * contained interface for the builder pattern
     */
    interface MenuBuilder {
        void addOption(String action, Runnable callback);
        void addStart(Runnable callback);
        void addPause(Runnable callback);
    }
}
