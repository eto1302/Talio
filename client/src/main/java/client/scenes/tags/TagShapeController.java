package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.utils.ServerUtils;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class TagShapeController {

    private ShowCtrl showCtrl;
    private ServerUtils serverUtils;

    @FXML
    private GridPane tagContainer;
    @FXML
    private Label tagText, tagDeleteButton;

    private Tag tag;

    @Inject
    public TagShapeController(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }

    public Scene getSceneUpdated(Tag tag){
        this.tag = tag;
        tagText.setText(tag.getName());
        Color backgroundColor = Color.web(tag.getColor());
        //if the color is dark enough, change text to white
        if(backgroundColor.getBrightness() < 0.5){
            tagText.setStyle("-fx-text-inner-color: white;");;
        }
        tagContainer.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        return tagContainer.getScene();
    }

    public void removeTag(){
        //TODO
    }

}
