package client.scenes;

import client.Services.BoardService;
import client.Services.ColorService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.models.IdResponseModel;
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
    private ShowCtrl showCtrl;
    private ColorService colorService;
    private BoardService boardService;

    @Inject
    public ColorPicker(UserData userData, ServerUtils serverUtils){
        this.colorService = new ColorService(userData, serverUtils);
        this.boardService = new BoardService(userData, serverUtils);
    }

    public void save() {
        IdResponseModel responseModel = colorService.editColor(
                -1,
                boardFont.getValue(), boardBackground.getValue(), false);

        if(responseModel.getId() == -1){
            showCtrl.showError(responseModel.getErrorMessage());
            showCtrl.cancel();
            return;
        }

        responseModel = colorService.editColor(-2,
                listFont.getValue(), listBackground.getValue(), false);

        if(responseModel.getId() == -1){
            showCtrl.showError(responseModel.getErrorMessage());
            showCtrl.cancel();
            return;
        }

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
        this.taskColorList.getChildren().clear();
        List<commons.Color> colors = this.boardService.getCurrentBoard().getColors();
        if(colors == null) return;
        for(int i = 2; i < colors.size(); ++i){
            commons.Color color = colors.get(i);
            showCtrl.addTaskColor(color);
        }
    }

    public void setup() {
        Board currentBoard = this.boardService.getCurrentBoard();
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
}
