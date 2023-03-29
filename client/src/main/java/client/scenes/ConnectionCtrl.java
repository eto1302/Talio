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
import java.util.List;

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
            Board board = Board.create("Default", null, new HashSet<>(),
                    Color.create("#000000", "#D3D3D3"),
                    Color.create("#000000", "#D3D3D3"));
            List<Color> taskColors = new ArrayList<>();
            taskColors.add(Color.create("#000000", "#D3D3D3"));
            board.setTaskColors(taskColors);
            serverUtils.addBoard(board);
        }
        if(userData.getCurrentBoard() == null)
            userData.openBoard(1);
        showCtrl.showBoard();
    }


}
