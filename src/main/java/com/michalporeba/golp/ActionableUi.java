package com.michalporeba.golp;

public interface ActionableUi {
    void addAction(String path, Object owner, Object action, Runnable callback);
    void enable(Object owner, Object action);
    void disable(Object owner, Object action);
}
