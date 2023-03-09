package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class SearchCtrl {
    private ShowCtrl showCtrl;

    @FXML
    private Button searchButton;

    @Inject
    public SearchCtrl(ShowCtrl showCtrl){
        this.showCtrl = showCtrl;
    }
}
