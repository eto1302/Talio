package client.scenes;

import client.Services.ListService;
import client.Services.TaskService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Color;
import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;

public class SelectTaskColorController {
    @FXML
    private Label backgroundTaskColor;
    @FXML
    private Label fontTaskColor;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button selectButton;
    @Inject
    private ShowCtrl showCtrl;
    private Color color;
    private Task task;
    private TaskService taskService;
    private ListService listService;

    @Inject
    public SelectTaskColorController(UserData userData, ServerUtils serverUtils){
        this.taskService = new TaskService(userData, serverUtils);
        this.listService = new ListService(userData, serverUtils);
    }

    public void set(Color color, Task task){
        this.color = color;
        this.task = task;
    }

    public Scene getSceneUpdated(Color color) {
        this.backgroundTaskColor.setStyle(
                "-fx-background-color: " + color.getBackgroundColor() + ";");
        this.fontTaskColor.setStyle(
                "-fx-background-color: " + color.getFontColor() + ";");
        if(this.task.getColorId() == color.getId()){
            this.selectButton.setVisible(false);
        }
        else{
            this.selectButton.setVisible(true);
        }
        return this.gridPane.getScene();
    }

    public void select() {
        this.task.setColorId(color.getId());
        List list = this.listService.getList(this.task.getListID());
        IdResponseModel response = this.taskService.editTask(task, list, task.getIndex());

        if (response.getId() == -1) {
            showCtrl.cancel();
            showCtrl.showError(response.getErrorMessage());
            return;
        }
        showCtrl.cancel();
    }
}
