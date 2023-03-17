package client.scenes;

import client.utils.ServerUtils;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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

    public void setup(List list, ListShape controller){
        this.id=list.getId();
        this.controller=controller;
        newBackground.setValue(Color.web(list.getBackgroundColor()));
        newTitle.setText(list.getName());
        newFont.setValue(Color.web(list.getFontColor()));
    }

    public void edit(){
        String backgroundColor = colorToHex(this.newBackground.getValue());
        String fontColor = colorToHex(this.newFont.getValue());
        String name = newTitle.getText();
        server.editList(name, id, backgroundColor, fontColor);
        showCtrl.editList(server.getList(id), controller);
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
