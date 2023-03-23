package client.scenes;

import client.utils.ServerUtils;
import commons.List;
import commons.Subtask;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.ArrayList;

public class AddTaskController {

    private final ShowCtrl showCtrl;
    private final ServerUtils server;
    private ListShapeCtrl controller;
    private Stage primaryStage;
    private List list;

    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField textField;

    @Inject
    public AddTaskController (ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        server=serverUtils;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void add(){
        String title = textField.getText();
        Task task = Task.create(null, title, list.getId(), new ArrayList<Subtask>());
        showCtrl.addTask(task, controller, primaryStage);
        showCtrl.cancel();
    }

    public void setup(ListShapeCtrl controller, Stage primaryStage, commons.List list) {
        this.controller=controller;
        this.primaryStage=primaryStage;
        this.list=list;
    }
}
