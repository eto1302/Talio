package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import static com.google.inject.Guice.createInjector;

public class HomeController {
    @FXML
    private Label addListLabel;
    @FXML
    private VBox vbox;
    @FXML
    private GridPane gridHome;
    @FXML
    private Button addListButton;
    @FXML
    private MenuButton menu;
    @FXML
    private MenuItem joinBoard;
    @FXML
    private MenuItem addBoard;
    @FXML
    private MenuItem yourBoards;
    @FXML
    private MenuItem personalize;
    @FXML
    private Label boardLabel;

    private final ShowCtrl showCtrl;
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    @Inject
    public HomeController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void showAddList(){
        showCtrl.showAddList();
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
