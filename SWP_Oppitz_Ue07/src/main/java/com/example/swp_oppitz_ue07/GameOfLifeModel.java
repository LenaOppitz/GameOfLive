package com.example.swp_oppitz_ue07;

import javafx.geometry.Dimension2D;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// note: col -> width
//       row -> height

public class GameOfLifeModel {
    Dimension2D board;
    Map<Integer, List<GameOfLiveCell>> cells;
   private Boolean isRunning = false;
    private Timer timer;

    // this is used to store the state for btn restore
    public Map<Integer, List<GameOfLiveCell>> saveCellsState;

    // constructor
    public GameOfLifeModel(Dimension2D board, Map<Integer, List<GameOfLiveCell>> cells){
        this.board = board;
        this.cells = cells;

        saveCellsState = new HashMap<>();
        initialize(saveCellsState);
    }
    // create HashMap with size of board and set all Cells on state not alive
    private void initialize(Map<Integer, List<GameOfLiveCell>> myHashMap) {
        for (int col = 0; col < board.getWidth(); col++) {
            for (int row = 0; row < board.getHeight() ; row++) {
                GameOfLiveCell cell = new GameOfLiveCell();
                myHashMap.computeIfAbsent(row, c -> new ArrayList<>());
                myHashMap.get(row).add(cell);
                myHashMap.get(row).get(col).setAliveProperty(false);
            }
        }
    }
    // logic
    public void computeNextGeneration() {
        Map<Integer, List<GameOfLiveCell>> nextGen = new HashMap<>();
        initialize(nextGen);
        System.out.println( "Size: " + cells.size() + "; " + cells.get(0).size());
        printGrid(cells);
        System.out.println();
        System.out.println( "Size nextGen: " + nextGen.size() + "; " + nextGen.get(0).size());
        printGrid(nextGen);

        // iterate thought board and apply logic
        for (int col = 0; col < board.getWidth(); col++) {
            for (int row = 0; row < board.getHeight() ; row++) {
                GameOfLiveCell currentCell = cells.get(row).get(col);
                int liveNeighbors = countLiveNeighbors(row, col);
                // set cell alive when it has three neighbours
                if (!currentCell.isAliveProperty() && liveNeighbors == 3) {
                    nextGen.get(row).get(col).setAliveProperty(true);
                // cells stays alive when it has 2 or 3 neighbours
                }else if(currentCell.isAliveProperty() && (liveNeighbors == 2 || liveNeighbors == 3)){
                        nextGen.get(row).get(col).setAliveProperty(true);
                }else {
                    // cell is dead
                    nextGen.get(row).get(col).setAliveProperty(false);
                }
            }
        }
        System.out.println();
        printGrid(nextGen);
        updateHashMap(nextGen, cells);
        System.out.println();
        printGrid(cells);
    }

    private void updateHashMap(Map<Integer, List<GameOfLiveCell>> src, Map<Integer, List<GameOfLiveCell>> mapToUpdate){
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                mapToUpdate.get(row).get(col).setAliveProperty(src.get(row).get(col).isAliveProperty());
            }
        }
    }

    private int countLiveNeighbors(int col, int row) {
        // edge areas are ignored
        int sum = 0;
        System.out.println(col + " " + row);
        if(col-1 >= 0 && row-1 >= 0 && col +1 < cells.size() && row+1 < cells.get(0).size()) {
            if (this.cells.get(col - 1).get(row - 1).isAliveProperty()) {
                sum++;
            }
            if (this.cells.get(col - 1).get(row).isAliveProperty()){
                sum++;
            }
            if (this.cells.get(col - 1).get(row + 1).isAliveProperty()) {
                sum++;
            }
            if (this.cells.get(col).get(row - 1).isAliveProperty()) {
                sum++;
            }
            if (this.cells.get(col).get(row + 1).isAliveProperty()) {
                sum++;
            }
            if (this.cells.get(col + 1).get(row - 1).isAliveProperty()) {
                sum++;
            }
            if (this.cells.get(col +1).get(row).isAliveProperty()) {
                sum++;
            }
            if (this.cells.get(col +1).get(row+1).isAliveProperty()) {
                sum++;
            }
        }
        else {
            System.out.println("boundaries");
        }
        return sum;
    }
    // implement logic of Buttons:

    // start new Thread and compute every 500 ms a new generation
    public void runGameOfLife() {
        if (!isRunning) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    computeNextGeneration();
                }
            }, 0, 500);
            isRunning = true;
        }
    }

    public void stopGameOfLife() {
        if (isRunning) {
            timer.cancel();
            isRunning = false;
        }
    }

    public void clearGameOfLife() {
        for (int col = 0; col < board.getHeight(); col++) {
            for (int row = 0; row < board.getWidth() ; row++) {
                cells.get(col).get(row).setAliveProperty(false);
            }
        }
    }

    public void resetGameOfLife() {
        updateHashMap(saveCellsState, cells);
    }

    public void updateSaveCells() {
        updateHashMap(cells, saveCellsState);
    }

    // writes file in project folder
    public void saveGame() throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeString = dtf.format(now);
        String filename = "GameOfLife_" + timeString + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<Integer, List<GameOfLiveCell>> entry : cells.entrySet()) {
                List<GameOfLiveCell> cellList = entry.getValue();
                for (GameOfLiveCell cell : cellList) {
                    writer.write(cell.isAliveProperty() ? "1" : "0");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new IOException("Failed to save game: " + e.getMessage());
        }
    }

    // file has to be valid
    public void loadGame(String filename) throws IOException, ClassNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNr = 0;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\\s", "");
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '0') {
                        cells.get(lineNr).get(i).setAliveProperty(false);
                    }
                    else if (line.charAt(i) == '1') {
                        cells.get(lineNr).get(i).setAliveProperty(true);
                    }
                    else {
                        System.err.println("Unexpected letter (loadGame - Model)");
                    }
                }
                lineNr++;
            }
        } catch (IOException e) {
            throw new IOException("Failed to save game: " + e.getMessage());
        }
    }

    // just for testing
    public void printBoard(){
        for (int col = 0; col < board.getHeight(); col++) {
            for (int row = 0; row < board.getWidth() ; row++) {
                System.out.println(cells.get(col).get(row));
                cells.get(col).get(row).setAliveProperty(true);
            }
        }
    }

    // just for testing
    public void printGrid(Map<Integer, List<GameOfLiveCell>> map) {
        for (List<GameOfLiveCell> row : map.values()) {
            for (GameOfLiveCell cell : row) {
                System.out.print(cell.isAliveProperty() ? "1" : "0");
            }
            System.out.println();
        }
    }
}






