package client.scenes;

import client.user.UserData;
import commons.Color;
import commons.models.IdResponseModel;
import commons.sync.ColorAdded;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;

import javax.inject.Inject;

public class AddTaskColor {
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker fontColor;
    @Inject
    private UserData userData;
    @Inject
    private ShowCtrl showCtrl;

    public AddTaskColor() {
    }

    public void add() {
        Color color = Color.create(colorToHex(backgroundColor.getValue()),
                colorToHex(fontColor.getValue()));
        if(this.userData.getCurrentBoard().getColors().size() == 2) color.setIsDefault(true);
        IdResponseModel model = userData.updateBoard(new ColorAdded(
                this.userData.getCurrentBoard().getId(), color));
        userData.openBoard(this.userData.getCurrentBoard().getId());
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

    /**
     * Returns a hexadecimal string representation of javafx.scene.paint.Color.
     * @param color the color to be transformed
     * @return string representation of the color.
     */
    private String colorToHex(javafx.scene.paint.Color color){
        String hexString = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        return hexString;
    }
}
