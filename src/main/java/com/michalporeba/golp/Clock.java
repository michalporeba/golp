package com.michalporeba.golp;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {
    /**
     * contained interface for the builder pattern
     */
    interface UiBuilder {
        void addOption(Actions action, Runnable callback);
    }

    /**
     * The Observer (from observer pattern) definition which is notified
     * about clock functions being invoked. It is useful on the UI
     * to change the state of the UI to align with what functions are
     * available.
     */
    interface ActionObserver {
        // TODO: it could be further improved if the UI did not check the logic
        // but instead the clock decided which actions are available. Perhaps
        // this observer could be changed from a push to pull model.
        void actionInvoked(Actions action);
    }

    /**
     * The Observer (from observer pattern) definition which is notified about ticks.
     * This interface could be removed and Runnable used instead
     * but understanding the observer pattern would be more difficult
     */
    interface TickObserver {
        void tick();
    }

    enum Actions {
        Slow,
        Medium,
        Fast,
        SlowDown,
        SpeedUp,
        Start,
        Pause,
        Tick
    }

    private Timer clock = new Timer();
    private TimerTask tick = null;
    private int currentDelay = 500;
    private Publisher tickPublisher = new Publisher();
    private Publisher actionPublisher = new Publisher();

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
        uiBuilder.addOption(Actions.Slow, () -> setSlow());
        uiBuilder.addOption(Actions.Medium, () -> setMedium());
        uiBuilder.addOption(Actions.Fast, () -> setFast());
        uiBuilder.addOption(Actions.SpeedUp, () -> speedUp());
        uiBuilder.addOption(Actions.SlowDown, () -> slowDown());
        uiBuilder.addOption(Actions.Start, () -> start());
        uiBuilder.addOption(Actions.Pause, () -> pause());
        uiBuilder.addOption(Actions.Tick, () -> tickNotify());
    }

    public void addMenuTo(ActionableUi ui) {
        ui.addAction("Clock", this, Actions.Tick, () -> tickNotify());
        ui.addAction("Clock", this, Actions.Start, () -> start());
        ui.addAction("Clock", this, Actions.Pause, () -> pause());
        ui.addAction("Clock.Speed", this, Actions.Slow, () -> setSlow());
        ui.addAction("Clock.Speed", this, Actions.Medium, () -> setMedium());
        ui.addAction("Clock.Speed", this, Actions.Fast, () -> setFast());
    }

    public void setSlow() {
        actionNotify(Actions.Slow);
        setDelay(1000);
    }

    public void setMedium() {
        actionNotify(Actions.Medium);
        setDelay(500);
    }

    public void setFast() {
        actionNotify(Actions.Fast);
        setDelay(100);
    }

    public void slowDown() {
        setDelay((int)(currentDelay*1.1));
    }

    public void speedUp() {
        if (currentDelay > 10) {
            setDelay((int) (currentDelay * 0.9));
        }
    }

    private void setDelay(int delayInMilliseconds) {
        currentDelay = delayInMilliseconds;
        if (tick == null) // it is not started, we are simply changing the tempo
            return;

        pause();
        newTick();
    }

    public void start() {
        // start only if it isn't started already
        if (tick == null) {
            actionNotify(Actions.Start);
            newTick();
        }
    }

    public void pause() {
        if (tick == null)
            return;

        actionNotify(Actions.Pause);
        tick.cancel();
        tick = null;
    }

    public void addObserver(TickObserver observer) {
        tickPublisher.subscribe(observer);
    }
    public void addObserver(ActionObserver observer) { actionPublisher.subscribe(observer); }

    private void newTick() {
        if (currentDelay > 0) {
            actionNotify(Actions.Start);
            tick = new TimerTask() {
                @Override
                public void run() {
                    tickNotify();
                }
            };
            clock.scheduleAtFixedRate(tick, 0, currentDelay);
        }
    }

    private void tickNotify() {
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

    private void actionNotify(Actions action) {
        actionPublisher.publish(new Publisher.Distributor() {
            @Override
            public void deliverTo(Object subscriber) {
                ((ActionObserver)subscriber).actionInvoked(action);
            }
        });
    }
}