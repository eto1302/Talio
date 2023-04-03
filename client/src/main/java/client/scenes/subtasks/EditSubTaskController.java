package client.scenes.subtasks;

import client.scenes.ShowCtrl;
import commons.Subtask;
import javafx.fxml.FXML;

import javax.inject.Inject;
import javafx.scene.control.TextArea;

public class EditSubTaskController {
    @FXML
    private TextArea description;
    private ShowCtrl showCtrl;
    private Subtask subtask;

    @Inject
    public EditSubTaskController(ShowCtrl showCtrl){
        this.showCtrl = showCtrl;
    }

    public void cancel(){
        showCtrl.closePopUp();
    }

    public void setup(Subtask subtask){
        this.subtask = subtask;
    }

    public void editSubTask() {
        String name = this.description.getText();

    }
}

