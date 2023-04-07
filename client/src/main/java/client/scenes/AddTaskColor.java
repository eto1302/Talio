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
    private final UserData userData;

    @Inject
    public AddTaskColor(UserData userData, ServerUtils serverUtils) {
        this.colorService = new ColorService(userData, serverUtils);
        this.userData = userData;
    }

    public void add() {
        IdResponseModel model =  this.colorService.addColor(
                backgroundColor.getValue(), fontColor.getValue());

        if (model.getId() == -1) {
            showCtrl.showError(model.getErrorMessage());
            return;
        }
        cancel();
        if(!showCtrl.isColorPickerOpen())
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
