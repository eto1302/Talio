package client.scenes;

import client.user.UserData;
import commons.Color;
import commons.models.ColorEditModel;
import commons.models.IdResponseModel;
import commons.sync.ColorEdited;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;
import java.util.Optional;

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
        else this.setDefaultButton.setVisible(true);
        return this.gridPane.getScene();
    }

    public void setDefault() {
        Optional<Color> currentDefault = this.userData.getCurrentBoard().getTaskColors()
                .stream().filter(x -> x.isDefault()).findFirst();
        ColorEditModel editCurrentDefault = new ColorEditModel(
                currentDefault.get().getBackgroundColor(),
                currentDefault.get().getFontColor(), false);
        IdResponseModel currentDefaultModel = userData.updateBoard(new ColorEdited(
                this.userData.getCurrentBoard().getId(),
                currentDefault.get().getId(), editCurrentDefault));
        if (currentDefaultModel.getId() == -1) {
            showCtrl.showError(currentDefaultModel.getErrorMessage());
            showCtrl.cancel();
            return;
        }

        ColorEditModel edit = new ColorEditModel(
                color.getBackgroundColor(), color.getFontColor(), true);
        IdResponseModel model = userData.updateBoard(new ColorEdited(
                this.userData.getCurrentBoard().getId(), color.getId(), edit));
        userData.openBoard(this.userData.getCurrentBoard().getId());
        if (model.getId() == -1) {
            showCtrl.showError(model.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        showCtrl.cancel();
        showCtrl.showColorPicker();
    }
}
