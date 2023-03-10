package client.scenes;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
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
    private AnchorPane todoList, doingList, doneList, selectedCard, card1, card2, card3, card4, card5;
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

    @Inject
    public BoardController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
        boards = new ArrayList<>();
        boards.add(todoList);
        boards.add(doingList);
        boards.add(doneList);
        cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);
    }

    public void onDrag(MouseEvent event){
        selectedCard.getParent().toFront();
        selectedCard.toFront();
    }

    public void mousePressed(MouseEvent event){
        selectedCard = (AnchorPane) event.getTarget();
        startX = event.getX();
        startY = event.getY();
    }

    public void mouseDragged(MouseEvent event) {
        selectedCard.setLayoutX(selectedCard.getLayoutX() + event.getX() - startX);
        selectedCard.setLayoutY(selectedCard.getLayoutY() + event.getY() - startY);
    }

    public void mouseReleased(MouseEvent event) {
        Bounds cardBounds = selectedCard.getLayoutBounds();
        for(int i = 0; i < boards.size(); ++i){
            AnchorPane board = boards.get(i);
            Bounds boardBounds = board.getLayoutBounds();
            if(cardBounds.intersects(boardBounds)){
                if(!board.equals(selectedCard.getParent())) {
                    addToBoard(board);
                }
                return;
            }
        }
    }

    private void addToBoard(AnchorPane board) {
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
