package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BoardController {
    @FXML
    private AnchorPane todoList;
    @FXML
    private  AnchorPane doingList;
    @FXML
    private  AnchorPane doneList;
    private  AnchorPane root;
    private  AnchorPane selectedCard;
    @FXML
    private  AnchorPane card1, card2, card3, card4, card5;
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
    private MenuItem addTaskToDo, addTaskDoing, addTaskDone, addTagToDo,
        addTagDoing, addTagDone, deleteToDo, deleteDoing, deleteDone, editToDo, editDoing, editDone;

    private final ShowCtrl showCtrl;
    private ServerUtils server;
    private List<AnchorPane> cards;
    private List<AnchorPane> boards;
    private double startX;
    private double startY;
    private Bounds todoListBounds, doingListBounds, doneListBounds;
    private List<Bounds> bounds;

    @Inject
    public BoardController(ShowCtrl showCtrl, ServerUtils server) {
        this.showCtrl = showCtrl;
        this.server = server;
        boards = new ArrayList<>();
        bounds = new ArrayList<>();
    }

//    public AnchorPane createCard(commons.messaging.Messages.List list){
//        AnchorPane result = new AnchorPane();
//        result.setId(list.getId().toString());
//        result.setPrefSize(40.0, 180.0);
//        result.setOnDragDetected(this::onDrag);
//        result.setOnMousePressed(this::mousePressed);
//        result.setOnMouseDragged(this::mouseDragged);
//        return result;
//    }

    public void onDrag(MouseEvent event){
        selectedCard.getParent().toFront();
        selectedCard.toFront();
    }

    public void mousePressed(MouseEvent event){
        selectedCard = (AnchorPane) event.getTarget();
        startX = event.getX();
        startY = event.getY();
        setup();
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

        try {
            lists = server.getListByBoard(1);
        } catch (Exception e) {
            showCtrl.showError(e.getMessage());
            return;
        }

        for (commons.List list : lists) {
            showCtrl.addList(list);
        }

    }

    public void mouseDragged(MouseEvent event) {
        selectedCard.setLayoutX(selectedCard.getLayoutX() + event.getX() - startX);
        selectedCard.setLayoutY(selectedCard.getLayoutY() + event.getY() - startY);
    }

    public void mouseReleased() {
        Bounds cardBounds = selectedCard.localToScene(selectedCard.getLayoutBounds());
        for(int i = 0; i < 3; ++i){
            AnchorPane board = boards.get(i);
            Bounds boardBounds = bounds.get(i);
            if(!board.equals(selectedCard.getParent()) && cardBounds.intersects(boardBounds)){
                addToBoard(board);
                return;
            }
        }
        resetParent();
    }

    private void addToBoard(AnchorPane board) {
        AnchorPane original = (AnchorPane) selectedCard.getParent();
        original.getChildren().remove(selectedCard);
        for(int i = 3; i < original.getChildren().size(); ++i){
            original.getChildren().get(i).setLayoutY((i - 2) * 40);
        }
        board.getChildren().add(selectedCard);
        selectedCard.setLayoutX(0.0);
        selectedCard.setLayoutY((board.getChildren().size() - 3) * 40);
    }

    public void resetParent(){
        AnchorPane parent = (AnchorPane) selectedCard.getParent();
        List<Node> children = parent.getChildren();
        for(int i = 3; i < children.size(); ++i){
            AnchorPane child = (AnchorPane) children.get(i);
            child.setLayoutY((i - 2) * 40);
        }
        selectedCard.setLayoutX(0.0);
        selectedCard.setLayoutY((children.size() - 3) * 40);
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
