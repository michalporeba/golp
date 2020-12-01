package com.michalporeba.golp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
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
        Label label = new Label("+");
        VBox root = new VBox(menuBar, toolBar, label);

        // the universe represents the game's business logic
        Universe universe = new Universe();

        ClockMenuBuilder clockMenuBuilder = new ClockMenuBuilder(menuBar);
        ClockToolbarBuilder clockToolbarBuilder = new ClockToolbarBuilder(toolBar);
        Clock.getInstance().createUiWith(clockMenuBuilder);
        Clock.getInstance().createUiWith(clockToolbarBuilder);
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
    class ClockMenuBuilder implements Clock.UiBuilder {
        MenuBar menuBar = null;
        Menu clockMenu = null;

        public ClockMenuBuilder(MenuBar menuBar) {
            this.menuBar = menuBar;
        }

        @Override
        public void addOption(Clock.Actions action, Runnable callback) {
            ensureMenuExists();

            MenuItem menu = new MenuItem(action.toString());
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

    class ClockToolbarBuilder implements Clock.UiBuilder {
        ToolBar toolBar;
        Button slower;
        Button stop;
        Button play;
        Button tick;
        Button faster;

        public ClockToolbarBuilder(ToolBar toolBar) {
            this.toolBar = toolBar;
            slower = new Button("-");
            toolBar.getItems().add(slower);
            stop = new Button("[]");
            toolBar.getItems().add(stop);
            tick = new Button(">");
            toolBar.getItems().add(tick);
            play = new Button(">>");
            toolBar.getItems().add(play);
            faster = new Button("+");
            toolBar.getItems().add(faster);
        }

        @Override
        public void addGroup(String name) {
            // do nothing, it's not applicable here
            // TODO: in that case perhaps it should not be part of the interface
        }

        @Override
        public void addOption(Clock.Actions action, Runnable callback) {
            switch(action) {
                case Pause: addCallbackToButton(stop, callback); break;
                case Start: addCallbackToButton(play, callback); break;
                case Tick: addCallbackToButton(tick, callback); break;
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
    }
}
