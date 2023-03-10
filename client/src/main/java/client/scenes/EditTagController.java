package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class EditTagController {

    private final ShowCtrl showCtrl;

    @FXML
    private Button cancelButton;
    @FXML
    private Button editButton;
    @FXML
    private TextField textField;
    @FXML
    private ColorPicker colorPicker;

    @Inject
    public EditTagController (ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void editTag(){
        //TODO
        showCtrl.cancel();
    }
}
