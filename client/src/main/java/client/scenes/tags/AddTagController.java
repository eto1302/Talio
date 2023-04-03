package client.scenes.tags;

import client.scenes.*;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import commons.models.IdResponseModel;
import commons.sync.TagCreated;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import commons.Tag;

import javax.inject.Inject;

/**
 * AddTagController
 * Controller that handles creating tags
 *
 */

public class AddTagController {

    private final ShowCtrl showCtrl;
    private UserData userData;
    private ServerUtils serverUtils;

    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField textField;
    @FXML
    private ColorPicker colorPicker;
    private commons.Task task;

    @Inject
    public AddTagController(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.showCtrl=showCtrl;
        this.serverUtils = serverUtils;
        this.userData = userData;
    }

    public void setup(Task task) {
        this.task = task;
    }

    /**
     * addTag
     * Creates tag, adds it to the current board, and then updates the board.
     * See commons.sync.TagCreated.apply() for update method
     */
    public void addTag() {
        String textColor = (getBrightness(colorPicker.getValue()) < 130) ? "#FFFFFF" : "#000000";
        String backgroundColor = colorToHex(colorPicker.getValue());
        commons.Color color = commons.Color.create(textColor, backgroundColor);

        String tagName = this.textField.getText();
        Tag tag = Tag.create(tagName, color);

        Board current = userData.getCurrentBoard();
        IdResponseModel resp = userData.updateBoard(new TagCreated(current.getId(), tag, current));

        if (resp.getId() == -1) {
            showCtrl.showError(resp.getErrorMessage());
            showCtrl.cancel();
        }
        cancel();
    }

    public void cancel(){
        showCtrl.closePopUp();
    }

    /**
     * Returns a hexadecimal string representation of javafx.scene.paint.Color.
     * @param color the color to be transformed
     * @return string representation of the color.
     */
    private String colorToHex(Color color){
        String hexString = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        return hexString;
    }

    /**
     * Returns a double representing the perceived brightness of the color
     * @param color javaFx color instance to be converted
     * @return double [0,255] representation of the brightness
     */
    private double getBrightness(Color color){
        return Math.sqrt(Math.pow(0.299*color.getRed()*255,2)
                            + Math.pow(0.587*color.getGreen()*255, 2)
                            + Math.pow(0.114* color.getBlue()*255, 2)
        );
    }
}
