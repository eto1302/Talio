package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AddCardController {

    @FXML
    private TextField nameField;
    @FXML
    private ColorPicker color;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;

    private final MainCtrl mainCtrl;

    @Inject
    public AddCardController(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void cancel(){
       mainCtrl.cancel();
    }


}
