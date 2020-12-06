package com.michalporeba.golp;

import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

public class MainToolbar implements ActionableUi {
    // a Facade over the toolbar system
    private static MainToolbar instance;
    private ToolBar toolBar;
    private final Map<Object, Map<Object, Node>> toolbarNodes = new HashMap<>();

    private MainToolbar() {}

    public static synchronized MainToolbar getInstance() {
        if (instance == null)
            instance = new MainToolbar();
        return instance;
    }

    public void attachTo(ToolBar toolBar) {
        assert toolBar != null:
                "Cannot attach to menu that doesn't exist!";
        assert this.toolBar == null:
                "The attachTo of Menu can be called only once!";

        this.toolBar = toolBar;
    }

    @Override
    public void addAction(String path, Object owner, Object action, Runnable callback) {
        if (!toolbarNodes.containsKey(owner))
            toolbarNodes.put(owner, new HashMap<>());

        if (!toolbarNodes.get(owner).containsKey(action)) {
            var toolbarNode = new Button(action.toString());
            toolbarNode.setOnAction(e -> {
                callback.run();
            });
            toolBar.getItems().add(toolbarNode);
            toolbarNodes.get(owner).put(action, toolbarNode);
        }
    }

    @Override
    public void enable(Object owner, Object action) {
        if (!actionExists(owner, action))
            return;
        toolbarNodes.get(owner).get(action).setDisable(false);
    }

    @Override
    public void disable(Object owner, Object action) {
        if (!actionExists(owner, action))
            return;
        toolbarNodes.get(owner).get(action).setDisable(true);
    }

    private boolean actionExists(Object owner, Object action) {
        return (
            toolbarNodes.containsKey(owner)
            && toolbarNodes.get(owner).containsKey(action)
        );
    }
}
