package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AddBoardController {

    private final ShowCtrl showCtrl;

    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField nameField;
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker fontColor;

    @Inject
    public AddBoardController (ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void addBoard(){
        System.out.println(nameField.getText());
        System.out.println(backgroundColor.getValue());
        System.out.println(fontColor.getValue());
        showCtrl.showAddBoard();
    }
}
