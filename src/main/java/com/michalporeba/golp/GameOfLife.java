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

    // pass args and start the Java FX application
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game of Patterns");

        MenuBar menuBar = new MenuBar();
        ToolBar toolBar = new ToolBar();

        MainMenu.getInstance().attachTo(menuBar);
        MainToolbar.getInstance().attachTo(toolBar);

        Label label = new Label("+");
        VBox root = new VBox(menuBar, toolBar, label);

        // the universe represents the game's business logic
        Universe universe = new Universe();

        Clock.getInstance().addToMenu(MainMenu.getInstance());
        Clock.getInstance().addToToolbar(MainToolbar.getInstance());

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

        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
}
