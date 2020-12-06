package com.michalporeba.golp;

import java.util.Timer;
import java.util.TimerTask;

public class Clock {
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
    private Publisher oldActionPublisher = new Publisher();
    private Publisher actionPublisher = new Publisher();

    private static Clock instance;

    private Clock() {
    }

    public static synchronized Clock getInstance() {
        if (instance == null)
            instance = new Clock();
        return instance;
    }

    public void addToMenu(ActionableUi ui) {
        ui.addAction("Clock", this, Actions.Tick, () -> tickNotify());
        ui.addAction("Clock", this, Actions.Start, () -> start());
        ui.addAction("Clock", this, Actions.Pause, () -> pause());
        ui.addAction("Clock.Speed", this, Actions.Slow, () -> setSlow());
        ui.addAction("Clock.Speed", this, Actions.Medium, () -> setMedium());
        ui.addAction("Clock.Speed", this, Actions.Fast, () -> setFast());
        actionPublisher.subscribe(ui);
        disable(Actions.Pause);
    }

    public void addToToolbar(ActionableUi ui) {
        ui.addAction("Clock", this, Actions.Tick, () -> tickNotify());
        ui.addAction("Clock", this, Actions.Start, () -> start());
        ui.addAction("Clock", this, Actions.Pause, () -> pause());
        ui.addAction("Clock.Speed", this, Actions.SlowDown, () -> slowDown());
        ui.addAction("Clock.Speed", this, Actions.SpeedUp, () -> speedUp());
        actionPublisher.subscribe(ui);
        disable(Actions.Pause);
    }

    public void setSlow() {
        setDelay(1000);
    }

    public void setMedium() {
        setDelay(500);
    }

    public void setFast() {
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
            newTick();
            disable(Actions.Tick, Actions.Start);
            enable(Actions.Pause);
        }

        tickPublisher.publish(subscriber -> ((TickObserver)subscriber).tick());
    }

    public void pause() {
        if (tick == null)
            return;

        disable(Actions.Pause);
        enable(Actions.Tick, Actions.Start);
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

    private void enable(Actions... actions) {
        for(var action : actions) {
            actionPublisher.publish(s -> ((ActionableUi)s).enable(this, action));
        }
    }

    private void disable(Actions... actions) {
        for(var action : actions) {
            actionPublisher.publish(s -> ((ActionableUi)s).disable(this, action));
        }
    }
}