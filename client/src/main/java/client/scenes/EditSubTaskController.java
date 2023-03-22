package client.scenes;

import commons.Task;
import javafx.fxml.FXML;

import javax.inject.Inject;
import java.awt.*;

public class EditSubTaskController {
    @FXML
    private TextField name;
    private ShowCtrl showCtrl;
    private Task task;
    private int index;

    @Inject
    public EditSubTaskController(ShowCtrl showCtrl){
        this.showCtrl = showCtrl;
    }

    public void cancel(){
        showCtrl.closePopUp();
    }

    public void setup(Task task, int index){
        this.task = task;
        this.index = index;
    }

    public void editSubTask() {
        String name = this.name.getText();
//        if(task.getSubTasks() == null){
//            task.makeSubtaskList();
//        }
//        task.getSubTasks().add(index, name);
    }
}

