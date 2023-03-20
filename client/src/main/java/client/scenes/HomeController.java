package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;


public class HomeController {
    @FXML
    private Label addListLabel, homeLabel;
    @FXML
    private VBox vbox;
    @FXML
    private GridPane gridHome;
    @FXML
    private Button addListButton;
    @FXML
    private MenuItem joinBoard, addBoard, yourBoards, personalize;

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

    public Scene addList(Label list){
        gridHome.add(list, 0, 1);
        return gridHome.getScene();
    }
}
