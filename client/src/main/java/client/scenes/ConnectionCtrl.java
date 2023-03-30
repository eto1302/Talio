package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Color;
import javafx.fxml.FXML;

import javax.inject.Inject;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.HashSet;

public class ConnectionCtrl {

    @FXML
    private TextField serverURL;
    @FXML
    private Button joinServerButton;

    private final ShowCtrl showCtrl;
    private final ServerUtils serverUtils;

    @Inject
    private UserData userData;

    @Inject
    public ConnectionCtrl(ServerUtils serverUtils, ShowCtrl showCtrl) {
        this.serverUtils = serverUtils;
        this.showCtrl = showCtrl;
    }

    /**
     * sets the server with the URL input
     */
    public void join(){
        serverUtils.setUrl(serverURL.getText());
        if (serverUtils.getBoard(1)==null){
            Color boardColor = Color.create("#000000", "#FFFFFF");
            Color fontColor = Color.create("#000000", "#FFFFFF");
            boardColor.setId(1);
            fontColor.setId(2);
            Board board = Board.create("Default", null,
                    new HashSet<>(), 0,0, new ArrayList<>());
            serverUtils.addBoard(board);
            serverUtils.addColor(boardColor, 1);
        }
        if(userData.getCurrentBoard() == null)
            userData.openBoard(1);
        showCtrl.showBoard();
    }


}
