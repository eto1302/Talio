package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import javax.inject.Inject;

public class HomeController {
    @FXML
    private Label addCardLabel;
    @FXML
    private Button addCardButton;
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

    private final MainCtrl mainCtrl;

    @Inject
    public HomeController(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void showAddCard(){
        mainCtrl.showAddCard();
    }
}
