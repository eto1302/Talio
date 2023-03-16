package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

import javax.inject.Inject;

public class YourBoardsController {

    private final ShowCtrl showCtrl;
    @FXML
    private MenuItem joinBoard;
    @FXML
    private MenuItem addBoard;
    @FXML
    private MenuItem personalize;
    @FXML
    private Label mainBoardLabel;
    @FXML
    private Label mainBoardDescription;
    @FXML
    private Button enterButton;
    @FXML
    private Button leaveButton;
    @FXML
    private Button copyButton;
    @FXML
    private Button searchButton;
    @FXML
    private HBox boardBox;

    @Inject
    public YourBoardsController(ShowCtrl showCtrl){
        this.showCtrl = showCtrl;
    }

    public void showSearch(){
        showCtrl.showSearch();
    }

    public void addBoard(){
        showCtrl.showAddBoard();
    }

    /**
     * Puts the root of the scene (the grid representing the board) inside the scene
     * @param scene ,whose root we are looking to add to our scene
     * @return the updated scene
     */
    public Scene putBoard(Scene scene){
        boardBox.getChildren().add(scene.getRoot());
        return mainBoardLabel.getScene();
    }
}
