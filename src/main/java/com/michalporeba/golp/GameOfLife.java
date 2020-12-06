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

        MainMenu.getInstance().attachTo(menuBar);
        // MenuUi.getInstance().addActio("abc.def.ghi", null, null, null);
        ToolBar toolBar = new ToolBar();
        Label label = new Label("+");
        VBox root = new VBox(menuBar, toolBar, label);

        // the universe represents the game's business logic
        Universe universe = new Universe();

        Clock.getInstance().addMenuTo(MainMenu.getInstance());

        ClockToolbar clockToolbar = new ClockToolbar(toolBar);
        Clock.getInstance().createUiWith(clockToolbar);
        Clock.getInstance().addObserver(clockToolbar);
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

    class ClockToolbar implements Clock.ActionObserver, Clock.UiBuilder  {
        ToolBar toolBar;
        Button slowDown;
        Button stop;
        Button play;
        Button tick;
        Button speedUp;

        public ClockToolbar(ToolBar toolBar) {
            this.toolBar = toolBar;
            slowDown = new Button("-");
            toolBar.getItems().add(slowDown);
            stop = new Button("[]");
            stop.setDisable(true);
            toolBar.getItems().add(stop);
            tick = new Button(">");
            toolBar.getItems().add(tick);
            play = new Button(">>");
            toolBar.getItems().add(play);
            speedUp = new Button("+");
            toolBar.getItems().add(speedUp);
        }

        @Override
        public void addOption(Clock.Actions action, Runnable callback) {
            switch(action) {
                case Pause: addCallbackToButton(stop, callback); break;
                case Start: addCallbackToButton(play, callback); break;
                case Tick: addCallbackToButton(tick, callback); break;
                case SpeedUp: addCallbackToButton(speedUp, callback); break;
                case SlowDown: addCallbackToButton(slowDown, callback); break;

            }
        }

        private void addCallbackToButton(Button button, Runnable callback) {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    callback.run();
                }
            });
        }

        @Override
        public void actionInvoked(Clock.Actions action) {
            switch (action) {
                case Pause:
                    stop.setDisable(true);
                    play.setDisable(false);
                    tick.setDisable(false);
                    break;
                case Start:
                    stop.setDisable(false);
                    play.setDisable(true);
                    tick.setDisable(true);
                    break;
            }
        }
    }
}
