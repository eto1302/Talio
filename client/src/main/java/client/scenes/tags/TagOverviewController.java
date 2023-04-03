package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.List;


public class TagOverviewController {

    private ShowCtrl showCtrl;
    private ServerUtils serverUtils;
    @Inject
    private UserData userData;


    @FXML
    private Button addTagButton;
    @FXML
    private VBox tagBoxRight, tagBoxLeft;
    @FXML
    private GridPane tagOverviewContainer;

    private List<Tag> tags;
    private Board board;
    private int ind;

    @Inject
    public TagOverviewController(ShowCtrl showCtrl, ServerUtils serverUtils) {
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }

    public void refresh(){
        clear();
        this.board = userData.getCurrentBoard();
        this.tags = serverUtils.getTagByBoard(board.getId());
        if(tags == null || tags.isEmpty()){
            return;
        }
        putTags(tags);
    }

    public void showAddTag(){
        showCtrl.showAddTag();
    }

    public void putTag(Scene tagScene, int ind) {
        if (ind % 2 == 0) {
            tagBoxLeft.getChildren().add(tagScene.getRoot());
        } else {
            tagBoxRight.getChildren().add(tagScene.getRoot());
        }
    }


    public void putTags(List<Tag> tags){
        if(tags == null) return;
        for(int i = 0; i < tags.size(); i++){
            Scene ts = showCtrl.getTagScene(tags.get(i));
            putTag(ts, i);
        }
    }

    public void clear(){
        tagBoxLeft.getChildren().remove(0, tagBoxLeft.getChildren().size());
        tagBoxRight.getChildren().remove(0, tagBoxRight.getChildren().size());
    }
}
