package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class TaskOverview {

    @FXML
    private Text titleText, descriptionText;
    @FXML
    private Button descriptionButton, addSubTaskButton;
    @FXML
    private VBox subtaskBox, buttonsBox;
    private final ShowCtrl showCtrl;
    private ServerUtils serverUtils;

    @Inject
    public TaskOverview(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        this.serverUtils=serverUtils;
    }

    public void changeButtonText(){
        if (!descriptionText.getText().equals("No description yet"))
            descriptionButton.setText("Edit description");
    }
}
