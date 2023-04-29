package com.example.blokus1;

import controller.MenuController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.MenuView;

public class HelloApplication extends Application {
    private MenuView menuView;
    private MenuController menuController;

    @Override
    public void start(Stage primaryStage) {
        menuView = new MenuView(primaryStage);
        menuController = new MenuController(menuView);
    }

    public static void main(String[] args) {
        launch();


    }
}