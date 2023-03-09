package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import javax.inject.Inject;

public class HomeController {
    @FXML
    private Label addListLabel;
    @FXML
    private Button addListButton;
    @FXML
    private MenuButton menu;
    @FXML
    private MenuItem joinBoard;
    @FXML
    private MenuItem createBoard;
    @FXML
    private MenuItem yourBoards;
    @FXML
    private MenuItem personalize;
    @FXML
    private Label boardLabel;

    private final ShowCtrl showCtrl;

    @Inject
    public HomeController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void showAddList(){
        showCtrl.showAddList();
    }

    public void showYourBoards(){
        showCtrl.showYourBoards();
    }
}
