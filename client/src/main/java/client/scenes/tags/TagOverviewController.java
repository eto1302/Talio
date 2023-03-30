package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

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

    @Inject
    public TagOverviewController(ShowCtrl showCtrl, ServerUtils serverUtils) {
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }

    public void setup(Board board){

    }
}
