package client.scenes;

import client.Services.BoardService;
import client.Services.TaskService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AddTaskController {
    private final ShowCtrl showCtrl;
    @FXML
    private TextField title;
    @FXML
    private TextArea descriptionField;
    private List list;
    private TaskService taskService;
    private BoardService boardService;

    @Inject
    public AddTaskController (ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData){
        this.showCtrl=showCtrl;
        this.taskService = new TaskService(userData, serverUtils);
        this.boardService = new BoardService(userData, serverUtils);
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void addTask() {
        IdResponseModel response = taskService.addTask(
                title.getText(), descriptionField.getText(), list);
        if(response.getId() == -1){
            showCtrl.showError(response.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        showCtrl.showBoard();
        showCtrl.cancel();
    }


    public void setup(commons.List list) {
        this.list=list;
    }
}
