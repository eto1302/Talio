package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

import javax.inject.Inject;

public class YourBoardsController {

    private final ShowCtrl showCtrl;
    @FXML
    private Button searchButton;
    @FXML
    private Button mainBoardEnter;
    @FXML
    private MenuItem joinBoard;
    @FXML
    private MenuItem createBoard;
    @FXML
    private MenuItem personalize;

    @Inject
    public YourBoardsController(ShowCtrl showCtrl){
        this.showCtrl = showCtrl;
    }
}
