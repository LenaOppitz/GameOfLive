package com.example.swp_oppitz_ue07;

import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameOfLiveController {
    private GameOfLifeView view;    // GUI
    private GameOfLifeModel model;  // contains logic

    // constructor
    public GameOfLiveController(){
        this.view = new GameOfLifeView(this);
        this.model = new GameOfLifeModel(this.view.getBoard(), this.view.getCells());
    }

    public GameOfLifeModel getModel() {
        return this.model;
    }

    public GameOfLifeView getView() {
        return this.view;
    }

    public void nextButtonClicked() {
        System.out.println("NextBtn clicked");
        Platform.runLater(new Runnable() {
            public void run() {
                getModel().computeNextGeneration();
            }
        });
    }

    // call functionality of model:

    public void runButtonClicked() {
        System.out.println("RunBtn clicked");
        Platform.runLater(new Runnable() {
            public void run() {
                getModel().runGameOfLife();
            }
        });
        this.view.stopButton.setDisable(false);
        this.view.runButton.setDisable(true);
    }

    public void setView(GameOfLifeView view) {
        this.view = view;
    }

    public void setModel(GameOfLifeModel model) {
        this.model = model;
    }

    public void stopButtonClicked() {
        System.out.println("StopBtn clicked");
        Platform.runLater(new Runnable() {
            public void run() {
                getModel().stopGameOfLife();
            }
        });
        this.view.stopButton.setDisable(true);
        this.view.runButton.setDisable(false);
    }

    public void resetButtonClicked() {
        System.out.println("ResetBtn clicked");
        Platform.runLater(new Runnable() {
            public void run() {
                getModel().resetGameOfLife();
            }
        });

    }

    public void clearButtonClicked() {
        System.out.println("ResetBtn clicked");
        Platform.runLater(new Runnable() {
            public void run() {
                getModel().clearGameOfLife();
            }
        });
    }


    public void saveButtonClicked() {
        try {
            this.model.saveGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGameFromFile(File file) {
        System.out.println(file.getPath());
        try {
            this.model.loadGame(file.getPath());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
