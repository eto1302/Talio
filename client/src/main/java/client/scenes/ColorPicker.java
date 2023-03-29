package client.scenes;

import client.user.UserData;
import commons.Board;
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

    }

    public void reset(){
        this.boardBackground.setValue(Color.web("#FFFFFF"));
        this.boardFont.setValue(Color.web("#000000"));
        this.listBackground.setValue(Color.web("#FFFFFF"));
        this.listFont.setValue(Color.web("#000000"));
    }

    public void fillTaskColors(){
        this.taskColorList.getChildren().removeAll(this.taskColorList.getChildren());
        List<commons.Color> colors = this.userData.getCurrentBoard().getTaskColors();
        if(colors == null) return;
        for(commons.Color color : colors){
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
        this.showCtrl.showAddTagColor();
    }
}
