package client.scenes;

import client.WSClientModule;
import client.messageClients.MessageAdmin;
import client.messageClients.MessageSender;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Color;
import javafx.fxml.FXML;

import javax.inject.Inject;
import javafx.scene.control.*;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.List;

public class ConnectionCtrl {

    @FXML
    private TextField serverURL;

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
        String url = serverURL.getText();

        serverUtils.setUrl(url);

        url = url.replace("http", "ws");
        StompSession session = null;


        try {
            session = WSClientModule.connect(url+"ws");
        } catch (Exception e) {
            showCtrl.showError("Could not connect to server");
            return;
        }

        userData.setWsConfig(new MessageAdmin(session), new MessageSender(session));

        if (serverUtils.getBoard(1)==null){
            Color boardColor = Color.create("#000000", "#FFFFFF");
            Color listColor = Color.create("#000000", "#FFFFFF");
            boardColor.setIsDefault(true);
            listColor.setIsDefault(true);

            boardColor.setId(serverUtils.addColor(boardColor).getId());
            listColor.setId(serverUtils.addColor(listColor).getId());
            List<Color> colors = new ArrayList<>();
            colors.add(boardColor);
            colors.add(listColor);
            Board board = Board.create("Default", null,
                    new ArrayList<>(), colors, new ArrayList<>());
            serverUtils.addBoard(board);
            serverUtils.setColorToBoard(boardColor, 1);
            serverUtils.setColorToBoard(listColor, 1);
        }
        if(userData.getCurrentBoard() == null)
            userData.openBoard(1);
        showCtrl.showBoard();
    }


}
