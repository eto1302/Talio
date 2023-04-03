package client.scenes;

import client.Services.ColorService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Color;
import commons.models.IdResponseModel;
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
    private ShowCtrl showCtrl;
    private Color color;
    private ColorService colorService;

    @Inject
    public TaskColorShape(UserData userData, ServerUtils serverUtils){
        this.colorService = new ColorService(userData, serverUtils);
    }

    public void delete() {
        IdResponseModel response = colorService.deleteColor(color);
        if(response.getId() == -1){
            showCtrl.showError(response.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        this.showCtrl.cancel();
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
        if(color.getIsDefault()) this.setDefaultButton.setVisible(false);
        else this.setDefaultButton.setVisible(true);
        return this.gridPane.getScene();
    }

    public void setDefault() {
        IdResponseModel model = colorService.editColor(color.getId(),
                javafx.scene.paint.Color.web(color.getFontColor()),
                javafx.scene.paint.Color.web(color.getBackgroundColor()), true);
        if(model.getId() == -1){
            showCtrl.showError(model.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        showCtrl.cancel();
        showCtrl.showColorPicker();
    }

    public void showEdit(){
        this.showCtrl.showEditColor(color);
    }
}
