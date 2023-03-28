package client.scenes.subtasks;

import client.scenes.ShowCtrl;
import commons.Task;
import javafx.fxml.FXML;

import javax.inject.Inject;
import javafx.scene.control.TextArea;

public class AddSubTaskController {
    @FXML
    private TextArea description;
    private ShowCtrl showCtrl;
    private Task task;

    @Inject
    public AddSubTaskController(ShowCtrl showCtrl){
        this.showCtrl = showCtrl;
    }

    public void cancel(){
        showCtrl.closePopUp();
    }

    public void setup(Task task){
        this.task = task;
    }

    public void addSubTask() {
        String name = this.description.getText();
//        if(task.getSubTasks() == null){
//            task.makeSubtaskList();
//        }
//        task.getSubTasks().add(name);
    }
}
