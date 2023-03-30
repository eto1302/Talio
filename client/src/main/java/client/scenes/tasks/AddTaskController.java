package client.scenes.tasks;

import client.scenes.ShowCtrl;
import client.scenes.lists.ListShapeCtrl;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;
import commons.Subtask;
import commons.Task;
import commons.models.IdResponseModel;
import commons.sync.TaskAdded;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.ArrayList;

public class AddTaskController {

    private final ShowCtrl showCtrl;
    private final ServerUtils server;
    private ListShapeCtrl controller;
    private Task task;
    @FXML
    private TextField title;
    @FXML
    private TextArea descriptionField;
    private List list;


    @Inject
    private UserData userData;

    @Inject
    public AddTaskController (ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        server=serverUtils;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void addTask() {
        String title = this.title.getText();
        String description = this.descriptionField.getText();
        task.setTitle(title);
        task.setDescription(description);
        java.util.List<Task> tasks = server.getTaskByList(list.getId());
        task.setIndex(tasks.size());

        IdResponseModel model = userData.updateBoard(new
                TaskAdded(list.getBoardId(), list.getId(), task));
        if(model.getId() == -1){
            showCtrl.showError(model.getErrorMessage());
            showCtrl.cancel();
            return;
        }

        showCtrl.cancel();
    }


    public void setup(ListShapeCtrl controller, commons.List list) {
        this.task = Task.create(null, null, list.getId(), new ArrayList<Subtask>());
        this.controller=controller;
        this.list=list;
    }
}
