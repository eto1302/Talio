package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.io.IOException;

import static com.google.inject.Guice.createInjector;

public class AddCardController {

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
    public AddCardController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void cancel(){
       showCtrl.cancel();
    }

    public void addCard() throws IOException {
//        Card card = new Card(nameField.getText(), new ArrayList<>());
//        var home = FXML.load(HomeController.class, "client", "scenes", "Home.fxml");
//        home.getKey().insertCard(card);
//        showCtrl.cancel();
    }
}
