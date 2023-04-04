package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;


public class HomeController {
    @FXML
    private GridPane gridHome;

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
