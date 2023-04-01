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
import org.apache.catalina.User;

import javax.inject.Inject;
import java.util.List;


public class TagOverviewController {

    private ShowCtrl showCtrl;
    private ServerUtils serverUtils;


    @FXML
    private Button addTagButton;
    @FXML
    private VBox tagBoxRight, tagBoxLeft;
    @FXML
    private GridPane tagOverviewContainer;

    List<Tag> tags;
    Board board;
    private int ind;

    @Inject
    public TagOverviewController(ShowCtrl showCtrl, ServerUtils serverUtils) {
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }

    public void setup(Board board){
        clear();
        this.board = board;
        tags = board.getTags();
        if(tags == null || tags.isEmpty()){
            return;
        }
        for (Tag t: tags) {

            Scene scene = showCtrl.getTagScene(t);
            if(ind%2 == 0){
                tagBoxLeft.getChildren().add(scene.getRoot());
            }else {
                tagBoxRight.getChildren().add(scene.getRoot());
            }
            ind++;
        }
    }

    public void showAddTag(){
        showCtrl.showAddTag();
    }

    public void putTag(Scene tagScene){
        if(ind%2 == 0){
            tagBoxLeft.getChildren().add(tagScene.getRoot());
        }else {
            tagBoxRight.getChildren().add(tagScene.getRoot());
        }
        ind++;
    }

    public void clear(){
        tagBoxLeft.getChildren().remove(0, tagBoxLeft.getChildren().size());
        tagBoxRight.getChildren().remove(0, tagBoxRight.getChildren().size());
    }
}
