package client.scenes;

import commons.Task;
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

    @Inject
    public AddTagController(ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }

    public void setup(Task task) {
        this.task = task;
    }

    public void addTag() {
        commons.Color color = commons.Color.create("#0000000", "#FFFFFF");
        String tagName = this.textField.getText();
        Tag tag = Tag.create(tagName, color);
        showCtrl.addTag(tag, controller, primaryStage);
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
