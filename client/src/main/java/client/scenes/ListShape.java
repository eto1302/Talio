package client.scenes;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class ListShape {

    @FXML
    private VBox tasksBox;
    @FXML
    private HBox tagBox;
    @FXML
    private MenuItem editList, deleteList, addTask, addTag;
    @FXML
    private Label listTitle;
    @FXML
    private GridPane listGrid;
    private ShowCtrl showCtrl;


    @Inject
    public ListShape (ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }

    /**
     * Updates the list's visual (sets the title and the colors of it)
     * based on the list object that is passed on
     * @param list the list with the necessary attributes
     * @return the updated scene after modifications
     */
    public Scene getSceneUpdated(commons.List list){
        listTitle.setText(list.getName());
        String rgbBackground = list.getBackgroundColor();
        String rgbFont = list.getFontColor();
        Color backgroundColor = Color.web(rgbBackground);
        Color fontColor = Color.web(rgbFont);

        listGrid.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        listTitle.setTextFill(fontColor);
        return listGrid.getScene();
    }

    public void deleteList(){
        //should have confirmation + deletion from database
        HBox parent = (HBox) listGrid.getParent();
        parent.getChildren().remove(listGrid);

    }
}
