package com.michalporeba.golf;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameOfLife extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game of Life");

        MenuBar menuBar = new MenuBar();
        VBox root = new VBox(menuBar);

        ClockMenuBuilder clockMenuBuilder = new ClockMenuBuilder(menuBar);
        Clock.getInstance().createUiWith(clockMenuBuilder);

        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }

    class ClockMenuBuilder implements Clock.MenuBuilder {
        MenuBar menuBar = null;
        Menu clockMenu = null;

        public ClockMenuBuilder(MenuBar menuBar) {
            this.menuBar = menuBar;
        }

        @Override
        public void addOption(String action, Clock.ClockCallback callback) {
            if (clockMenu == null) {
                clockMenu = new Menu("Clock");
                menuBar.getMenus().add(clockMenu);
            }

            MenuItem menu = new MenuItem(action);
            menu.setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent event) {
                    callback.execute();
                }
            });
            clockMenu.getItems().add(menu);
        }
    }
}
