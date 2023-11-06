package com.example.swp_oppitz_ue07;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Rectangle;

public class GameOfLiveCell extends Rectangle{
    // used to create a board of cells
    // saves state of cells
    private final BooleanProperty aliveProperty = new SimpleBooleanProperty();
    public boolean isAliveProperty() {
        return aliveProperty.get();
    }
    public BooleanProperty alivePropertyProperty() {
        return aliveProperty;
    }
    public void setAliveProperty(boolean aliveProperty) {
        this.aliveProperty.set(aliveProperty);
    }
}
