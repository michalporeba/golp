package com.michalporeba.golp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class MenuUi implements ActionableUi {
    // a Facade over the menu system
    private static MenuUi instance;
    private MenuBar menuBar;
    private final Map<String, Menu> menus = new HashMap<>();
    private final Map<Object, Map<Object, MenuItem>> menuItems = new HashMap<>();

    private MenuUi(){}

    public static synchronized MenuUi getInstance() {
        if (instance == null)
            instance = new MenuUi();
        return instance;
    }

    public void attachTo(MenuBar menuBar) {
        assert menuBar != null:
                "Cannot attach to menu that doesn't exist!";
        assert this.menuBar == null:
                "The attachTo of Menu can be called only once!";

        this.menuBar = menuBar;
    }

    @Override
    public void addAction(String path, Object owner, Object action, Runnable callback) {
        String currentPath = "";
        for(var p : path.split("\\.")) {
            Menu menu = new Menu(p);
            if (currentPath.isBlank()) {
                menuBar.getMenus().add(menu);
            } else {
                menus.get(currentPath).getItems().add(menu);
            }

            if (!currentPath.isBlank())
                currentPath += ".";
            currentPath += p;

            menus.put(currentPath, menu);
        }

        if (!menuItems.containsKey(owner))
            menuItems.put(owner, new HashMap<>());

        if (!menuItems.get(owner).containsKey(action)) {
            var menuItem = new MenuItem(action.toString());
            menuItem.setOnAction(e -> { callback.run(); });
            menuItems.get(owner).put(action, menuItem);
        }
    }

    @Override
    public void enable(Object owner, Object action) {

    }

    @Override
    public void disable(Object owner, Object action) {

    }
}
