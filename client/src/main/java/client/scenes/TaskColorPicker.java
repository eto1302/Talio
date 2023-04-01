package client.scenes;

import client.user.UserData;
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
    private UserData userData;
    @Inject
    private ShowCtrl showCtrl;
    private Task task;

    public TaskColorPicker(){

    }

    public void close(){
        showCtrl.cancel();
    }

    public void fillTaskColors(){
        this.taskColorList.getChildren().removeAll(this.taskColorList.getChildren());
        List<commons.Color> colors = this.userData.getCurrentBoard().getColors();
        if(colors == null) return;
        for(int i = 2; i < colors.size(); ++i){
            commons.Color color = colors.get(i);
            showCtrl.addSelectTaskColor(color, task);
        }
    }

    public void setup(Task task) {
        Board currentBoard = this.userData.getCurrentBoard();
        this.boardLabel.setText(currentBoard.getName());
        this.task = task;
        this.fillTaskColors();
    }

    public Scene putColor(Scene taskScene){
        taskColorList.getChildren().add(taskScene.getRoot());
        return boardLabel.getScene();
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
