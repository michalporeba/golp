package com.michalporeba.golf;

import javafx.application.Application;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
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
        Menu menu1 = new Menu("Hello");
        menuBar.getMenus().add(menu1);

        VBox root = new VBox(menuBar);


        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }
}
