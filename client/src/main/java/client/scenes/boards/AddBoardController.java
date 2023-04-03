package client.scenes.boards;

import client.scenes.ShowCtrl;
import commons.*;
import client.utils.ServerUtils;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
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
        commons.Color boardColor = commons.
                Color.create("#000000", "#FFFFFF");
        commons.Color listColor = commons.
                Color.create("#000000", "#FFFFFF");
        boardColor.setIsDefault(true);
        listColor.setIsDefault(true);

        IdResponseModel boardColorResponse = server.addColor(boardColor);
        IdResponseModel listColorResponse = server.addColor(listColor);
        boardColor.setId(server.addColor(boardColor).getId());
        listColor.setId(server.addColor(listColor).getId());
        List<commons.Color> colors = new ArrayList<>();
        colors.add(boardColor);
        colors.add(listColor);

        // if creation fails or client cannot connect to the server, it will return -1
        if (boardColorResponse.getId() == -1) {
            showCtrl.showError(boardColorResponse.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        if (listColorResponse.getId() == -1) {
            showCtrl.showError(listColorResponse.getErrorMessage());
            showCtrl.cancel();
            return;
        }

        Board board = Board.create(nameField.getText(), null,
                new ArrayList<>(), colors, new ArrayList<>());
        String inviteKey = generateInviteKey();
        board.setInviteKey(inviteKey);

        IdResponseModel response = server.addBoard(board);
        server.setColorToBoard(boardColor, response.getId());
        server.setColorToBoard(listColor, response.getId());

        if (response.getId() == -1) {
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
        return Board.create(nameField.getText(), null,
                new ArrayList<>(),new ArrayList<>(), new ArrayList<>());
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
