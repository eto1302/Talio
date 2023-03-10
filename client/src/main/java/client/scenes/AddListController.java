package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.inject.Inject;

import static com.google.inject.Guice.createInjector;

public class AddListController {

    @FXML
    private TextField nameField;
    @FXML
    private ColorPicker color;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;

    private final ShowCtrl showCtrl;
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    @Inject
    public AddListController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void showBoard() {showCtrl.showBoard();}

    public void cancel(){
       showCtrl.cancel();
    }

    /**
     * This button adds a list back into the grid
     */
    public void addList() {
        List list = List.create(nameField.getText(), null);
        showCtrl.showBoardUpdated(new Label(list.getName()));
        showCtrl.cancel();
    }

}
