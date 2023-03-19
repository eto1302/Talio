package client.scenes;

import client.utils.ServerUtils;
import commons.List;
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
    private Label plusSign, progressLabel, title, deleteX;
    private ShowCtrl showCtrl;
    private ServerUtils server;
    private int id;
    private List list;
    @Inject
    public TaskShape(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        server=serverUtils;
    }

    /**
     * On double-click, this will show the window containing the overview (details of the task)
     */
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

    /**
     * Sets the content of the task accordingly.
     * @param task the task whose information will be displayed
     * @return the new scene updated
     */
    public Scene getSceneUpdated(Task task){
        title.setText(task.getTitle());
        if (task.getDescription()==null)
            plusSign.setVisible(false);
        return grid.getScene();
    }

    /**
     * deletes the task
     */
    public void delete(){
        deleteX.setOnMouseClicked(event -> {
            VBox parent = (VBox) grid.getParent();
            parent.getChildren().remove(grid);
        });
    }

    /**
     * Sets the information
     * @param id the id of the task
     * @param list the task's list
     */
    public void setup(int id, List list){
        this.id= id;
        this.list=list;
    }
}
