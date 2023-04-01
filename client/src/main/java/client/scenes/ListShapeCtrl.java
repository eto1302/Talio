package client.scenes;


import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.TaskEditModel;
import commons.sync.ListDeleted;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.LinkedList;

public class ListShapeCtrl {

    @FXML
    private VBox tasksBox;
    @FXML
    private MenuItem editList, deleteList;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label listTitle;
    @FXML
    private GridPane listGrid;
    private final ShowCtrl showCtrl;
    private final ServerUtils serverUtils;
    private final UserData userData;
    private List list;

    private LinkedList<TaskShape> taskControllers;
    private BoardController boardController;

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

    public void updateScrollPane(int index){
        Bounds bounds = scrollPane.getViewportBounds();
        scrollPane.setVvalue(tasksBox.getChildren().get(index).getLayoutY() *
                (1/(tasksBox.getHeight()-bounds.getHeight())));
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
        showCtrl.showEditList(list);
    }

    /**
     * sets list and updates the list's visual (sets the title
     * and the colors of it) based on the list object that is passed on
     * @param list our list
     */
    public void updateScene(List list, BoardController boardController) {
        this.list = list;
        this.boardController = boardController;
        this.taskControllers = new LinkedList<>();
        Board board = serverUtils.getBoard(list.getBoardId());
        taskControllers = new LinkedList<>();

        listGrid.setOnDragOver(this::dragOver);
        listGrid.setOnDragDropped(this::dragDrop);

        listTitle.setText(list.getName());
        Color backgroundColor= Color.web(board.getListColor().getBackgroundColor());
        Color fontColor= Color.web(board.getListColor().getFontColor());

        listGrid.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        listTitle.setTextFill(fontColor);
    }
    public List getList(){
        return list;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    /**
     * shows the add task window
     */
    public void showAddTask(){
        showCtrl.showAddTask(this, list);
    }

    /**
     * Adds the task inside the box with tasks
     * @param root the grid representing the task UI
     */
    public void addTask(Parent root, TaskShape controller) {
        tasksBox.getChildren().add(root);
        taskControllers.addLast(controller);
    }

    /**
     * Removes the task from the list
     * @param taskId the ID of the task to remove
     */
    public void removeTask(int taskId) {
        TaskShape controller = taskControllers.stream().filter(e ->
                e.getTask().getId() == taskId).findFirst().orElse(null);
        if(controller != null) {
            controller.delete();
            taskControllers.remove(controller);
        }
    }

    public LinkedList<TaskShape> getTaskControllers() {
        return taskControllers;
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
                    task.getDescription(), newIndex, list, task.getColorId());
            serverUtils.editTask(taskId, model);

            list.getTasks().add(task);
            reorderTasks(previousListId);

            done = true;
            boardController.refresh();
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
                    taskIndex.getDescription(), i, previousList, taskIndex.getColorId());
            serverUtils.editTask(taskIndex.getId(), model);
        }
    }


    public TaskShape findSelectedTask(){
        for (TaskShape controller: taskControllers)
            if (controller.isSelected())
                return controller;
        return null;
    }

}
