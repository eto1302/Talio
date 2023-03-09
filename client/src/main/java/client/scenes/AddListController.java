package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AddListController {

    @FXML
    private TextField nameField;
    @FXML
    private ColorPicker color;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;

    private final ShowCtrl showCtrl;

    @Inject
    public AddListController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void showBoard() {showCtrl.showBoard();}

    public void cancel(){
       showCtrl.cancel();
    }


}
