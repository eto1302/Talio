package client.scenes;

import client.Services.ConnectionService;
import client.user.UserData;
import client.utils.ServerUtils;

import javax.inject.Inject;


public class HomeController {

    private final ShowCtrl showCtrl;
    private final ConnectionService connectionService;

    @Inject
    public HomeController(ShowCtrl showCtrl, UserData userData, ServerUtils serverUtils) {
        this.showCtrl = showCtrl;
        this.connectionService = new ConnectionService(userData, serverUtils);
    }

    public void showAddBoard(){
        showCtrl.showAddBoard();
    }

    public void showYourBoards(){
        showCtrl.showYourBoards();
    }

    public void showSearch() {showCtrl.showSearch();}

    public void setServer() {
        this.connectionService.disconnect();
        showCtrl.showConnection();
    }

    public void showAdmin() {
        showCtrl.showAdmin();
    }
}
