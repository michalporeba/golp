package com.michalporeba.golp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The GameOfLife entry point
 */
public class GameOfLife extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game of Life");

        MenuBar menuBar = new MenuBar();
        Label label = new Label("+");
        VBox root = new VBox(menuBar, label);

        Universe universe = new Universe();

        ClockMenuBuilder clockMenuBuilder = new ClockMenuBuilder(menuBar);
        Clock.getInstance().createUiWith(clockMenuBuilder);
        Clock.getInstance().addObserver(universe);
        Clock.getInstance().addObserver(new Clock.TickObserver() {
            //this anonymous class implements the concrete observer for the clock
            @Override
            public void tick() {
                Platform.runLater(() -> {
                    if (label.getText().length()==0) {
                        label.setText("*");
                    } else {
                        label.setText("");
                    }
                });
            }
        });

        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }

    /**
     * Implements the Menu Builder for the Clock
     * A concrete builder from the builder pattern
     */
    class ClockMenuBuilder implements Clock.MenuBuilder {
        MenuBar menuBar = null;
        Menu clockMenu = null;

        public ClockMenuBuilder(MenuBar menuBar) {
            this.menuBar = menuBar;
        }

        @Override
        public void addOption(String action, Runnable callback) {
            ensureMenuExists();

            MenuItem menu = new MenuItem(action);
            menu.setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent event) {
                    callback.run();
                }
            });
            clockMenu.getItems().add(menu);
        }

        public void addGroup(String name) {
            ensureMenuExists();

            if (clockMenu.getItems().size() == 0)
                return;

            SeparatorMenuItem menu = new SeparatorMenuItem();
            clockMenu.getItems().addAll(menu);
        }

        private void ensureMenuExists() {
            if (clockMenu == null) {
                clockMenu = new Menu("Clock");
                menuBar.getMenus().add(clockMenu);
            }
        }
    }
}
