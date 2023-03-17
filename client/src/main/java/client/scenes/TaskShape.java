package client.scenes;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javax.inject.Inject;
import javafx.scene.control.*;

public class TaskShape {
    @FXML
    private GridPane grid;
    @FXML
    private Label title;
    @FXML
    private Label plusSign;
    private ShowCtrl showCtrl;
    @Inject
    public TaskShape(ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }

    public void doubleClick (){
        grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY))
                    if (event.getClickCount()==2)
                        showCtrl.showTaskOverview();
            }
        });
    }
}
