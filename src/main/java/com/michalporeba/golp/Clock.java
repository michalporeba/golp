package com.michalporeba.golp;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {

    /**
     * observer definition for the observer pattern.
     * this interface could be removed and Runnable used instead
     * but understanding the observer pattern would be more difficult
     */
    interface TickObserver {
        void tick();
    }

    enum Actions {
        Slow,
        Medium,
        Fast,
        Start,
        Pause,
        Tick
    }

    private Timer clock = new Timer();
    private TimerTask tick = null;
    private int currentDelay = 500;
    private Publisher tickPublisher = new Publisher();

    private static Clock instance;

    private Clock() {
    }

    public static synchronized Clock getInstance() {
        if (instance == null)
            instance = new Clock();
        return instance;
    }

    /**
     * This method creates the UI for the Clock object
     * It is important that the object creates its own UI, but it is also important
     * that it does not depend on the specifics of the UI framework used. To deal
     * with this problem the Builder pattern is used.
     * @param uiBuilder
     */
    public void createUiWith(UiBuilder uiBuilder) {
        uiBuilder.addGroup("Tempos");
        uiBuilder.addOption(Actions.Slow, () -> setDelay(1000));
        uiBuilder.addOption(Actions.Medium, () -> setDelay(500));
        uiBuilder.addOption(Actions.Fast, () -> setDelay(100));
        uiBuilder.addGroup("Controls");
        uiBuilder.addOption(Actions.Start, () -> start());
        uiBuilder.addOption(Actions.Pause, () -> pause());
        uiBuilder.addGroup("Other");
        uiBuilder.addOption(Actions.Tick, () -> tick());
    }

    public void setDelay(int delayInMilliseconds) {
        currentDelay = delayInMilliseconds;
        if (tick == null) // it is not started, we are simply changing the tempo
            return;

        pause();
        newTick();
    }

    public void start() {
        // start only if it isn't started already
        if (tick == null)
            newTick();
    }

    public void pause() {
        if (tick == null)
            return;

        tick.cancel();
        tick = null;
    }

    public void addObserver(TickObserver observer) {
        tickPublisher.subscribe(observer);
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

    private void tickAlternativeSyntax() {
        // calls deliverTo method on Publisher.Distributor
        tickPublisher.publish(subscriber -> ((TickObserver)subscriber).tick());
    }

    /**
     * contained interface for the builder pattern
     */
    interface UiBuilder {
        void addGroup(String name);
        void addOption(Actions action, Runnable callback);
    }
}
