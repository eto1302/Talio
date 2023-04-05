package client.scenes;

import javax.inject.Inject;


public class HomeController {

    private final ShowCtrl showCtrl;

    @Inject
    public HomeController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void showAddBoard(){
        showCtrl.showAddBoard();
    }

    public void showYourBoards(){
        showCtrl.showYourBoards();
    }

    public void showSearch() {showCtrl.showSearch();}

    public void showConnection() {
        showCtrl.showConnection();
    }

    public void showAdmin() {
        showCtrl.showAdmin();
    }
}
