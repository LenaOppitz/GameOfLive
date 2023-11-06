package com.example.swp_oppitz_ue07;

import javafx.beans.binding.When;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameOfLifeView extends BorderPane {
    private static int COLS = 20;
    private static int ROWS = 30;

    public Button nextButton;
    public Button runButton;
    public Button stopButton;
    public Button resetButton;
    public Button clearButton;
    public Button loadButton;

    private final Dimension2D board;    // a 2D board with height and width
    private final Map<Integer, List<GameOfLiveCell>> cells = new HashMap<>();  // data of board
    private final GameOfLiveController controller;

    public Dimension2D getBoard() {
        return board;
    }

    public  Map<Integer, List<GameOfLiveCell>> getCells(){
        return cells;
    }

    public GameOfLifeView(GameOfLiveController controller){
        this.controller = controller;

        // default size
        this.board = new Dimension2D(COLS, ROWS);
    }

    public void init() {
        for (int col = 0; col <board.getWidth() ; col++) {
            for (int row = 0; row <board.getHeight() ; row++) {
                GameOfLiveCell cell = new GameOfLiveCell();
                cell.setUserData(new Coordinate(col, row));
                cell.setFill(Color.LIGHTGRAY);

                //setzen eines Listeners
                cell.fillProperty().bind(
                        new When(cell.alivePropertyProperty()).then(Color.DARKVIOLET).otherwise(Color.LIGHTGRAY));
                cell.setOnMouseClicked(this::mouseClickHandler);

                //Node Border Pane hinzufügen
                this.cells.computeIfAbsent(row, c->new ArrayList<>());
                this.cells.get(row).add(cell);
                this.getChildren().add(cell);

            }
        }
        displayContainer();

        // in Task named Step
        nextButton = new Button("Next");
        nextButton.setOnAction(e -> controller.nextButtonClicked());

        runButton = new Button("Run");
        runButton.setOnAction(e -> controller.runButtonClicked());

        stopButton = new Button("Stop");
        stopButton.setOnAction(e -> controller.stopButtonClicked());
        stopButton.setDisable(true);

        resetButton = new Button("Reset");
        resetButton.setOnAction(e -> controller.resetButtonClicked());

        clearButton = new Button("Clear");
        clearButton.setOnAction(e -> controller.clearButtonClicked());

        loadButton = new Button("Load");
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Game of Life File");
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                controller.loadGameFromFile(file);
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> controller.saveButtonClicked());

        HBox hbox = new HBox();
        hbox.getChildren().addAll(nextButton, runButton, stopButton, resetButton, clearButton, saveButton, loadButton);
        hbox.setSpacing(10);
        this.setBottom(hbox);

        //resizeable
        this.widthProperty().addListener((observable, oldValue, newValue) -> displayContainer());
        this.heightProperty().addListener((observable, oldValue, newValue) -> displayContainer());
    }

    private void displayContainer() {
        //Vorteil von 4 Ecken, zeichnen
        for (int col = 0; col <board.getWidth() ; col++) {
            for (int row = 0; row <board.getHeight() ; row++) {
                GameOfLiveCell cell = this.cells.get(row).get(col);
                //
                cell.setX(this.getScene().getWidth() /board.getWidth() * col);
                cell.setY(this.getScene().getHeight() /board.getHeight() * row);
                cell.setWidth(this.getScene().getWidth() / board.getWidth()-1);
                cell.setHeight(this.getScene().getHeight() / board.getHeight()-1);
            }
        }
    }

    private void mouseClickHandler(MouseEvent mouseEvent) {
        GameOfLiveCell cell = (GameOfLiveCell)mouseEvent.getSource();
        boolean isAlive = !cell.isAliveProperty();
        cell.setAliveProperty(isAlive);
        System.out.println(cell + " - " + isAlive);
        controller.getModel().updateSaveCells();
    }

    private class Coordinate{
        int column;
        int row;

        public Coordinate(int column, int row) {
            this.column = column;
            this.row = row;
        }
    }
}
