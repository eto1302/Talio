package client.scenes;

import commons.Color;
import commons.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class TaskColorShape {
    @FXML
    private Label backgroundTaskColor;
    @FXML
    private Label fontTaskColor;
    Color color;

    public void delete() {
    }

    public void set(Color color){
        this.color = color;
    }

    public Scene getSceneUpdated(Color color) {
        this.backgroundTaskColor.setStyle("-fx-background-color: " + color.getBackgroundColor() + ";");
        this.fontTaskColor.setStyle("-fx-background-color: " + color.getFontColor() + ";");
        return this.backgroundTaskColor.getScene();
    }
}
