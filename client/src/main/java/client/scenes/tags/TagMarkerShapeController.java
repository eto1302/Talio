package client.scenes.tags;

import client.scenes.ShowCtrl;
import client.scenes.tasks.TaskShape;
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

    /**
     * sets and returns a tagMarker
     * a tagMarker is a circle representing a tag on a task
     * @param tag tag to be represented as a marker
     * @return scene containing the marker
     */
    public Scene getSceneUpdated(Tag tag){
        Color markerColor = Color.web(tag.getColor().getBackgroundColor());
        markerCircle.setFill(markerColor);
        return markerCircle.getScene();
    }
}
