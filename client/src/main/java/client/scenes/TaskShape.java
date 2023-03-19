package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javax.inject.Inject;
import javafx.scene.control.*;

public class TaskShape {
    @FXML
    private GridPane grid;
    @FXML
    private Label title;
    @FXML
    private Label plusSign, progressLabel;
    private ShowCtrl showCtrl;
    private ServerUtils server;
    private int id;
    @Inject
    public TaskShape(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        server=serverUtils;
    }

    public void doubleClick (){
        grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY))
                    if (event.getClickCount()==2)
                        showCtrl.showTaskOverview();
            }
        });
    }

    public Scene getSceneUpdated(Task task){
        title.setText(task.getTitle());
        if (task.getDescription()==null)
            plusSign.setVisible(false);
        return grid.getScene();
    }
}
