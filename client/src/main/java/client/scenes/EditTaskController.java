package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class EditTaskController {

    private final ShowCtrl showCtrl;
    @FXML
    private TextField title;
    @FXML
    private TextArea descriptionField;
    @FXML
    private VBox subtaskBox, tagBox;
    private commons.Task task;
    private ServerUtils server;
    private ListShapeCtrl listShapeCtrl;

    @Inject
    public EditTaskController (ShowCtrl showCtrl, ServerUtils serverUtils){
        this.server = serverUtils;
        this.showCtrl=showCtrl;
    }

    public Scene setup(Task task, ListShapeCtrl listShapeCtrl){
        this.task = task;
        this.title.setText(task.getTitle());
        this.descriptionField.setText(task.getDescription());
        this.listShapeCtrl = listShapeCtrl;
        return title.getScene();
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void showAddTag() {
        showCtrl.showAddTag(task);
    }

    public void showAddSubTask(){
        showCtrl.showAddSubTask(task);
    }

    public void save() {
        String title = this.title.getText();
        String description = this.descriptionField.getText();
        task.setTitle(title);
        task.setDescription(description);

//        TaskEditModel model = new TaskEditModel(title, description);
//        IdResponseModel response = server.editTask(task.getId(), model);
//
//        if (response.getId() == -1) {
//            showCtrl.cancel();
//            showCtrl.showError(response.getErrorMessage());
//            return;
//        }

        listShapeCtrl.refreshList();
        showCtrl.cancel();
        showCtrl.showTaskOverview(task, listShapeCtrl);
    }
}
