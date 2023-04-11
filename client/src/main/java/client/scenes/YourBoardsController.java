package client.scenes;

import client.Services.BoardService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.Map;

public class YourBoardsController {

    private final ShowCtrl showCtrl;
    @FXML
    private VBox boardList;
    private BoardService boardService;

    @Inject
    public YourBoardsController(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData){
        this.showCtrl = showCtrl;
        this.boardService = new BoardService(userData, serverUtils);
    }
    public void fillBoardBox() {
        this.boardList.getChildren().removeAll(this.boardList.getChildren());
        Map<Integer, String> boardMap = this.boardService.getJoinedBoards();
        for(int id : boardMap.keySet()){
            Board currentBoard = this.boardService.getBoard(id);
            if(currentBoard != null) {
                showCtrl.addBoard(currentBoard);
            }
        }
    }

    public void showSearch(){
        showCtrl.showSearch();
    }

    public void addBoard(){
        showCtrl.showAddBoard();
    }

    public void putBoard(Parent root) {
        boardList.getChildren().add(root);
    }

}
