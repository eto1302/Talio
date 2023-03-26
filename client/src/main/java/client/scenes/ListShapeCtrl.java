package client.scenes;


import client.utils.ServerUtils;
import commons.List;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
    private BoardController boardController;


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

    public void refreshList(){
        boardController.refresh();
    }

    public Scene putTask(Scene scene){
        tasksBox.getChildren().add(scene.getRoot());
        return tasksBox.getScene();
    }

    /**
     *deletes the list from the board
     */
    public void deleteList(){
        serverUtils.deleteList(list.getBoardId(), list.getId());
        HBox parent = (HBox) listGrid.getParent();
        parent.getChildren().remove(listGrid);
    }

    /**
     * shows the window with options for editing the list
     */
    public void editList(){
        if(list == null) {
            showCtrl.showError("Failed to get the list...");
            return;
        }
        showCtrl.showEditList(list, this, primaryStage);
    }

    /**
     * sets information
     * @param list our list
     * @param primaryStage of the scene we are in
     */
    public void set(List list, Stage primaryStage, BoardController boardController){
        this.list=list;
        this.primaryStage=primaryStage;

        listGrid.setOnDragOver(this::dragOver);
        listGrid.setOnDragDropped(this::dragDrop);
    }
    public List getList(){
        return list;
    }

    /**
     * shows the add task window
     */
    public void showAddTask(){
        showCtrl.showAddTask(this, primaryStage, list);
    }

    /**
     * Adds the task inside the box with tasks
     * @param taskScene the scene containing the grid representing
     * @return the updated scene
     */
    public Scene addTask(Scene taskScene, Task task){
        Node root = taskScene.getRoot();
        tasksBox.getChildren().add(root);
        task.setIndex(tasksBox.getChildren().indexOf(root));
        return tasksBox.getScene();
    }

    public void dragOver(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasString()){
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        }
    }

    public void dragDrop(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        boolean done = false;
        Object source = event.getGestureSource();

        String identify = dragboard.getString();
        int taskId = Integer.parseInt(identify.split("\\+")[0].trim());
        int previousListId = Integer.parseInt(identify.split("\\+")[1].trim());

        if (previousListId!=list.getId()) {
            Task task =serverUtils.getTask(taskId);
            List previousList = serverUtils.getList(previousListId);

            list.getTasks().add(task);

            tasksBox.getChildren().add(((GridPane) source));

            done = true;
        }
        ((GridPane) source).setOpacity(1);

        event.setDropCompleted(done);
        event.consume();
    }

    public void setBoard(BoardController boardController) {
        this.boardController = boardController;
    }
}
