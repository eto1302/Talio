package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;
import commons.Subtask;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.TaskEditModel;
import commons.sync.TaskEdited;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private UserData userData;

    @Inject
    public EditTaskController (ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.server = serverUtils;
        this.showCtrl = showCtrl;
        this.userData = userData;
    }

    public Scene setup(Task task, ListShapeCtrl listShapeCtrl) {
        this.task = task;
        this.title.setText(task.getTitle());
        this.descriptionField.setText(task.getDescription());
        this.listShapeCtrl = listShapeCtrl;

        return refresh();
    }

    public Scene refresh(){
        subtaskBox.getChildren().clear();
        java.util.List<Subtask> subtasks = server.getSubtasksOrdered(task.getId());
        for (Subtask subtask: subtasks)
            showCtrl.addSubTask(subtask, this);
//        tagBox.getChildren().clear();
//        java.util.List<Tag> tags = server.getTagsByTask(task.getId());
//        for (Tag tag: tags)
//            showCtrl.addTag(tag, this);
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
        showCtrl.showAddSubTask(this, task);
    }

    public void save() {
        String title = this.title.getText();
        String description = this.descriptionField.getText();
        task.setTitle(title);
        task.setDescription(description);
        List list = server.getList(task.getListID());

        TaskEditModel model = new TaskEditModel(title, description, task.getIndex(), list);
        IdResponseModel response = userData.updateBoard
                (new TaskEdited(list.getBoardId(), list.getId(), task.getId(), model));

        if (response.getId() == -1) {
            showCtrl.cancel();
            showCtrl.showError(response.getErrorMessage());
            return;
        }

        showCtrl.cancel();
    }
}
