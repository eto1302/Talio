package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

public class HelpCtrl {
    @FXML
    private AnchorPane pane;
    private final ShowCtrl showCtrl;

    @Inject
    public HelpCtrl(ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }

}
