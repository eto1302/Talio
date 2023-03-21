package client.scenes;

import client.utils.ServerUtils;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.inject.Inject;

public class EditListController {

    @FXML
    private ColorPicker newBackground, newFont;
    @FXML
    private TextField newTitle;
    @FXML
    private Button cancel, edit;

    private ShowCtrl showCtrl;
    private int id;
    private Stage primaryStage;
    private ListShape controller;
    private ServerUtils server;
    @Inject
    private EditListController(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl =showCtrl;
        server=serverUtils;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    /**
     * Sets the values of the fields according to our list's information
     * @param list the list to be edited
     * @param controller the list's controller
     * @param primaryStage of the window we clicked to this scene from.
     */
    public void setup(List list, ListShape controller, Stage primaryStage){
        this.id=list.getId();
        this.controller=controller;
        this.primaryStage = primaryStage;
        newBackground.setValue(Color.web(list.getBackgroundColor()));
        newTitle.setText(list.getName());
        newFont.setValue(Color.web(list.getFontColor()));
    }

    /**
     * Gets the values from the fields and edits the list accordingly.
     */
    public void edit(){
        String backgroundColor = colorToHex(this.newBackground.getValue());
        String fontColor = colorToHex(this.newFont.getValue());
        String name = newTitle.getText();
        server.editList(name, id, backgroundColor, fontColor);
        showCtrl.editList(server.getList(id), controller, primaryStage);
        showCtrl.cancel();
    }

    /**
     * Returns a hexadecimal string representation of javafx.scene.paint.Color.
     * @param color the color to be transformed
     * @return string representation of the color.
     */
    private String colorToHex(Color color){
        String hexString = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        return hexString;
    }
}
