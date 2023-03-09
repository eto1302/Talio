package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CardShape {

    @FXML
    private Label titleCard;
    @FXML
    private GridPane gridCard;

    public GridPane getGridCard() {
        return gridCard;
    }

    public void setText(String title) {
        titleCard.setText(title);
    }
}
