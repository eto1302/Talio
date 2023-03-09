package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;

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

    public void cancel(){
       showCtrl.cancel();
    }

    /**
     * This button adds a list into the grid that will contain everything else and be set as the root of the scene
     */
    public void addList() {
        List list = List.create(nameField.getText(), null);
        GridPane grid=insertList(list);
        showCtrl.addList(grid);
        showCtrl.cancel();
    }

    /**
     * Inserts a card into a grid in another grid that will be set with the proper properties.
     * @param list The list that will be inserted
     * @return A manual made grid
     */
    public GridPane insertList(List list){
        GridPane grid = new GridPane();
        for (int i=0; i<=5; i++){
            Label label = new Label(list.getName()+i);
            label.setBackground(new Background(new BackgroundFill(color.getValue(), null, null)));
            grid.addRow(i, label);
        }
        grid.setStyle("-fx-vgap: 4px");
        return grid;
    }
}
