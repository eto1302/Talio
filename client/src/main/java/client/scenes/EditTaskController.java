package client.scenes;

import client.scenes.ShowCtrl;
import client.scenes.ListShapeCtrl;
import client.utils.ServerUtils;
import commons.List;
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
        return refresh();
    }

    public Scene refresh(){
        subtaskBox.getChildren().clear();
        java.util.List<Subtask> subtasks = server.getSubtasksOrdered(task.getId());
        if(subtasks != null) {
            for(Subtask subtask: subtasks){
                showCtrl.addSubTask(subtask, this);
            }
        }
//        java.util.List<Tag> tags = task.getTags();
        cleanTagBox();
        java.util.List<Tag> tags = server.getTagByTask(task.getId());
        if(tags != null) {
            for(Tag tag: tags){
                showCtrl.addTagToTask(tag, task);
            }
        }
        return title.getScene();
    }

    public void putSubtask(Scene scene, Subtask subtask){
        subtaskBox.getChildren().add(scene.getRoot());
        task.getSubtasks().add(subtask);
    }

    public Scene putTag(Scene scene){
        tagBox.getChildren().add(scene.getRoot());
        return tagBox.getScene();
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void showAddTagToTask() {
        showCtrl.showAddTagToTask(this);
    }

    public void showAddSubTask(){
        showCtrl.showAddSubTask(this, task);
    }

    public void save() {
        String title = this.title.getText();
        String description = this.descriptionField.getText();
        task.setTitle(title);
        task.setDescription(description);
        List list =server.getList(task.getListID());

        TaskEditModel model = new TaskEditModel(title, description, task.getIndex(),
                list, task.getColorId());
        IdResponseModel response = server.editTask(task.getId(), model);

        if (response.getId() == -1) {
            showCtrl.cancel();
            showCtrl.showError(response.getErrorMessage());
            return;
        }

        listShapeCtrl.refreshList();
        showCtrl.cancel();
    }

    public void showTaskColorPicker() {
        showCtrl.cancel();
        showCtrl.showTaskColorPicker(task);
    }

    public ListShapeCtrl getListShapeCtrl() {
        return listShapeCtrl;
    }

    public void setListShapeCtrl(ListShapeCtrl listShapeCtrl) {
        this.listShapeCtrl = listShapeCtrl;
    }

    public Task getTask() {
        return task;
    }

    private void cleanTagBox() {
        tagBox.getChildren().remove(0, tagBox.getChildren().size());
    }
}
