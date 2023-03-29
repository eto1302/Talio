package client.scenes;

import client.user.UserData;
import commons.Color;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TaskColorShape {
    @FXML
    private Button setDefaultButton;
    @FXML
    private Label backgroundTaskColor;
    @FXML
    private Label fontTaskColor;

    @FXML
    private UserData userData;
    @FXML
    private ShowCtrl showCtrl;
    private Color color;

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
        return this.backgroundTaskColor.getScene();
    }

    public void setDefault() {
    }
}
