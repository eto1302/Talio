package client.scenes;

import commons.sync.ListAdded;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
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
    private final ServerUtils server;

    @Inject
    private UserData userData;

    @Inject
    public AddListController(ShowCtrl showCtrl, ServerUtils serverUtils) {
        this.showCtrl = showCtrl;
        this.server=serverUtils;
    }

    public void cancel(){
        reset();
        showCtrl.cancel();
    }

    /**
     * Creates a list based on user input
     */
    public void addList() {
        String backgroundColor = colorToHex(this.backgroundColor.getValue());
        String fontColor = colorToHex(this.fontColor.getValue());

        List list = List.create(nameField.getText(),
                backgroundColor, fontColor, 1, new ArrayList<Task>());

        IdResponseModel model = userData.updateBoard(new ListAdded(1, list));
        if (model.getId() == -1) {
            showCtrl.showError(model.getErrorMessage());
            showCtrl.cancel();
            return;
        }

        List listTest = server.getList(model.getId());
        showCtrl.addList(listTest);
        cancel();
    }

    public void reset() {
        nameField.clear();
        backgroundColor.setValue(Color.WHITE);
        fontColor.setValue(Color.BLACK);
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
