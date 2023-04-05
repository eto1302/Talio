package client.scenes;

import client.Services.BoardService;
import client.user.UserData;
import commons.*;
import client.utils.ServerUtils;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AddBoardController {

    private final ShowCtrl showCtrl;
    @FXML
    private TextField nameField;
    private BoardService boardService;

    @Inject
    public AddBoardController (ShowCtrl showCtrl, ServerUtils server, UserData userData){
        this.showCtrl=showCtrl;
        this.boardService = new BoardService(userData, server);
    }

    /**
     * Closes the AddBoard pop-up
     */
    public void cancel(){
        clearFields();
        showCtrl.showAddBoard();
        showCtrl.cancel();
    }

    /**
     * Converts the user data into a board and sends it to the server
     */
    public void addBoard(){
        IdResponseModel response = boardService.addBoard(nameField.getText());

        if (response.getId() == -1) {
            showCtrl.showError(response.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        Board boardUpdated = boardService.getBoard(response.getId());
        boardService.enterBoard(response.getId());
        showCtrl.addBoard(boardUpdated);
        showCtrl.cancel();
        clearFields();
        showCtrl.showBoard();
    }

    /**
     * Clears all the fields
     */
    private void clearFields() {
        nameField.clear();
    }
}
