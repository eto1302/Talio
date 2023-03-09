package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import static com.google.inject.Guice.createInjector;

public class HomeController {
    @FXML
    private Label addCardLabel;
    @FXML
    private VBox vbox;
    @FXML
    private GridPane gridHome;
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

    private final ShowCtrl showCtrl;
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    @Inject
    public HomeController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void showAddCard(){
        showCtrl.showAddCard();
    }

    public void showYourBoards(){
        showCtrl.showYourBoards();
    }

}
