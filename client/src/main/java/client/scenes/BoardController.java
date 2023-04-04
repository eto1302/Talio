package client.scenes;

import client.Services.BoardService;
import client.Services.TaskService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import commons.models.IdResponseModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.Arrays;
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
    private GridPane boardBox;
    @FXML
    private ImageView lockIcon;
    @FXML
    private ImageView editIcon;
    @FXML
    private ImageView deleteIcon;
    @FXML
    private ScrollPane scrollPane;
    private final ShowCtrl showCtrl;
    private LinkedList<ListShapeCtrl> listControllers;
    private ListShapeCtrl selectedList=null;
    private TaskShape selectedTask=null;
    private boolean editable = false;
    private UserData userData;
    private BoardService boardService;
    private TaskService taskService;

    private static final Image LOCKED_IMG = new Image(
            "file:client/build/resources/main/icons/lock.png");
    private static final Image UNLOCKED_IMG = new Image(
            "file:client/build/resources/main/icons/unlock.png");

    @Inject
    public BoardController(ShowCtrl showCtrl, ServerUtils server, UserData userData) {
        this.showCtrl = showCtrl;
        this.userData = userData;
        this.boardService = new BoardService(userData, server);
        this.taskService = new TaskService(userData, server);
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
        Board board = this.boardService.getCurrentBoard();
        this.boardLabel.setText(board.getName());
        this.boardLabel.setTextFill(Color.web(
                board.getBoardColor().getFontColor()));
        this.boardBox.setStyle("-fx-background-color: " +
                board.getBoardColor().getBackgroundColor() + "; -fx-padding: 5px;");
        listBox.getChildren().clear();
        listBox.getChildren();
        listControllers.clear();

        List<commons.List> lists;

        try {
            boardService.refresh();
        } catch (Exception e) {
            showCtrl.showError(e.getMessage());
            return;
        }
        lists = boardService.getCurrentBoard().getLists();

        for (commons.List list : lists) {
            showCtrl.addList(list);

            List<Task> orderedTasks = taskService.getTasksOrdered(list.getId());
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

    public void showEditBoard() { showCtrl.showEditBoard();}

    public void delete() {
        IdResponseModel response = this.boardService.delete(
                this.boardService.getCurrentBoard().getId());

        if (response.getId() == -1) {
            showCtrl.showError(response.getErrorMessage());
        }
        else{
            showCtrl.showYourBoards();
        }
    }

    public void lockHoverOn() {
        ((Lighting) lockIcon.getEffect()).getLight().setColor(Color.valueOf("#FFFFFF"));
    }

    public void lockHoverOff() {
        ((Lighting) lockIcon.getEffect()).getLight().setColor(Color.valueOf("#000000"));
    }

    public void manageLock() {
        if(userData.isCurrentBoardLocked())
            showCtrl.showUnlockBoard();
        else showCtrl.showLockBoard();
    }

    public void updateLockIcon(boolean locked) {
        lockIcon.setImage(locked ? LOCKED_IMG : UNLOCKED_IMG);
        editIcon.setVisible(!locked);
        deleteIcon.setVisible(!locked);
    }

    public void movement(KeyEvent event){
        if (selectedTask==null)
            find();

        KeyCode key = event.getCode();
        if (selectedTask!=null && !event.isShiftDown() && !editable) {
            int index = selectedList.getTaskControllers().indexOf(selectedTask);
            switchCase(key, index);
        }

        else if (selectedTask!=null && !editable){
            int index = selectedList.getTaskControllers().indexOf(selectedTask);
            TaskShape copy = selectedTask;

            switch (key){
                case DOWN:
                case KP_DOWN:
                case S:
                    selectedTask.orderWithKeyEvent(index, "down");
                    if (index!=selectedList.getTaskControllers().size()-1) {
                        selectedTask = selectedList.getTaskControllers().get(index + 1);
                        selectedList.updateScrollPane(index+1);
                    } else selectedTask=copy;
                    selectedTask.setStatus(true);
                    break;

                case UP:
                case KP_UP:
                case W:
                    selectedTask.orderWithKeyEvent(index, "up");
                    if (index!=0) {
                        selectedTask = selectedList.getTaskControllers().get(index - 1);
                        selectedList.updateScrollPane(index);
                    }
                    else selectedTask=copy;
                    selectedTask.setStatus(true);
                    break;

            }
        }

        else if (editable)
            if (key==KeyCode.ENTER) {
                editable=false;
                selectedTask.editOnKey();
                grid.requestFocus();
            }
    }


    private void switchCase(KeyCode key, int index){
        switch (key){
            case DOWN:
            case KP_DOWN:
            case S:
                down(index);
                break;
            case UP:
            case KP_UP:
            case W:
                up(index);
                break;
            case LEFT:
            case KP_LEFT:
            case A:
                left();
                break;
            case RIGHT:
            case KP_RIGHT:
            case D:
                right();
                break;
            case DELETE:
            case BACK_SPACE:
                selectedTask.deleteOnKey();
                break;
            case ENTER:
                showCtrl.showEditTask(selectedTask.getTask(), selectedList);
                break;
            case E:
                editable=true;
                selectedTask.makeEditable();
                break;
            case C:
                //show color preset
                break;
            case T:
                //show tag
                break;
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
                List<KeyCode> arrows = Arrays.asList(KeyCode.DOWN, KeyCode.KP_DOWN, KeyCode.UP,
                        KeyCode.KP_UP, KeyCode.LEFT, KeyCode.KP_LEFT,
                        KeyCode.RIGHT, KeyCode.KP_RIGHT);
                if (arrows.contains(event.getCode()) && !event.isShiftDown())
                    movement(event);
            }
        });
    }

}
