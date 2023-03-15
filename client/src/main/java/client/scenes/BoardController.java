package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class BoardController {
    @FXML
    private MenuItem joinBoard, addBoard, yourBoards, personalize;
    @FXML
    private Label boardLabel;
    @FXML
    private VBox toDoBox, doingBox, doneBox;
    @FXML
    private HBox listBox, tagBoxToDo, tagBoxDoing, tagBoxDone;
    @FXML
    private GridPane toDo, doing, done;
    @FXML
    private MenuItem addTaskToDo, addTaskDoing, addTaskDone, addTagToDo,
        addTagDoing, addTagDone;

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

    public void showEditList() {};

    public void showConfirmDelete() {};

    public void addBoard(){
        showCtrl.showAddBoard();
    }

    public void showAddList(){
        showCtrl.showAddList();
    }

    /**
     * Puts the root of the scene (the grid representing the list) inside the board
     * @param scene ,whose root we are looking to add to our board
     * @return the updated scene of the board
     */
    public Scene putList(Scene scene){

        listBox.getChildren().add(scene.getRoot());
        return boardLabel.getScene();
    }

}
