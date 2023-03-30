package client.scenes;

import client.user.UserData;
import commons.Color;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;

public class TaskColorShape {
    @FXML
    private Button setDefaultButton;
    @FXML
    private Label backgroundTaskColor;
    @FXML
    private Label fontTaskColor;
    @FXML
    private GridPane gridPane;
    @Inject
    private UserData userData;
    @Inject
    private ShowCtrl showCtrl;
    private Color color;

    public TaskColorShape(){
    }

    public void delete() {
        //this.userData.deleteColor(color);
        this.showCtrl.showColorPicker();
    }

    public void set(Color color){
        this.color = color;
    }

    public Scene getSceneUpdated(Color color) {
        this.backgroundTaskColor.setStyle(
                "-fx-background-color: " + color.getBackgroundColor() + ";");
        this.fontTaskColor.setStyle(
                "-fx-background-color: " + color.getFontColor() + ";");
        if(color.isDefault()) this.setDefaultButton.setVisible(false);
        return this.gridPane.getScene();
    }

    public void setDefault() {
    }
}
