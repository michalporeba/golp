package com.michalporeba.golp;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class MainMenu implements ActionableUi {
    // a Facade over the menu system
    private static MainMenu instance;
    private MenuBar menuBar;
    private final Map<String, Menu> menus = new HashMap<>();
    private final Map<Object, Map<Object, MenuItem>> menuItems = new HashMap<>();

    private MainMenu(){}

    public static synchronized MainMenu getInstance() {
        if (instance == null)
            instance = new MainMenu();
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
        if (path.isBlank())
            path = owner.getClass().getName();

        String currentPath = "";
        Menu currentMenu = null;

        for(var p : path.split("\\.")) {
            currentPath += (currentMenu == null ? "" : ".") + p;

            if (menus.containsKey(currentPath)) {
                currentMenu = menus.get(currentPath);
                continue;
            }

            Menu menu = new Menu(p);

            if (currentMenu == null) {
                menuBar.getMenus().add(menu);
            } else {
                currentMenu.getItems().add(menu);
            }

            menus.put(currentPath, menu);
            currentMenu = menu;
        }

        if (!menuItems.containsKey(owner))
            menuItems.put(owner, new HashMap<>());

        if (!menuItems.get(owner).containsKey(action)) {
            var menuItem = new MenuItem(action.toString());
            menuItem.setOnAction(e -> {
                callback.run();
            });
            currentMenu.getItems().add(menuItem);
            menuItems.get(owner).put(action, menuItem);
        }
    }

    @Override
    public void enable(Object owner, Object action) {
        if (!actionExists(owner, action))
            return;
        menuItems.get(owner).get(action).setDisable(false);
    }

    @Override
    public void disable(Object owner, Object action) {
        if (!actionExists(owner, action))
            return;
        menuItems.get(owner).get(action).setDisable(true);
    }

    private boolean actionExists(Object owner, Object action) {
        return (
            menuItems.containsKey(owner)
            && menuItems.get(owner).containsKey(action)
        );
    }
}
