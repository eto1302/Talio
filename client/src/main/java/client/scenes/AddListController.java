package client.scenes;

import commons.List;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    @Inject
    public AddListController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    /**
     * Creates a list based on user input
     */
    public void addList() {
        String backgroundColor = this.backgroundColor.getValue().getRed()+"+"+
                this.backgroundColor.getValue().getGreen()+"+"+this.backgroundColor.getValue().getBlue();
        String fontColor = this.fontColor.getValue().getRed()+"+"+
                this.fontColor.getValue().getGreen()+"+"+this.fontColor.getValue().getBlue();
        List list = List.create(nameField.getText(), backgroundColor, fontColor, new ArrayList<Task>());
        showCtrl.addList(list);
        showCtrl.cancel();
    }

}
