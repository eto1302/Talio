package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;

import javax.inject.Inject;
import javafx.scene.control.*;

public class ConnectionCtrl {

    @FXML
    private TextField serverURL;
    @FXML
    private Button joinServerButton;

    private final ShowCtrl showCtrl;
    private final ServerUtils serverUtils;

    @Inject
    public ConnectionCtrl(ServerUtils serverUtils, ShowCtrl showCtrl) {
        this.serverUtils = serverUtils;
        this.showCtrl = showCtrl;
    }

    /**
     * sets the server with the URL input
     */
    public void join(){
        showCtrl.showHome();
    }


}
