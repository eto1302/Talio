package client.scenes;

import client.user.UserData;
import commons.models.BoardEditModel;
import commons.sync.BoardEdited;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import javax.inject.Inject;
public class EditBoardController {

    @FXML
    private TextField newTitle;

    @FXML
    private ColorPicker newBackground;
    @FXML
    private ColorPicker newFont;
    @Inject
    private ShowCtrl showCtrl;
    @Inject
    private UserData userData;

    @Inject
    public EditBoardController(){

    }
    public void cancel() {
        showCtrl.cancel();
    }

    public void edit() {
        this.userData.updateBoard(
                new BoardEdited(this.userData.getCurrentBoard().getId(),
                        new BoardEditModel(newTitle.getText(),
                                colorToHex(newBackground.getValue()),
                                colorToHex(newFont.getValue()))));
    }

    /**
     * Returns a hexadecimal string representation of javafx.scene.paint.Color.
     * @param color
     * @return string representation of the color.
     */
    private String colorToHex(Color color){
        String hexString = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        return hexString;
    }
}
