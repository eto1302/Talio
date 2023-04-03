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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;


public class AddTagToTaskController {
    private ShowCtrl showCtrl;
    private ServerUtils serverUtils;
    @Inject
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

    @Inject
    public AddTagToTaskController(ShowCtrl showCtrl, ServerUtils serverUtils) {
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }

    public void setController(EditTaskController controller){
        this.controller = controller;
    }

    public void refresh(){
        this.board = userData.getCurrentBoard();
        this.tags = serverUtils.getTagByBoard(board.getId());
        updateScene();
    }

    public void handleKey(){
        //TODO: text inside tags does not render after refreshing list for some reason, fix!
        String searchText = searchBox.getText();
        tags = board.getTags().stream()
                .filter(t -> t.getName().contains(searchText))
                .collect(Collectors.toList());
        updateScene();
    }

    public void updateScene(){
        filterUsedTags();
        putTags(tags);
    }

    public void putTag(Scene tagScene){
        Node root = tagScene.getRoot();
        tagBox.getChildren().add(root);
    }

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

    public void cleanBox(){
        tagBox.getChildren().remove(0, tagBox.getChildren().size());
    }

    public void filterUsedTags(){
        List<Tag> used = serverUtils.getTagByTask(controller.getTask().getId());
        tags.removeAll(used);
    }

    public void cancel() {showCtrl.closePopUp();}
}
