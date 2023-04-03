package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.scenes.tasks.EditTaskController;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;


public class AddTagToTaskController {
    private ShowCtrl showCtrl;
    private ServerUtils serverUtils;

    private UserData userData;

    @FXML
    private GridPane tag2taskContainer;
    @FXML
    private TextField searchBox;

    @FXML
    private VBox tagBox;

    private Board board;
    private List<Tag> tags;

    private EditTaskController controller;

    private String prevSearch = "";

    @Inject
    public AddTagToTaskController(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
        this.userData = userData;
    }

    public void setController(EditTaskController controller){
        this.controller = controller;
    }

    /**
     * Refreshes the board and tags associated with the controller
     * and updates the scene with the fresh information
     */
    public void refresh(){
        this.board = userData.getCurrentBoard();
        this.tags = serverUtils.getTagByBoard(board.getId());
        updateScene();
    }

    /**
     * Handles a keypress, filters out the currently shown tags based
     * on the current searchText, and updates the scene
     * @param e
     */
    public void handleKey(KeyEvent e){
        //TODO: text inside tags does not render after refreshing list for some reason, fix!
        String searchText = searchBox.getText();
        if(searchText.equals(prevSearch)) return;
        prevSearch = searchText;
        tags = board.getTags().stream()
                .filter(t -> t.getName().contains(searchText))
                .collect(Collectors.toList());
        updateScene();
    }

    /**
     * Filters out tags currently assigned to the task
     * and then puts all the tags into the scene
     */
    public void updateScene(){
        filterUsedTags();
        putTags(tags);
    }

    /**
     * puts a scene into the tagBox
     * used by putTags
     * @param tagScene TagShape scene to be inserted
     */
    public void putTag(Scene tagScene){
        Node root = tagScene.getRoot();
        tagBox.getChildren().add(root);
    }

    /**
     * puts a list of tags into the tagbox
     * @param tags tags to be inserted
     */
    public void putTags(List<Tag> tags){
        cleanBox();
        if(tags == null || tags.isEmpty()){
            tagBox.getChildren().add(new Label("No tags!"));
            return;
        }
        int max = (tags.size() < 5) ? tags.size():5;
        for(int i = 0; i < max; i++){
            Scene ts = showCtrl.getTagSceneTask(tags.get(i), controller);
            putTag(ts);
        }
    }

    /**
     * clears the tagBox
     */
    public void cleanBox(){
        tagBox.getChildren().clear();
    }

    /**
     * filters out tags already associated to a task from this.tags
     */
    public void filterUsedTags(){
        List<Tag> used = serverUtils.getTagByTask(controller.getTask().getId());
        tags.removeAll(used);
    }

    public void cancel() {showCtrl.closePopUp();}
}
