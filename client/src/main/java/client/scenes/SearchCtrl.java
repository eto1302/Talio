package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class SearchCtrl {
    private ShowCtrl showCtrl;

    @FXML
    private Button searchButton;
    @FXML
    private TextField inviteKeyField;
    private ServerUtils server;

    @Inject
    public SearchCtrl(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl = showCtrl;
        server=serverUtils;
    }

    public void search(){
        String inviteKey = inviteKeyField.getText();
        Board board = server.getBoardByInviteKey(inviteKey);

        if (board==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Invalid invite key. Try again?");
        }
        else{
          //  showCtrl.showBoard();
            showCtrl.cancel();
        }
    }
}
