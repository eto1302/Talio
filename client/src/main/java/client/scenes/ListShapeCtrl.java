package client.scenes;


import client.utils.ServerUtils;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.inject.Inject;

public class ListShapeCtrl {

    @FXML
    private VBox tasksBox;
    @FXML
    private MenuItem editList, deleteList;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button addTask;
    @FXML
    private Label listTitle;
    @FXML
    private GridPane listGrid;
    private ShowCtrl showCtrl;
    private ServerUtils serverUtils;
    private List list;
    private Stage primaryStage;


    @Inject
    public ListShapeCtrl(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        this.serverUtils=serverUtils;
    }

    /**
     * Updates the list's visual (sets the title and the colors of it)
     * based on the list object that is passed on
     * @param list the list with the necessary attributes
     * @return the updated scene after modifications
     */
    public Scene getSceneUpdated(commons.List list){
        listTitle.setText(list.getName());
        Color backgroundColor= Color.web(list.getBackgroundColor());
        Color fontColor= Color.web(list.getFontColor());

        listGrid.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        listTitle.setTextFill(fontColor);
        return listGrid.getScene();
    }

    /**
     *deletes the list from the board
     */
    public void deleteList(){
        serverUtils.deleteList(list.getBoardId(), list.getId());
    }

    /**
     * shows the window with options for editing the list
     */
    public void editList(){
        if(list == null) showCtrl.showError("Failed to get the list...");
        showCtrl.showEditList(list, this);
    }

    /**
     * sets information
     * @param list our list
     * @param primaryStage of the scene we are in
     */
    public void set(List list, Stage primaryStage){
        this.list=list;
        this.primaryStage=primaryStage;
    }
    public List getList(){
        return list;
    }

    /**
     * shows the add task window
     */
    public void showAddTask(){
        showCtrl.showAddTask(this, primaryStage);
    }

    /**
     * Adds the task inside the box with tasks
     * @param taskScene the scene containing the grid representing
     * @return the updated scene
     */
    public Scene addTask(Scene taskScene){
        tasksBox.getChildren().add(taskScene.getRoot());
        return tasksBox.getScene();
    }
}
