package client.scenes;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.ArrayList;

public class AddTaskController {

    private final ShowCtrl showCtrl;
    private final ServerUtils server;
    private ListShapeCtrl controller;
    private Stage primaryStage;
    private Task task;
    @FXML
    private TextField title;
    @FXML
    private TextArea descriptionField;
    @FXML
    private VBox subtaskBox, tagBox;
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

        IdResponseModel model = userData.updateBoard(new TaskAdded(list.getId(), task));
        if(model.getId() == -1){
            showCtrl.showError(model.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        Task taskTest = server.getTask(model.getId());
        showCtrl.addTask(taskTest, controller, primaryStage);
        showCtrl.cancel();
    }

    public void showAddSubTask(){
        showCtrl.showAddSubTask(task);
    }

    public void showAddTag() {
        showCtrl.showAddTag(task);
    }

    public void setup(ListShapeCtrl controller, Stage primaryStage, commons.List list) {
        this.task = Task.create(null, null, list.getId(), new ArrayList<Subtask>());
        this.controller=controller;
        this.primaryStage=primaryStage;
        this.list=list;
    }
}
