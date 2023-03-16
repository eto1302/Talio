package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;

public class BoardShape {

    private int id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @FXML
    private Label nameLabel;
    @FXML
    private Button selectButton;
    private ShowCtrl showCtrl;
    @Inject
    public BoardShape (ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }
    public void selectBoard(){

    }
}
