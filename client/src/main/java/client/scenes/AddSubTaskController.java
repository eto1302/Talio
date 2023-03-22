package client.scenes;

import commons.Task;
import javafx.fxml.FXML;

import javax.inject.Inject;
import java.awt.*;

public class AddSubTaskController {
    @FXML
    private TextField name;
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
        String name = this.name.getText();
//        if(task.getSubTasks() == null){
//            task.makeSubtaskList();
//        }
//        task.getSubTasks().add(name);
    }
}
