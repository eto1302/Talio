package client.scenes;

import commons.*;
import client.utils.ServerUtils;
import commons.models.BoardIdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.HashSet;

public class AddBoardController {

    private final ShowCtrl showCtrl;
    private final ServerUtils server;

    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField nameField;
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker fontColor;

    @Inject
    public AddBoardController (ShowCtrl showCtrl, ServerUtils server){
        this.showCtrl=showCtrl;
        this.server = server;
    }

    public void cancel(){
        nameField.clear();
        showCtrl.cancel();
    }

    public void addBoard(){
        Board board = Board.create(nameField.getText(), "pwd", new HashSet<>());

        BoardIdResponseModel response = server.addBoard(board);

        // if creation fails or client cannot connect to the server, it will return -1
        if (response.getBoardId() == -1) {
            // show the error popup
            showCtrl.showError(response.getErrorMessage());
        }

        nameField.clear();
        showCtrl.cancel();
    }
}
