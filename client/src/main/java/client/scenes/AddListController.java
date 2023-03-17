package client.scenes;

import client.utils.ServerUtils;
import commons.List;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.ArrayList;


public class AddListController {

    @FXML
    private TextField nameField;
    @FXML
    private ColorPicker backgroundColor, fontColor;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;

    private final ShowCtrl showCtrl;
    private final ServerUtils serverUtils;

    @Inject
    public AddListController(ShowCtrl showCtrl, ServerUtils serverUtils) {
        this.showCtrl = showCtrl;
        this.serverUtils=serverUtils;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    /**
     * Creates a list based on user input
     */
    public void addList() {
        String backgroundColor = colorToHex(this.backgroundColor.getValue());
        String fontColor = colorToHex(this.fontColor.getValue());

        List list = List.create(nameField.getText(),
                backgroundColor, fontColor, new ArrayList<Task>());

        serverUtils.addlist(list, 1);
        showCtrl.addList(list);
        showCtrl.cancel();
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
