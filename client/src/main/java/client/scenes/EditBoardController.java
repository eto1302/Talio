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

    @Inject
    public EditBoardController(UserData userData, ServerUtils serverUtils){
        this.boardService = new BoardService(userData, serverUtils);
    }
    public void cancel() {
        showCtrl.cancel();
    }

    public void edit() {
        IdResponseModel response = this.boardService.editBoard(newTitle.getText());
        if(response.getId() == -1){
            this.showCtrl.showError(response.getErrorMessage());
            this.showCtrl.cancel();
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
