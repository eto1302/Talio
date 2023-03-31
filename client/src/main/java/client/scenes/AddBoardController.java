package client.scenes;

import commons.*;
import client.utils.ServerUtils;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class AddBoardController {

    private final ShowCtrl showCtrl;
    private final ServerUtils server;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField nameField;

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
        showCtrl.cancel();
    }

    /**
     * Converts the user data into a board and sends it to the server
     */
    public void addBoard(){
        Board board = Board.create(nameField.getText(), null, new HashSet<>(),
                1,1, new ArrayList<>());
        String inviteKey = generateInviteKey();
        board.setInviteKey(inviteKey);

        IdResponseModel response = server.addBoard(board);

        // if creation fails or client cannot connect to the server, it will return -1
        if (response.getId() == -1) {
            // show the error popup
            showCtrl.showError(response.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        Board boardUpdated = server.getBoard(response.getId());

        showCtrl.addBoard(boardUpdated);
        showCtrl.cancel();
        clearFields();
        showCtrl.showBoard();
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
                1,1, new ArrayList<>());
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

    /**
     * Generates a random 15-character string to serve as the invite key
     * @return the generated invite key
     */
    private String generateInviteKey(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i=0; i<15; i++){
            int randomInt = random.nextInt(26);
            char letter = (char) (randomInt +'a');
            sb.append(letter);
        }

        return sb.toString();
    }
}
