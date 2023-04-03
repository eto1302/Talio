package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.scenes.tasks.EditTaskController;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Tag;
import commons.Task;
import commons.models.IdResponseModel;
import commons.sync.ListAdded;
import commons.sync.TagAddedToTask;
import commons.sync.TagDeleted;
import commons.sync.TagRemovedFromTask;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.catalina.User;

import javax.inject.Inject;

public class TagShapeController<T> {

    @Inject
    private ShowCtrl showCtrl;
    @Inject
    private ServerUtils serverUtils;
    @Inject
    private UserData userData;

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


    public Scene getSceneUpdated(Tag tag){
        this.tag = tag;
        tagText.setText(tag.getName());
        Color backgroundColor = Color.web(tag.getColor().getBackgroundColor());
        tagContainer.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        return tagContainer.getScene();
    }

    public void handleDelete(){
        IdResponseModel resp;
        if(inEditTask()){
            resp = userData.updateBoard(new TagRemovedFromTask(tag.getBoardId(),tag, taskController.getTask()));
        }else{
            resp = userData.updateBoard(new TagDeleted(tag.getBoardId(), tag));
        }
        if (resp.getId() == -1) {
            showCtrl.showError(resp.getErrorMessage());
            showCtrl.cancel();
        }
    }

    public void handleHighlight(){
          tagContainer.setOnMouseEntered(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                  tagContainer.setStyle("-fx-border-width: 2px;-fx-border-color: blue");
//                  tagContainer.setBorder(new Border(new BorderStroke(Color.BLUEVIOLET, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, )));
              }
          });
          tagContainer.setOnMouseExited(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                  tagContainer.setStyle("-fx-border-width: 0");
              }
          });
    }


    public void handleClick(){
        tagText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY))
                    if (event.getClickCount() == 2) {
                        showCtrl.showEditTag(tag);
                    } else if (event.getClickCount()==1 && inEditTask()) {

                        if(validateTagBeforeAdd(tag, taskController.getTask())) return;
                        showCtrl.closePopUp();
                        IdResponseModel model = userData.updateBoard(new TagAddedToTask(tag.getBoardId(), taskController.getTask(), tag));
                        if (model.getId() == -1) {
                            showCtrl.showError(model.getErrorMessage());
                            showCtrl.cancel();
                        }
                    }
            }
        });
    }

    private boolean validateTagBeforeAdd(Tag tag, Task task){
        java.util.List<Tag> tags = serverUtils.getTagByTask(task.getId());
        return tags.contains(tag);
    }

    private boolean inEditTask(){
        return taskController != null;
    }
}
