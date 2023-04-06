package client.scenes;

import client.Services.ColorService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Color;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;

import javax.inject.Inject;

public class EditColor {
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker fontColor;
    private ColorService colorService;
    private Color color;
    @Inject
    private ShowCtrl showCtrl;

    @Inject
    public EditColor(UserData userData, ServerUtils serverUtils) {
        this.colorService = new ColorService(userData, serverUtils);
    }

    public void setup(Color color){
        this.color = color;
        backgroundColor.setValue(javafx.scene.paint.Color.web(color.getBackgroundColor()));
        fontColor.setValue(javafx.scene.paint.Color.web(color.getFontColor()));
    }

    public void edit() {
        IdResponseModel model = colorService.editColor(color.getId(),
                backgroundColor.getValue(), fontColor.getValue(), color.getIsDefault());
        if(model.getId() < 0){
            this.showCtrl.cancel();
            this.showCtrl.showError(model.getErrorMessage());
            return;
        }
        showCtrl.cancel();
        showCtrl.showColorPicker();
    }
}
