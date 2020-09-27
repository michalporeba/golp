package com.michalporeba.golp;

class Universe implements Clock.TickObserver {
    public void tick() {
        System.out.print(".");
    }
}
