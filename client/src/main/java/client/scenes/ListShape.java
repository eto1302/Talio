package client.scenes;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
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
        String[] rgbBackground = list.getBackgroundColor().split("\\+");
        String[] rgbFont = list.getFontColor().split("\\+");
        Color backgroundColor= Color.color(Double.parseDouble(rgbBackground[0]),
                Double.parseDouble(rgbBackground[1]), Double.parseDouble(rgbBackground[2]));
        Color fontColor= Color.color(Double.parseDouble(rgbFont[0]),
                Double.parseDouble(rgbFont[1]), Double.parseDouble(rgbFont[2]));

        listGrid.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        listTitle.setTextFill(fontColor);
        return listGrid.getScene();
    }
}
