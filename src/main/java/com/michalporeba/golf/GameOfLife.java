package com.michalporeba.golf;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameOfLife extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game of Life");

        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }
}
