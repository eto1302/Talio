package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Color;
import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.TaskEditModel;
import commons.sync.TaskEdited;
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
    private UserData userData;
    @Inject
    private ShowCtrl showCtrl;
    @Inject
    private ServerUtils serverUtils;
    private Color color;
    private Task task;

    public SelectTaskColorController(){
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
        List list = this.serverUtils.getList(this.task.getListID());
        TaskEditModel edit = new TaskEditModel(task.getTitle(), task.getDescription(),
                task.getIndex(), list, task.getColorId());

        IdResponseModel response = userData.updateBoard
                (new TaskEdited(list.getBoardId(), list.getId(),
                        task.getId(), edit));

        if (response.getId() == -1) {
            showCtrl.cancel();
            showCtrl.showError(response.getErrorMessage());
            return;
        }
        showCtrl.cancel();
    }
}
