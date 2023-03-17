package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class TaskOverview {

    @FXML
    private Text titleText;
    @FXML
    private TextField descriptionField;
    @FXML
    private Button okButton, addSubTaskButton;
    @FXML
    private VBox subtaskBox, buttonsBox;
    private final ShowCtrl showCtrl;
    private ServerUtils serverUtils;
    private int id;

    @Inject
    public TaskOverview(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        this.serverUtils=serverUtils;
    }

}
