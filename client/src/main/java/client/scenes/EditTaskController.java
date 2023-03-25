package client.scenes;

import client.utils.ServerUtils;
import commons.Subtask;
import commons.Tag;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.TaskEditModel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private Stage primaryStage;

    @Inject
    public EditTaskController (ShowCtrl showCtrl, ServerUtils serverUtils){
        this.server = serverUtils;
        this.showCtrl=showCtrl;
    }

    public Scene setup(Task task, ListShapeCtrl listShapeCtrl, Stage primaryStage){
        this.task = task;
        this.title.setText(task.getTitle());
        this.descriptionField.setText(task.getDescription());
        this.listShapeCtrl = listShapeCtrl;
        this.primaryStage = primaryStage;

        java.util.List<Subtask> subtasks = task.getSubtasks();
        for(Subtask subtask: subtasks){
            showCtrl.addSubTask(subtask, this);
        }
        java.util.List<Tag> tags = task.getTags();
        for(Tag tag: tags){
            showCtrl.addTag(tag, this);
        }

        return title.getScene();
    }

    public Scene putSubtask(Scene scene){
        subtaskBox.getChildren().add(scene.getRoot());
        return subtaskBox.getScene();
    }

    public Scene putTag(Scene scene){
        tagBox.getChildren().add(scene.getRoot());
        return tagBox.getScene();
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

        TaskEditModel model = new TaskEditModel(title, description);
        IdResponseModel response = server.editTask(task.getId(), model);

        if (response.getId() == -1) {
            showCtrl.cancel();
            showCtrl.showError(response.getErrorMessage());
            return;
        }

        listShapeCtrl.refreshList();
    }
}
