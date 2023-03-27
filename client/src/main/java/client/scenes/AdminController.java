package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.models.IdResponseModel;
import commons.sync.BoardDeleted;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class AdminController {

    private final ShowCtrl showCtrl;
    private final ServerUtils server;

    @FXML
    private TableView<Board> table;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Board, String> id, name, pwd;

    @Inject
    private UserData userData;

    @Inject
    public AdminController (ShowCtrl showCtrl, ServerUtils server){
        this.showCtrl=showCtrl;
        this.server = server;
    }

    public void setup(){
        userData.subscribeToAdmin();

        Board[] boards = server.getAllBoards();
        ObservableList<Board> boardsList = FXCollections.observableArrayList(boards);

        // set up the table
        id.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getId()+""));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getName()));
        pwd.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));

        FilteredList<Board> filteredData = new FilteredList<>(boardsList, p -> true);

        // set the filter Predicate whenever the filter changes
        // (when user search for boards using the search bar)
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(board -> filterBoard(board, newValue));
        });

        table.setItems(filteredData);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        search.setOnMouseClicked(event -> search.selectAll());
    }


    public void cancel(){
        showCtrl.showHome();
    }

    // search for boards by the input in the search bar
    public boolean filterBoard(Board board, String newValue) {
        if(newValue == null || newValue.isEmpty()) return true;
        String lowerCaseFilter = newValue.toLowerCase();

        if ((board.getName()!=null && board.getName().toLowerCase().contains(lowerCaseFilter))
                || (board.getId()+"").toLowerCase().contains(lowerCaseFilter)
                || (board.getPassword()!=null
                && board.getPassword().toLowerCase().contains(lowerCaseFilter)))
            return true;
        return false;

    }

    public void selectAll(){
        table.getSelectionModel().selectAll();
    }

    public void delete(){
        List<Board> boards = table.getSelectionModel().getSelectedItems();
        for(Board board : boards){
            BoardDeleted boardDeleted = new BoardDeleted(board.getId());
            IdResponseModel model = userData.deleteBoard(boardDeleted);

            if (model.getId() == -1) {
                showCtrl.showError(model.getErrorMessage());
                return;
            }
        }
    }

    public boolean verifyAdmin(String password){
        return server.verifyAdmin(password);
    }
}
