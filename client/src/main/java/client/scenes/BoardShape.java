package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class BoardShape {

    private int id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @FXML
    private GridPane boardBox;
    @FXML
    private Label nameLabel;
    @FXML
    private HBox tagBox;
    @FXML
    private Button enterButton;
    @FXML
    private Button leaveButton;
    @FXML
    private Button copyButton;
    private ShowCtrl showCtrl;
    private ServerUtils server;
    @Inject
    private UserData userData;
    @Inject
    public BoardShape (ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        this.server=serverUtils;
    }

    /**
     * Updates the board's visual (sets the title and the colors of it)
     * based on the board object that is passed on
     * @param board the board with the necessary attributes
     * @return the updated scene after modifications
     */
    public Scene getSceneUpdated(Board board){
        nameLabel.setText(board.getName());
        commons.Color background = board.getBoardColor();
        String rgbBackground = background.getBackgroundColor();
        String rgbFont = background.getFontColor();
        Color backgroundColor = Color.web(rgbBackground);
        Color fontColor = Color.web(rgbFont);

        boardBox.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        nameLabel.setTextFill(fontColor);
        setId(board.getId());
        return boardBox.getScene();
    }

    public void enter(){
        this.userData.openBoard(id);
        this.showCtrl.showBoard();
    }

    public void leave(){
        this.userData.leaveBoard(id);
        this.userData.saveToDisk();
        this.showCtrl.showYourBoards();
    }

    /**
     * Copies to clipboard the invite key of the board, to be used in later purposes
     */
    public void copy(){
        Board board = server.getBoard(id);
        String inviteKey = board.getInviteKey();

        StringSelection string = new StringSelection(inviteKey);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(string, null);
    }
}
