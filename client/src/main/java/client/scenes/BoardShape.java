package client.scenes;

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
    @Inject
    public BoardShape (ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }

    /**
     * Updates the board's visual (sets the title and the colors of it)
     * based on the board object that is passed on
     * @param board the board with the necessary attributes
     * @return the updated scene after modifications
     */
    public Scene getSceneUpdated(Board board){
        nameLabel.setText(board.getName());
        String rgbBackground = board.getBackgroundColor();
        String rgbFont = board.getFontColor();
        Color backgroundColor = Color.web(rgbBackground);
        Color fontColor = Color.web(rgbFont);

        boardBox.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        nameLabel.setTextFill(fontColor);
        setId(board.getId());
        return boardBox.getScene();
    }

    public void enter(){
        System.out.println(getId());
    }
}
