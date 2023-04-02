package client.scenes;

import client.user.UserData;
import commons.Board;
import commons.models.ColorEditModel;
import commons.models.IdResponseModel;
import commons.sync.ColorEdited;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.List;

public class ColorPicker {

    @FXML
    private Label boardLabel;
    @FXML
    private javafx.scene.control.ColorPicker boardBackground;
    @FXML
    private javafx.scene.control.ColorPicker boardFont;
    @FXML
    private javafx.scene.control.ColorPicker listBackground;
    @FXML
    private javafx.scene.control.ColorPicker listFont;
    @FXML
    private VBox taskColorList;
    @Inject
    private UserData userData;
    @Inject
    private ShowCtrl showCtrl;
    public ColorPicker(){

    }

    public void save(){
        Board board = this.userData.getCurrentBoard();
        commons.Color boardColor = commons.Color.create(
                colorToHex(boardFont.getValue()), colorToHex(boardBackground.getValue()));
        commons.Color listColor = commons.Color.create(
                colorToHex(listFont.getValue()), colorToHex(listBackground.getValue()));

        boolean boardDefault = boardColor.getBackgroundColor().equals("#FFFFFF")
                && boardColor.getFontColor().equals("#000000");
        boolean listDefault = listColor.getBackgroundColor().equals("#FFFFFF")
                && listColor.getFontColor().equals("#000000");

        ColorEditModel boardColorEdit = new ColorEditModel(
                boardColor.getBackgroundColor(), boardColor.getFontColor(), boardDefault);
        ColorEditModel listColorEdit = new ColorEditModel(
                listColor.getBackgroundColor(), listColor.getFontColor(), listDefault);


        IdResponseModel boardModel = userData.updateBoard(new ColorEdited(
                board.getId(), board.getBoardColor().getId(), boardColorEdit));
        if (boardModel.getId() == -1) {
            showCtrl.showError(boardModel.getErrorMessage());
            showCtrl.cancel();
            return;
        }

        IdResponseModel listModel = userData.updateBoard(new ColorEdited(
                board.getId(), board.getListColor().getId(), listColorEdit));
        if (listModel.getId() == -1) {
            showCtrl.showError(listModel.getErrorMessage());
            showCtrl.cancel();
            return;
        }

        this.userData.openBoard(board.getId());
        showCtrl.cancel();
        showCtrl.showEditBoard();
    }

    public void reset(){
        this.boardBackground.setValue(Color.web("#FFFFFF"));
        this.boardFont.setValue(Color.web("#000000"));
        this.listBackground.setValue(Color.web("#FFFFFF"));
        this.listFont.setValue(Color.web("#000000"));
    }

    public void fillTaskColors(){
        this.taskColorList.getChildren().removeAll(this.taskColorList.getChildren());
        List<commons.Color> colors = this.userData.getCurrentBoard().getColors();
        if(colors == null) return;
        for(int i = 2; i < colors.size(); ++i){
            commons.Color color = colors.get(i);
            showCtrl.addTaskColor(color);
        }
    }

    public void setup() {
        Board currentBoard = this.userData.getCurrentBoard();
        this.boardLabel.setText(currentBoard.getName());
        this.boardBackground.setValue(Color.web(currentBoard.getBoardColor().getBackgroundColor()));
        this.boardFont.setValue(Color.web(currentBoard.getBoardColor().getFontColor()));
        this.listBackground.setValue(Color.web(currentBoard.getListColor().getBackgroundColor()));
        this.listFont.setValue(Color.web(currentBoard.getListColor().getFontColor()));
        this.fillTaskColors();
    }

    public Scene putColor(Scene taskScene){
        taskColorList.getChildren().add(taskScene.getRoot());
        return boardLabel.getScene();
    }

    public void showAddTaskColor() {
        this.showCtrl.cancel();
        this.showCtrl.showAddTagColor();
    }

    /**
     * Returns a hexadecimal string representation of javafx.scene.paint.Color.
     * @param color the color to be transformed
     * @return string representation of the color.
     */
    private String colorToHex(javafx.scene.paint.Color color){
        String hexString = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        return hexString;
    }
}
