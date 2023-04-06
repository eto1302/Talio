package client.scenes;

import client.Services.ColorService;
import client.user.UserData;
import client.utils.ServerUtils;
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
    public AddTaskColor(UserData userData, ServerUtils serverUtils) {
        this.colorService = new ColorService(userData, serverUtils);
    }

    public void add() {
        IdResponseModel model =  this.colorService.addColor(
                backgroundColor.getValue(), fontColor.getValue());

        if (model.getId() < 0) {
            showCtrl.cancel();
            showCtrl.showError(model.getErrorMessage());
            return;
        }
        showCtrl.cancel();
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
