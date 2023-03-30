package client.scenes;


import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.List;
import commons.models.IdResponseModel;
import commons.sync.ListDeleted;
import commons.Task;
import commons.models.TaskEditModel;
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
    private final ShowCtrl showCtrl;
    private final ServerUtils serverUtils;
    private final UserData userData;
    private List list;
    private Stage primaryStage;


    @Inject
    public ListShapeCtrl(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
        this.userData = userData;
    }

    /**
     * Updates the list's visual (sets the title and the colors of it)
     * based on the list object that is passed on
     * @param list the list with the necessary attributes
     * @return the updated scene after modifications
     */
    public Scene getSceneUpdated(commons.List list){
        listTitle.setText(list.getName());
        Board board = this.userData.getCurrentBoard();
        Color backgroundColor= Color.web(board.getListColor().getBackgroundColor());
        Color fontColor= Color.web(board.getListColor().getFontColor());

        listGrid.setBackground(new Background(
                new BackgroundFill(backgroundColor, null, null)));
        listTitle.setTextFill(fontColor);
        return listGrid.getScene();
    }

    public void refreshList(){
        showCtrl.refreshBoardCtrl();
    }

    public Scene putTask(Scene scene){
        tasksBox.getChildren().add(scene.getRoot());
        return tasksBox.getScene();
    }

    /**
     * deletes the list from the board
     */
    public void deleteList() {
        HBox parent = (HBox) listGrid.getParent();
        parent.getChildren().remove(listGrid);
    }

    /**
     * sends a message for board deletion, invoked from FXML
     */
    public void initiateDeleteList() {
        IdResponseModel response = userData.updateBoard(new
                ListDeleted(list.getBoardId(), list.getId()));
        if (response.getId() == -1)
            showCtrl.showError(response.getErrorMessage());
    }

    /**
     * shows the window with options for editing the list
     */
    public void editList(){
        if(list == null) {
            showCtrl.showError("Failed to get the list...");
            return;
        }
        showCtrl.showEditList(list, primaryStage);
    }

    /**
     * sets information
     * @param list our list
     * @param primaryStage of the scene we are in
     */
    public void set(List list, Stage primaryStage){
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

    /**
     * Allows the task to be dragged over this list
     * @param event the drag event
     */
    public void dragOver(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasString()){
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        }
    }

    /**
     * Drops the dragged task into this list, changing the task's index and list references
     * to this list by sending a request to the server.
     * Only allows this if the task comes from a different list
     * @param event the drag event
     */
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
            previousList.getTasks().remove(task);

            tasksBox.getChildren().add(((GridPane) source));
            int newIndex= tasksBox.getChildren().indexOf((GridPane) source);

            TaskEditModel model = new TaskEditModel(task.getTitle(),
                    task.getDescription(), newIndex, list);
            serverUtils.editTask(taskId, model);

            list.getTasks().add(task);
            reorderTasks(previousListId);

            done = true;
        }
        ((GridPane) source).setOpacity(1);

        event.setDropCompleted(done);
        event.consume();
    }

    /**
     * Re-assigns the tasks' indexes after the drag and drop event update
     * @param previousListId the list in which the other task got dragged from
     */
    private void reorderTasks(int previousListId){
        java.util.List<Task> tasksToReorder = serverUtils.getTasksOrdered(previousListId);
        List previousList = serverUtils.getList(previousListId);

        for (int i=0; i<tasksToReorder.size(); i++){
            Task taskIndex = tasksToReorder.get(i);
            TaskEditModel model = new TaskEditModel(taskIndex.getTitle(),
                    taskIndex.getDescription(), i, previousList);
            serverUtils.editTask(taskIndex.getId(), model);
        }
    }

}
