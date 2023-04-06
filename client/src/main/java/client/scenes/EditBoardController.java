package client.scenes;

import client.Services.BoardService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;
public class EditBoardController {

    @FXML
    private TextField newTitle;
    @Inject
    private ShowCtrl showCtrl;
    private BoardService boardService;
    private UserData userData;

    @Inject
    public EditBoardController(UserData userData, ServerUtils serverUtils){
        this.boardService = new BoardService(userData, serverUtils);
        this.userData = userData;
    }
    public void cancel() {
        showCtrl.cancel();
    }

    public void edit() {
        if(userData.isCurrentBoardLocked()){
            userData.showError();
            return;
        }
        IdResponseModel response = this.boardService.editBoard(newTitle.getText());
        if(response.getId() < 0){
            this.showCtrl.cancel();
            this.showCtrl.showError(response.getErrorMessage());
            return;
        }
        cancel();
    }

    public void setup(){
        Board currentBoard = this.boardService.getCurrentBoard();
        this.newTitle.setText(currentBoard.getName());
    }

    public void showColorPicker() {
        showCtrl.cancel();
        showCtrl.showColorPicker();
    }
}
