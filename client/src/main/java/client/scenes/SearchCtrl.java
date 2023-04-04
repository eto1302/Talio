package client.scenes;

import client.Services.BoardService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class SearchCtrl {
    private ShowCtrl showCtrl;
    @FXML
    private TextField inviteKeyField;
    private BoardService boardService;

    @Inject
    public SearchCtrl(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData){
        this.showCtrl = showCtrl;
        this.boardService = new BoardService(userData, serverUtils);
    }

    public void search(){
        IdResponseModel response = this.boardService.search(inviteKeyField.getText());
        if(response.getId() == -1){
            this.showCtrl.showError(response.getErrorMessage());
            this.showCtrl.cancel();
            return;
        }
        showCtrl.showBoard();
        showCtrl.cancel();
    }
}
