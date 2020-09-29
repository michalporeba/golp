package com.michalporeba.golp;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {
    private Timer clock = new Timer();
    private TimerTask tick = null;
    private int currentDelay = 500;
    //private List<TickObserver> tickObservers = new ArrayList<TickObserver>();
    private Publisher tickPublisher = new Publisher();

    private static Clock instance;

    private Clock() {
    }

    public synchronized static Clock getInstance() {
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
        if (tick != null) {
            tick.cancel();
            tick = null;
        }

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

    public void start() {
        cancelTick();

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

    private void tick() {
        tickPublisher.publish(new Publisher.Distributor() {
            @Override
            public void deliverTo(Object subscriber) {
                ((TickObserver)subscriber).tick();
            }
        });
    }

    interface ClockCallback {
        void execute();
    }

    /**
     * contained interface for the builder pattern
     */
    interface MenuBuilder {
        void addOption(String action, ClockCallback callback);
        void addStart(ClockCallback callback);
        void addPause(ClockCallback callback);
    }

    /**
     * observer definition for the observer pattern.
     */
    interface TickObserver {
        void tick();
    }
}
