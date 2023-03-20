package client.scenes;


import client.utils.ServerUtils;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class ListShapeCtrl {

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
    private ServerUtils serverUtils;
    private int id;


    @Inject
    public ListShapeCtrl(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        this.serverUtils=serverUtils;
    }

    /**
     * Updates the list's visual (sets the title and the colors of it)
     * based on the list object that is passed on
     * @param list the list with the necessary attributes
     * @return the updated scene after modifications
     */
    public Scene getSceneUpdated(commons.List list){
        listTitle.setText(list.getName());
        Color backgroundColor= Color.web(list.getBackgroundColor());
        Color fontColor= Color.web(list.getFontColor());

        listGrid.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        listTitle.setTextFill(fontColor);
        return listGrid.getScene();
    }

    public void deleteList(){
        List list = serverUtils.getList(id);
        serverUtils.deleteList(list.getBoardId(), id);
    }

    public void editList(){
        List list = serverUtils.getList(id);
        if(list == null) showCtrl.showError("Failed to get the list...");
        showCtrl.showEditList(list, this);
    }

    public void setId(int id){
        this.id=id;
    }
}
