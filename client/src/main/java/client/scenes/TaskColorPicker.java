package client.scenes;

import client.Services.BoardService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.List;

public class TaskColorPicker {

    @FXML
    private Label boardLabel;
    @FXML
    private VBox taskColorList;
    @Inject
    private ShowCtrl showCtrl;
    private Task task;
    private BoardService boardService;
    @Inject
    public TaskColorPicker(UserData userData, ServerUtils serverUtils){
        this.boardService = new BoardService(userData, serverUtils);
    }

    public void close(){
        showCtrl.cancel();
    }

    public void fillTaskColors(){
        this.taskColorList.getChildren().removeAll(this.taskColorList.getChildren());
        List<commons.Color> colors = this.boardService.getCurrentBoard().getColors();
        if(colors == null) return;
        for(int i = 2; i < colors.size(); ++i){
            commons.Color color = colors.get(i);
            showCtrl.addSelectTaskColor(color, task);
        }
    }

    public void setup(Task task) {
        Board currentBoard = this.boardService.getCurrentBoard();
        this.boardLabel.setText(currentBoard.getName());
        this.task = task;
        this.fillTaskColors();
    }

    public Scene putColor(Scene taskScene){
        taskColorList.getChildren().add(taskScene.getRoot());
        return boardLabel.getScene();
    }
}
