package client.scenes;

import commons.*;
import client.utils.ServerUtils;
import commons.models.BoardIdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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

    /**
     * Closes the AddBoard pop-up
     */
    public void cancel(){
        clearFields();
        showCtrl.showAddBoard();
    }

    /**
     * Converts the user data into a board and sends it to the server
     */
    public void addBoard(){
        Board board = Board.create(nameField.getText(), null, new HashSet<>(),
                colorToHex(fontColor.getValue()), colorToHex(backgroundColor.getValue()));

        BoardIdResponseModel response = server.addBoard(board);

        // if creation fails or client cannot connect to the server, it will return -1
        if (response.getBoardId() == -1) {
            // show the error popup
            showCtrl.showError(response.getErrorMessage());
        }

        showCtrl.addBoard(board);
    }

    /**
     * Clears all the fields
     */
    private void clearFields() {
        nameField.clear();
    }

    /**
     * Converts the user input into a board.
     * @return board, the user created
     */
    private Board getBoard() {
        return Board.create(nameField.getText(), null, new HashSet<>(),
                colorToHex(fontColor.getValue()), colorToHex(backgroundColor.getValue()));
    }

    /**
     * Returns a hexadecimal string representation of javafx.scene.paint.Color.
     * @param color
     * @return string representation of the color.
     */
    private String colorToHex(Color color){
        String hexString = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        return hexString;
    }
}
