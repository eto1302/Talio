package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.List;


public class TagOverviewController {
    private ShowCtrl showCtrl;
    @Inject
    private final ServerUtils serverUtils;
    @Inject
    private final UserData userData;

    @FXML
    private Button addTagButton;
    @FXML
    private VBox tagBoxRight, tagBoxLeft;
    @FXML
    private GridPane tagOverviewContainer;

    private List<Tag> tags;
    private Board board;

    private int ind = 0;

    @Inject
    public TagOverviewController(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
        this.userData = userData;
    }

    /**
     * refreshes the current board and list of tags
     * clears current tags being shown and updates the overview
     */
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
        if(userData.isCurrentBoardLocked()){
            userData.showError();
            return;
        }
        showCtrl.showAddTag();
    }

    public void putTag(Node parent) {
        if (ind % 2 == 0) {
            tagBoxLeft.getChildren().add(parent);
        } else {
            tagBoxRight.getChildren().add(parent);
        }
        ind++;
    }
    public void putTags(List<Tag> tags){
        if(tags == null) return;
        for(int i = 0; i < tags.size(); i++){
            showCtrl.putTagSceneOverview(tags.get(i), this);
        }
    }

    public void clear(){
        tagBoxLeft.getChildren().clear();
        tagBoxRight.getChildren().clear();
        ind = 0;
    }

    public int getInd() {
        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }
}
