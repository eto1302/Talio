package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.Set;

public class BoardController {
    @FXML
    private MenuButton menu;
    @FXML
    private MenuItem joinBoard;
    @FXML
    private MenuItem addBoard;
    @FXML
    private MenuItem yourBoards;
    @FXML
    private MenuItem personalize;
    @FXML
    private Label boardLabel;
    @FXML
    private VBox toDoBox, doingBox, doneBox;
    @FXML
    private HBox listBox;
    @FXML
    private GridPane toDo, doing, done;
    @FXML
    private MenuItem addTaskToDo, addTaskDoing, addTaskDone,
         deleteToDo, deleteDoing, deleteDone, editToDo, editDoing, editDone;

    private final ShowCtrl showCtrl;
    private ServerUtils server;

    @Inject
    private UserData userData;

    @Inject
    public BoardController(ShowCtrl showCtrl, ServerUtils server) {
        this.showCtrl = showCtrl;
        this.server = server;
    }


    // I commented this code snippet as these three lists are not really connected to the server.
    // If we want default lists in the board we can create the board with three default lists.
    // When calling this function, the client will send a request to the server to fetch all
    // lists in the corresponding board and display them.
    public void setup() {
//        root = (AnchorPane) selectedCard.getParent().getParent();
//        List<Node> children = root.getChildren();
//        for(int i = 0; i < children.size(); ++i){
//            Node child = children.get(i);
//            if(child.getId().equals("todoList")){
//                this.todoList = (AnchorPane) child;
//                this.todoListBounds = todoList.localToScene(todoList.getLayoutBounds());
//            }
//            if(child.getId().equals("doingList")){
//                this.doingList = (AnchorPane) child;
//                this.doingListBounds = doingList.localToScene(doingList.getLayoutBounds());
//            }
//            if(child.getId().equals("doneList")){
//                this.doneList = (AnchorPane) child;
//                this.doneListBounds = doneList.localToScene(doneList.getLayoutBounds());
//            }
//        }
//        boards.add(todoList);
//        boards.add(doingList);
//        boards.add(doneList);
//        bounds.add(todoListBounds);
//        bounds.add(doingListBounds);
//        bounds.add(doneListBounds);
        refresh();
    }


    /**
     *  Send a request to the server and fetch all lists stored in the default board with id 1 (for
     *  single board mode).
     *  TODO: For now, I added a button to call this function, but we can use webSocket to refresh
     *  TODO: the board and get rid of the button in the future.
     */
    public void refresh() {
        listBox.getChildren().clear();
        listBox.getChildren();
        Set<commons.List> lists;
        java.util.List<commons.Task> tasks;

        try {
            userData.refresh();
        } catch (Exception e) {
            showCtrl.showError(e.getMessage());
            return;
        }
        lists = userData.getCurrentBoard().getLists();

        for (commons.List list : lists) {
            ListShapeCtrl listShapeCtrl = showCtrl.addAndReturnList(list);
            listShapeCtrl.setBoard(this);
            tasks = list.getTasks();
            for(commons.Task task: tasks){
                showCtrl.addTask(task, listShapeCtrl);
            }
        }

    }

    public void showYourBoards(){
        showCtrl.showYourBoards();
    }

    public void showSearch() {showCtrl.showSearch();}

    public void showDetails() {};

    public void showEditList() {};

    public void showConfirmDelete() {};

    public void addBoard(){
        showCtrl.showAddBoard();
    }

    public void showAddList(){
        showCtrl.showAddList();
    }

    public void showAdmin(){
        showCtrl.showAdmin();
    }

    /**
     * Puts the root of the scene (the grid representing the list) inside the board
     * @param scene ,whose root we are looking to add to our board
     * @return the updated scene of the board
     */
    public Scene putList(Scene scene){
        listBox.getChildren().add(scene.getRoot());
        return boardLabel.getScene();
    }

    public void setServer() {
        showCtrl.showConnection();
    }

    public void deleteToDo(){
        listBox.getChildren().remove(toDo);
    }
    public void deleteDoing(){
        listBox.getChildren().remove(doing);
    }

    public void deleteDone(){
        listBox.getChildren().remove(done);
    }

}
