package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.Map;

public class YourBoardsController {

    private final ShowCtrl showCtrl;
    @FXML
    private VBox boardList;
    private ServerUtils serverUtils;
    @Inject
    private UserData userData;

    @Inject
    public YourBoardsController(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }
    public void fillBoardBox() {
        this.boardList.getChildren().removeAll(this.boardList.getChildren());
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
