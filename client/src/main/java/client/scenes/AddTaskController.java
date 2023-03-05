package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;

import javax.inject.Inject;

public class AddTaskController {

    private final ShowCtrl showCtrl;

    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;
    @FXML
    private TextArea textArea;
    @FXML
    private ColorPicker colorPicker;

    @Inject
    public AddTaskController (ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }

    public void cancel(){
        showCtrl.cancel();
    }
}
