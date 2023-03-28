package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.Map;

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
    private UserData userData;

    @Inject
    public YourBoardsController(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }
    public void fillBoardBox() {
        Map<Integer, String> boardMap = this.userData.getBoards();
        for(int id : boardMap.keySet()){
            Board currentBoard = this.serverUtils.getBoard(id);
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
