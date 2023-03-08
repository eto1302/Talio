package client.scenes;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javax.inject.Inject;
import java.awt.*;

public class Task {
    @FXML
    private GridPane grid;
    @FXML
    private Label title;
    @FXML
    private Label plusSign;
    @FXML
    private VBox box;

    private ShowCtrl showCtrl;
    @Inject
    public Task(ShowCtrl showCtrl){
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
