package com.example.swp_oppitz_ue07;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Run this class to start the application!
public class GameOfLifeApplication extends Application {

    private static GameOfLiveController controller;

    public static void main(String[] args) {
        controller = new GameOfLiveController();
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Of Live");
        Scene scene = new Scene(controller.getView(), 355, 400);        // create new Scene and set size
        controller.getView().init();
        primaryStage.setScene(scene);
        primaryStage.show();        // show GUI
    }

    @Override
    public void stop() throws Exception {
        controller.getModel().stopGameOfLife();
    }



}