package com.michalporeba.golp;

import java.util.Timer;
import java.util.TimerTask;

public class Clock {
    private Timer clock = new Timer();
    private TimerTask tick = null;

    private static Clock instance;

    private Clock() {
    }

    public synchronized static Clock getInstance() {
        if (instance == null)
            instance = new Clock();
        return instance;
    }

    public void createUiWith(MenuBuilder menuBuilder) {
        menuBuilder.addOption("Stop", () -> setDelay(0));
        menuBuilder.addOption("Slow", () -> setDelay(100));
        menuBuilder.addOption("Medium", () -> setDelay(500));
        menuBuilder.addOption("Fast", () -> setDelay(1000));
    }

    public void setDelay(int delayInMilliseconds) {
        if (tick != null) {
            tick.cancel();
            tick = null;
        }

        if (delayInMilliseconds > 0) {
            tick = new TimerTask() {
                @Override
                public void run() {
                    tick();
                }
            };
            clock.scheduleAtFixedRate(tick, 0, delayInMilliseconds);
        }
    }

    private void tick() {
        System.out.print("*");
    }

    interface ClockCallback {
        void execute();
    }

    interface MenuBuilder {
        void addOption(String action, ClockCallback callback);
    }
}
