package client.scenes;

import client.Services.AdminService;
import client.Services.BoardService;
import client.user.UserData;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.models.IdResponseModel;
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

    @FXML
    private TableView<Board> table;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Board, String> id, name, pwd;
    private BoardService boardService;
    private AdminService adminService;

    @Inject
    public AdminController (ShowCtrl showCtrl, ServerUtils server, UserData userData){
        this.showCtrl=showCtrl;
        this.boardService = new BoardService(userData, server);
        this.adminService = new AdminService(server);
    }

    public void setup() {
        // set up the table
        id.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getId()+""));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getName()));
        pwd.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));

        // allow multiple selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // select all the text when the user clicks on the search bar
        // to make it easier for the user to type in words
        search.setOnMouseClicked(event -> search.selectAll());

        // display all the boards from the server
        Board[] boards = boardService.getAllBoards();
        refresh(boards);

        // start the thread that will pull for updates using long-polling
        pullBoardsUpdated();
    }

    // start the thread that will pull for updates using long-polling
    public void pullBoardsUpdated() {
        new Thread(() -> {
            try {

                // server will return the boards when there is an update
                // or null when the timeout is reached
                Board[] boardsUpdated = boardService.getBoardsUpdated();
                if (boardsUpdated != null) {
                    refresh(boardsUpdated);
                }

                // start the thread again if the server responded or on timeout
                pullBoardsUpdated();

            } catch (Exception e) {
                showCtrl.showError(e.getMessage());
            }
        }).start();
    }

    // update UI with the new boards
    public void refresh(Board[] boards){
        ObservableList<Board> boardsList = FXCollections.observableArrayList(boards);
        FilteredList<Board> filteredData = new FilteredList<>(boardsList, p -> true);
        // set the filter Predicate whenever the filter changes
        // (when user search for boards using the search bar)
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(board -> filterBoard(board, newValue));
        });

        table.setItems(filteredData);
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

        if (boards.size() == 0) {
            showCtrl.showError("Please select at least one board to delete.");
            return;
        }

        for(int boardId : boards.stream().map(Board::getId).toArray(Integer[]::new)){
            IdResponseModel model = boardService.deleteBoard(boardId);

            if (model.getId() == -1) {
                showCtrl.showError(model.getErrorMessage());
                return;
            }
        }

        refresh(boardService.getAllBoards());
    }

    public boolean verifyAdmin(String password){
        return adminService.verifyAdmin(password);
    }
}
