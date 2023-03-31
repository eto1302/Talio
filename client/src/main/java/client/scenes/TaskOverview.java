package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class TaskOverview {
    @FXML
    private Label title, descriptionField;
    @FXML
    private VBox subtaskBox, tagBox;
    private final ShowCtrl showCtrl;
    private ServerUtils serverUtils;
    private int id;
    private commons.Task task;
    private ListShapeCtrl listShapeCtrl;

    @Inject
    public TaskOverview(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        this.serverUtils=serverUtils;
    }

    public Scene setup(Task task, ListShapeCtrl listShapeCtrl){
        this.task = task;
        title.setText(task.getTitle());
        descriptionField.setText((task.getDescription()));
        this.listShapeCtrl = listShapeCtrl;
        return title.getScene();
    }

    public void showEditTask(){
        showCtrl.cancel();
        showCtrl.showEditTask(task, listShapeCtrl);
    }

    public void cancel(){
        showCtrl.cancel();
    }
}
