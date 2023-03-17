package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class YourBoardsController {

    private final ShowCtrl showCtrl;
    @FXML
    private MenuItem joinBoard;
    @FXML
    private MenuItem addBoard;
    @FXML
    private MenuItem personalize;
    @FXML
    private GridPane boardBox;
    @FXML
    private VBox boardList;
    @FXML
    private Button searchButton;
    private ServerUtils serverUtils;

    @Inject
    public YourBoardsController(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }
    public void fillBoardBox() {
        Board[] boards = serverUtils.getAllBoards();
        for(int i = 0; i < boards.length; ++i){
            Board currentBoard = boards[i];
            showCtrl.addBoard(currentBoard);
        }
    }

    public void showSearch(){
        showCtrl.showSearch();
    }

    public void addBoard(){
        showCtrl.showAddBoard();
    }

    public Scene putBoard(Scene scene){
        boardList.getChildren().add(scene.getRoot());
        return boardList.getScene();
    }
}
