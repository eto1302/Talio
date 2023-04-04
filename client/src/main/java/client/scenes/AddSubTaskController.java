package client.scenes;

import client.scenes.ShowCtrl;
import client.scenes.EditTaskController;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;
import commons.Subtask;
import commons.Task;
import commons.models.IdResponseModel;
import commons.sync.SubtaskAdded;
import javafx.fxml.FXML;

import javax.inject.Inject;
import javafx.scene.control.TextArea;

public class AddSubTaskController {
    private final ShowCtrl showCtrl;
    private final ServerUtils server;
    private EditTaskController controller;
    @FXML
    private TextArea description;
    private Task task;

    @Inject
    private UserData userData;

    @Inject
    public AddSubTaskController(ShowCtrl showCtrl, ServerUtils server){
        this.showCtrl = showCtrl;
        this.server = server;
    }

    public void cancel(){
        showCtrl.closePopUp();
    }

    public void setup(EditTaskController controller, Task task){
        this.controller = controller;
        this.task = task;
    }

    public void addSubTask() {
        String name = this.description.getText();
        Subtask subtask = Subtask.create(name, false, task.getId());
        java.util.List<Subtask> subtasks = server.getSubtasksByTask(task.getId());
        subtask.setIndex(subtasks.size());

        List list = server.getList(task.getListID());
        IdResponseModel model = userData.updateBoard(new
                SubtaskAdded(list.getBoardId(), task.getId(), subtask));
        if(model.getId() == -1){
            showCtrl.showError(model.getErrorMessage());
            showCtrl.closePopUp();
            return;
        }
        controller.refresh();
        showCtrl.closePopUp();
    }
}
