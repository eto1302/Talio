package client.scenes;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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

    private final ShowCtrl showCtrl;
    private List<AnchorPane> cards;
    private List<AnchorPane> boards;
    private double startX;
    private double startY;
    private Bounds todoListBounds, doingListBounds, doneListBounds;
    private List<Bounds> bounds;

    @Inject
    public BoardController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
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

    private void setup() {
        root = (AnchorPane) selectedCard.getParent().getParent();
        List<Node> children = root.getChildren();
        for(int i = 0; i < children.size(); ++i){
            Node child = children.get(i);
            if(child.getId().equals("todoList")){
                this.todoList = (AnchorPane) child;
                this.todoListBounds = todoList.localToScene(todoList.getLayoutBounds());
            }
            if(child.getId().equals("doingList")){
                this.doingList = (AnchorPane) child;
                this.doingListBounds = doingList.localToScene(doingList.getLayoutBounds());
            }
            if(child.getId().equals("doneList")){
                this.doneList = (AnchorPane) child;
                this.doneListBounds = doneList.localToScene(doneList.getLayoutBounds());
            }
        }
        boards.add(todoList);
        boards.add(doingList);
        boards.add(doneList);
        bounds.add(todoListBounds);
        bounds.add(doingListBounds);
        bounds.add(doneListBounds);
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
}
