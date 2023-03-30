package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.utils.ServerUtils;
import commons.Tag;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.inject.Inject;

public class TagMarkerShapeController {

    @Inject
    private ShowCtrl showCtrl;
    @Inject
    private ServerUtils serverUtils;

    @FXML
    private GridPane tagMarkerContainer;
    @FXML
    private Circle markerCircle;

    public TagMarkerShapeController() {

    }

    //    @Inject
//    public TagMarkerShapeController(ShowCtrl showCtrl, ServerUtils serverUtils) {
//        this.showCtrl = showCtrl;
//        this.serverUtils = serverUtils;
//    }

    public Scene getSceneUpdated(Tag tag){
        Color markerColor = Color.web(tag.getColor());
        markerCircle.setFill(markerColor);
        //TODO: Make background of marker transparent

        return tagMarkerContainer.getScene();
    }

    public void removeTagMarker(){
        HBox parent = (HBox) tagMarkerContainer.getParent();
        parent.getChildren().remove(tagMarkerContainer);
    }
}
