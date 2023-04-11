package client.scenes;

import client.Services.BoardService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ImageView lockStateIcon;
    private ShowCtrl showCtrl;
    private BoardService boardService;

    private final Image lockedImage = new Image(
            "file:client/build/resources/main/icons/lock.png");
    private final Image unlockedImage = new Image(
            "file:client/build/resources/main/icons/unlock.png");

    @Inject
    public BoardShape (ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData){
        this.showCtrl=showCtrl;
        this.boardService = new BoardService(userData, serverUtils);
    }

    /**
     * Updates the board's visual (sets the title and the colors of it)
     * based on the board object that is passed on
     * @param board the board with the necessary attributes
     */
    public void updateScene(Board board){
        nameLabel.setText(board.getName());
        commons.Color background = board.getBoardColor();
        String rgbBackground = background.getBackgroundColor();
        String rgbFont = background.getFontColor();
        Color backgroundColor = Color.web(rgbBackground);
        Color fontColor = Color.web(rgbFont);

        boardBox.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        nameLabel.setTextFill(fontColor);
        lockStateIcon.setImage(board.getPassword() != null && board.getPassword().length() > 0 ?
                lockedImage : unlockedImage);
        setId(board.getId());
    }

    public void enter(){
        this.boardService.enterBoard(id);
        this.showCtrl.showBoard();
    }

    public void leave(){
        this.boardService.leaveBoard(id);
        this.showCtrl.showYourBoards();
    }

    /**
     * Copies to clipboard the invite key of the board, to be used in later purposes
     */
    public void copy(){
        this.boardService.copyInviteLink(id);
    }
}
