package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import commons.models.IdResponseModel;
import commons.sync.BoardDeleted;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public class BoardController {
    @FXML
    private GridPane grid;
    @FXML
    private Label boardLabel;
    @FXML
    private HBox listBox;
    @FXML
    private ScrollPane scrollPane;

    private final ShowCtrl showCtrl;
    private ServerUtils server;
    private LinkedList<ListShapeCtrl> listControllers;
    private ListShapeCtrl selectedList=null;
    private TaskShape selectedTask=null;

    @Inject
    private UserData userData;

    @Inject
    public BoardController(ShowCtrl showCtrl, ServerUtils server) {
        this.showCtrl = showCtrl;
        this.server = server;
    }


    public void setup() {
        listControllers=new LinkedList<>();
        grid.requestFocus();
        filtering();
        grid.getScene().setOnKeyPressed(this::movement);
        refresh();
    }

    /**
     *  Send a request to the server and fetch all lists stored in the default board with id 1 (for
     *  single board mode).
     *  TODO: For now, I added a button to call this function, but we can use webSocket to refresh
     *  TODO: the board and get rid of the button in the future.
     */
    public void refresh() {
        this.boardLabel.setText(this.userData.getCurrentBoard().getName());
        this.boardLabel.setTextFill(Color.web(this.userData.getCurrentBoard().getFontColor()));
        listBox.getChildren().clear();
        listBox.getChildren();
        listControllers.clear();

        List<commons.List> lists;
        java.util.List<commons.Task> tasks;

        try {
            userData.refresh();
        } catch (Exception e) {
            showCtrl.showError(e.getMessage());
            return;
        }
        lists = userData.getCurrentBoard().getLists();

        for (commons.List list : lists) {
            showCtrl.addList(list);
            
            List<Task> orderedTasks = server.getTasksOrdered(list.getId());
            for(Task task: orderedTasks){
                showCtrl.addTask(task, list);
            }
        }

    }

    public void showYourBoards(){
        showCtrl.showYourBoards();
    }

    public void showSearch() {showCtrl.showSearch();}

    public void addBoard(){
        showCtrl.showAddBoard();
    }

    public void showAddList(){
        showCtrl.showAddList();
    }

    public void showAdmin(){
        showCtrl.showAdmin();
    }

    public LinkedList<ListShapeCtrl> getListControllers() {
        return listControllers;
    }

    /**
     * Puts the root of the scene (the grid representing the list) inside the board
     * @param root the root we are looking to add to our board
     */
    public void putList(Parent root, ListShapeCtrl ctrl) {
        listBox.getChildren().add(root);
        listControllers.addLast(ctrl);
    }

    /**
     * Deletes a list shape controller from the controller list
     * @param ctrl the controller to delete
     */
    public void deleteList(ListShapeCtrl ctrl) {
        listControllers.remove(ctrl);
    }

    public void setServer() {
        showCtrl.showConnection();
    }


    public void showEditBoard() { showCtrl.showEditBoard(this);}

    public void delete() {
        Board board = this.userData.getCurrentBoard();
        this.userData.leaveBoard(board.getId());
        this.userData.saveToDisk();
        BoardDeleted boardDeleted = new BoardDeleted(board.getId());

        IdResponseModel model = userData.deleteBoard(boardDeleted);

        if (model.getId() == -1) {
            showCtrl.showError(model.getErrorMessage());
        }
        else{
            showCtrl.showYourBoards();
        }
    }

    public void movement(KeyEvent event){
        if (selectedTask==null)
            find();

        KeyCode key = event.getCode();
        if (selectedTask!=null) {
            int index = selectedList.getTaskControllers().indexOf(selectedTask);
            switch (key){
                case DOWN, KP_DOWN, S -> down(index);
                case UP, KP_UP, W -> up(index);
                case LEFT, KP_LEFT, A -> left();
                case RIGHT, KP_RIGHT, D-> right();
                case DELETE, BACK_SPACE -> selectedTask.deleteOnKey();
                case ENTER -> showCtrl.showEditTask(selectedTask.getTask(), selectedList);
            }
        }
    }

    private void down(int index){
        selectedTask.setStatus(false);

        if (index + 1 == selectedList.getTaskControllers().size()) {
            selectedTask=selectedList.getTaskControllers().get(0);
            selectedList.updateScrollPane(0);
        } else {
            selectedTask=selectedList.getTaskControllers().get(index+1);
            selectedList.updateScrollPane(index+1);
        }
        selectedTask.setStatus(true);
    }

    private void up(int index){
        selectedTask.setStatus(false);

        if (index==0){
            int size =selectedList.getTaskControllers().size();
            selectedTask=selectedList.getTaskControllers().get(size-1);
            selectedList.updateScrollPane(size-1);
        }
        else{
            selectedTask = selectedList.getTaskControllers().get(index-1);
            selectedList.updateScrollPane(index-1);
        }
        selectedTask.setStatus(true);
    }

    private void left(){
        int index = listControllers.indexOf(selectedList);

        if (index==0){
            int size = listControllers.size();
            selectedList = listControllers.get(size - 1);
            updateScrollPane(size-1);
        }
        else{
            selectedList = listControllers.get(index-1);
            updateScrollPane(index-1);
        }
        selectedList.updateScrollPane(0);

        selectedTask.setStatus(false);
        selectedTask = selectedList.getTaskControllers().get(0);
        selectedTask.setStatus(true);
    }

    private void right(){
        int index = listControllers.indexOf(selectedList);

        if (index+1==listControllers.size()){
            selectedList = listControllers.get(0);
            updateScrollPane(0);
        }
        else{
            selectedList = listControllers.get(index+1);
            updateScrollPane(index);
        }
        selectedList.updateScrollPane(0);

        selectedTask.setStatus(false);
        selectedTask = selectedList.getTaskControllers().get(0);
        selectedTask.setStatus(true);
    }

    public TaskShape find(){
        for (ListShapeCtrl ctrl :listControllers) {
            TaskShape selected = ctrl.findSelectedTask();
            if (selected != null) {
                selectedList = ctrl;
                selectedTask = selected;
                return selected;
            }
        }
        return null;
    }

    public void reset(){
        selectedList=null;
        selectedTask=null;
    }

    private void updateScrollPane(int index){
        Bounds bounds = scrollPane.getViewportBounds();
        scrollPane.setHvalue(listBox.getChildren().get(index).getLayoutX() *
                (1/(listBox.getWidth()-bounds.getWidth())));
    }

    private void filtering(){
        scrollPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                movement(event);
            }
        });
    }

}
