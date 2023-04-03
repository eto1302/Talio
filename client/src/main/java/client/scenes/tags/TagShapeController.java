package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.scenes.tasks.EditTaskController;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Tag;
import commons.Task;
import commons.models.IdResponseModel;
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

import javax.inject.Inject;

public class TagShapeController {

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

    /**
     * Method used to set the EditTaskController when creating shapes for
     * EditTask and AddTagToTask
     * @param s
     */
    public void setTaskController(EditTaskController s){
        taskController = s;
    }


    /**
     * Sets fields and returns a scene containing the tagShape
     * @param tag tag we are showing
     * @return Scene with a tag
     */
    public Scene getSceneUpdated(Tag tag){
        this.tag = tag;

        tagText.setText(tag.getName());
        tagText.setTextFill(Color.web(tag.getColor().getFontColor()));
        tagDeleteButton.setTextFill(Color.web(tag.getColor().getFontColor()));

        Color backgroundColor = Color.web(tag.getColor().getBackgroundColor());
        tagContainer.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));

        return tagContainer.getScene();
    }

    /**
     * Handles the delete (X) button being clicked on
     * IF IN EDITTASK OR ADDTAGTOTASK -> removes the tag from the task
     * ELSE -> deletes the tag
     *
     * Uses boardUpdate to delete/remove the tag and refresh.
     * See commons.sync.TagRemovedFromTask.apply() and commons.sync.TagDeleted.apply()
     * for update methods
     *
     * TODO: stop allowing tags to be deleted from addTagToTask search
     */
    public void handleDelete(){
        IdResponseModel resp;
        if(inEditTaskOrAddTagToTask()){
            resp = userData.updateBoard(new TagRemovedFromTask(tag.getBoardId(),tag, taskController.getTask()));
        }else{
            resp = userData.updateBoard(new TagDeleted(tag.getBoardId(), tag));
        }
        if (resp.getId() == -1) {
            showCtrl.showError(resp.getErrorMessage());
            showCtrl.cancel();
        }
    }

    /**
     * Highlights a tag if the mouse is currently inside it
     *
     */
    public void handleHighlight(){
          tagContainer.setOnMouseEntered(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                  tagContainer.setStyle("-fx-border-width: 2px;-fx-border-color: blue");
              }
          });
          tagContainer.setOnMouseExited(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                  tagContainer.setStyle("-fx-border-width: 0");
              }
          });
    }


    /**
     * handles editing a tag, and adding a tag to a task
     * IF IN EDITTASK OR ADDTAGTOTASK AND ONE CLICK -> adds a tag to the task
     * IF DOUBLECLICK -> edit the tag
     *
     * Uses BoardUpdate to add a tag to a task
     * See commons.sync.TagAddedToTask.apply() for update method
     */
    public void handleClick(){
        tagText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY))
                    if (event.getClickCount() == 2) {
                        showCtrl.showEditTag(tag);
                    } else if (event.getClickCount()==1 && inEditTaskOrAddTagToTask()) {

                        if(!validateTagBeforeAdd(tag, taskController.getTask())) return;
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

    /**
     * Validates the tag before adding it to a task
     * Currently just checks if the tag has already been added
     * @param tag tag to be added
     * @param task task to be added to
     * @return boolean representing whether the tag is valid
     */
    private boolean validateTagBeforeAdd(Tag tag, Task task){
        java.util.List<Tag> tags = serverUtils.getTagByTask(task.getId());
        return !tags.contains(tag);
    }

    /**
     * Checks if the tag is in editTask or addTagToTask
     * currently done by checking if the editTaskController has been set
     * which is only done when creating shapes for the above classes.
     *
     * NOTE: The task field on Tags is always null when received since it has a @JsonIgnore
     * annotation, without the EditTaskController (or a task field replacement) we would have
     * limited access to the task entity we want to work with.
     *
     * @return boolean representing whether the tag is in the classes.
     */
    private boolean inEditTaskOrAddTagToTask(){
        return taskController != null;
    }
}
