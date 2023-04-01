package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.scenes.tasks.EditTaskController;
import client.utils.ServerUtils;
import commons.Tag;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class TagShapeController<T> {

    @Inject
    private ShowCtrl showCtrl;
    @Inject
    private ServerUtils serverUtils;

    @FXML
    private GridPane tagContainer;
    @FXML
    private Label tagText, tagDeleteButton;

    private Tag tag;

    private EditTaskController taskController;

    public TagShapeController() {
    }

    public void setTaskController(EditTaskController s){
        taskController = s;
    }

    //    @Inject
//    public TagShapeController(ShowCtrl showCtrl, ServerUtils serverUtils){
//        this.showCtrl = showCtrl;
//        this.serverUtils = serverUtils;
//    }

    public Scene getSceneUpdated(Tag tag){
        this.tag = tag;
        tagText.setText(tag.getName());
        Color backgroundColor = Color.web(tag.getColor());
        //if the color is dark enough, change text to white
//        if(backgroundColor.getBrightness() < 0.7){
//            tagText.setStyle("-fx-text-inner-color: white;");;
//        }
        tagContainer.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        return tagContainer.getScene();
    }

    public void removeTag(){
        //TODO
    }

    public void handleClick(){
        tagContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY))
                    if (event.getClickCount()==1 && taskController != null) {
                        //add the tag to a task if clicked in tagToTask (only place where editTaskController is set)
                        showCtrl.addTagToTask(tag, taskController);
                    } else if (event.getClickCount() == 2) {
                        //TODO: edit the tag
                    }
            }
        });
    }

}
