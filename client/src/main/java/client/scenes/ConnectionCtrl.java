package client.scenes;

import client.Services.BoardService;
import client.Services.ConnectionService;
import client.WSClientModule;
import client.user.UserData;
import client.utils.ServerUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javax.inject.Inject;

import javafx.scene.control.*;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.File;

public class ConnectionCtrl {
    @FXML
    private ComboBox<String> urlSelector;
    @FXML
    private TextField serverURL;
    private final ShowCtrl showCtrl;
    private ConnectionService connectionService;
    private BoardService boardService;

    @Inject
    public ConnectionCtrl(ServerUtils serverUtils, ShowCtrl showCtrl, UserData userData) {
        this.showCtrl = showCtrl;
        this.boardService = new BoardService(userData, serverUtils);
        this.connectionService = new ConnectionService(userData, serverUtils);
    }

    public void setup(){
        urlSelector.getItems().clear();
        urlSelector.setItems(FXCollections.observableArrayList(
                this.connectionService.getServerUrls()));
    }

    /**
     * sets the server with the URL input
     */
    public void join() {
        String url = serverURL.getText();
        if(!urlSelector.getSelectionModel().isEmpty()){
            url = urlSelector.getValue().toString();
        }

        connectionService.setUrl(url);

        url = url.replace("http", "ws");
        StompSession session = null;

        try {
            session = WSClientModule.connect(url + "ws");
        } catch (Exception e) {
            showCtrl.showError("Could not connect to server");
            return;
        }

        connectionService.setWsConfig(session);
        showCtrl.showHome();
    }


}
