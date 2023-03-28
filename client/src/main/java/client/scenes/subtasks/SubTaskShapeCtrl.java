package client.scenes.subtasks;

import client.scenes.ShowCtrl;
import client.scenes.tasks.EditTaskController;
import client.utils.ServerUtils;
import commons.Subtask;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;

public class SubTaskShapeCtrl {
    @FXML
    private GridPane grid;
    @FXML
    private CheckBox textField;
    private ShowCtrl showCtrl;
    private ServerUtils serverUtils;
    private Subtask subtask;
    private EditTaskController editTaskController;

    @Inject
    public SubTaskShapeCtrl(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }

    public void setup(Subtask subtask, EditTaskController editTaskController){
        this.subtask = subtask;
        textField.setText(subtask.getDescription());
        this.editTaskController = editTaskController;
    }

    public Scene getScene(Subtask subtask){
        textField.setText(subtask.getDescription());
        textField.setSelected(subtask.isChecked());
        return grid.getScene();
    }

    public void changeSelected(){
        if(subtask.isChecked()){
            subtask.setChecked(false);
        }
        else{
            subtask.setChecked(true);
        }
    }

    public void remove(){
        //TODO
    }
}
