package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import javax.inject.Inject;

public class BoardController {
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
    public BoardController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void showYourBoards(){
        showCtrl.showYourBoards();
    }

    public void showSearch() {showCtrl.showSearch();}

    public void showDetails() {};

    public void showEditCard() {};

    public void showConfirmDelete() {};
}
