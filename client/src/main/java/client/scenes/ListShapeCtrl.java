package client.scenes;


import client.Services.BoardService;
import client.Services.ListService;
import client.Services.TaskService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;

public class ListShapeCtrl {

    @FXML
    private VBox tasksBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label listTitle;
    @FXML
    private GridPane listGrid;
    @FXML
    private HBox hbox;
    @FXML
    private ImageView deleteList;
    @FXML
    private Label addTask;
    private final ShowCtrl showCtrl;
    private List list;
    private LinkedList<TaskShape> taskControllers;
    private BoardController boardController;
    private ListService listService;
    private BoardService boardService;
    private TaskService taskService;
    private TextField text;

    @Inject
    public ListShapeCtrl(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.showCtrl = showCtrl;
        this.listService = new ListService(userData, serverUtils);
        this.boardService = new BoardService(userData, serverUtils);
        this.taskService = new TaskService(userData, serverUtils);
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
        IdResponseModel response = this.listService.deleteList(list);
        if (response.getId() < 0)
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
        list.setTasks(new ArrayList<>());
        this.boardController = boardController;
        this.taskControllers = new LinkedList<>();
        text = new TextField();
        initializeText();

        Board board = boardService.getBoard(list.getBoardId());
        taskControllers = new LinkedList<>();

        listGrid.setOnDragOver(this::dragOver);
        listGrid.setOnDragDropped(this::dragDrop);

        listTitle.setText(list.getName());
        Color backgroundColor= Color.web(board.getListColor().getBackgroundColor());
        Color fontColor= Color.web(board.getListColor().getFontColor());

        listGrid.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        listTitle.setTextFill(fontColor);
    }

    private void initializeText() {
        text.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode()==KeyCode.ENTER){
                    if(!text.getText().equals("")) {
                        IdResponseModel response = taskService.addTask(
                                text.getText(), list);
                        if (response.getId() == -1) {
                            showCtrl.showError(response.getErrorMessage());
                        }
                    }
                    hbox.getChildren().remove(text);
                    hbox.getChildren().add(deleteList);
                    hbox.getChildren().add(addTask);
                }
            }
        });
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
        text.setPrefWidth(200);
        hbox.getChildren().remove(deleteList);
        hbox.getChildren().remove(addTask);
        hbox.getChildren().add(text);
        text.requestFocus();
    }

    /**
     * Adds the task inside the box with tasks
     * @param root the grid representing the task UI
     */
    public void addTask(Parent root, TaskShape controller, Task task) {
        tasksBox.getChildren().add(root);
        taskControllers.addLast(controller);
        list.getTasks().add(task);
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
            Task task = taskService.getTask(taskId);
            List previousList = listService.getList(previousListId);
            previousList.getTasks().remove(task);

            tasksBox.getChildren().add(((GridPane) source));
            int newIndex= tasksBox.getChildren().indexOf((GridPane) source);

            taskService.editTask(task, list, newIndex);

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
        java.util.List<Task> tasksToReorder = taskService.getTasksOrdered(previousListId);
        List previousList = listService.getList(previousListId);

        for (int i=0; i<tasksToReorder.size(); i++){
            Task taskIndex = tasksToReorder.get(i);
            taskService.editTask(taskIndex, previousList, i);
        }
    }


    public TaskShape findSelectedTask(){
        for (TaskShape controller: taskControllers)
            if (controller.isSelected())
                return controller;
        return null;
    }

    public TaskShape findTask(Task task){
        for(TaskShape c: taskControllers){
            if(task.equals(c.getTask())){
                return c;
            }
        }
        return null;
    }

}
