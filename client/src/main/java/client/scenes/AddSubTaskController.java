package client.scenes;

import client.Services.SubtaskService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Task;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.inject.Inject;

public class AddSubTaskController {
    private final ShowCtrl showCtrl;
    private EditTaskController controller;
    @FXML
    private TextArea description;
    private Task task;
    private SubtaskService subtaskService;
    @Inject
    public AddSubTaskController(ShowCtrl showCtrl, ServerUtils server, UserData userData){
        this.showCtrl = showCtrl;
        this.subtaskService = new SubtaskService(userData, server);
    }

    public void cancel(){
        showCtrl.closePopUp();
    }

    public void setup(EditTaskController controller, Task task){
        this.controller = controller;
        this.task = task;
    }

    public void addSubTask() {
        IdResponseModel response = this.subtaskService.add(description.getText(), task);
        if(response.getId() == -1)
            showCtrl.showError(response.getErrorMessage());
        showCtrl.closePopUp();
    }
}
