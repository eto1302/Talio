package client.scenes.tags;

import client.scenes.*;
import client.scenes.tasks.EditTaskController;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import commons.sync.TagCreated;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import commons.Tag;

import javax.inject.Inject;

public class AddTagController {

    private final ShowCtrl showCtrl;
    @Inject
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
    private EditTaskController controller;
    private Stage primaryStage;
    private commons.Task task;
    private Board board;

    @Inject
    public AddTagController(ShowCtrl showCtrl, ServerUtils serverUtils) {
        this.showCtrl=showCtrl;
        this.serverUtils = serverUtils;
    }


    public void setup(Task task) {
        this.task = task;
        this.board = userData.getCurrentBoard();
    }

    public void addTag() {
        String textColor = (colorPicker.getValue().getBrightness() < 0.7) ? "#FFFFFF" : "#000000";
        String backgroundColor = colorToHex(colorPicker.getValue());
        commons.Color color = commons.Color.create(textColor, backgroundColor);
        String tagName = this.textField.getText();
        Tag tag = Tag.create(tagName, color);

        Board current = userData.getCurrentBoard();
        userData.updateBoard(new TagCreated(current.getId(), tag, current));
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
}
