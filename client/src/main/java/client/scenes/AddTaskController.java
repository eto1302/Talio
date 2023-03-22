package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.inject.Inject;

public class AddTaskController {

    private final ShowCtrl showCtrl;
    private final ServerUtils server;
    private ListShapeCtrl controller;
    private Stage primaryStage;
    private Task task;

    @FXML
    private Button add, cancel, editTask, deleteTask;
    @FXML
    private TextField title;
    @FXML
    private TextArea descriptionField;


    @Inject
    public AddTaskController (ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        server=serverUtils;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void addTask() {
        String title = this.title.getText();
        String description = this.descriptionField.getText();
        task.setTitle(title);
        task.setDescription(description);
        showCtrl.addTask(task, controller, primaryStage);
        showCtrl.cancel();
    }

    public void showAddSubTask(){
        showCtrl.showAddSubTask(task);
    }

    public void showAddTag() {
        showCtrl.showAddTag(task);
    }

    public void setup(ListShapeCtrl controller, Stage primaryStage) {
        this.task = Task.create(null, null);
        this.controller=controller;
        this.primaryStage=primaryStage;
    }
}
