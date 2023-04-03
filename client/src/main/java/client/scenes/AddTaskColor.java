package client.scenes;

import client.user.Services.ColorService;
import client.user.UserData;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;

import javax.inject.Inject;

public class AddTaskColor {
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker fontColor;
    @Inject
    private ShowCtrl showCtrl;

    private ColorService colorService;

    @Inject
    public AddTaskColor(UserData userData) {
        this.colorService = new ColorService(userData);
    }

    public void add() {
        IdResponseModel model =  this.colorService.addTaskColor(
                backgroundColor.getValue(), fontColor.getValue());

        if (model.getId() == -1) {
            showCtrl.showError(model.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        showCtrl.cancel();
        showCtrl.showColorPicker();
    }

    public void cancel(){
        reset();
        showCtrl.cancel();
    }

    public void reset(){
        this.backgroundColor.setValue(javafx.scene.paint.Color.web("#000000"));
        this.backgroundColor.setValue(javafx.scene.paint.Color.web("#FFFFFF"));
    }
}
