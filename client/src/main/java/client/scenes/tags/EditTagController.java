package client.scenes.tags;

import client.user.UserData;
import commons.Color;
import commons.Tag;
import commons.models.IdResponseModel;
import commons.models.TagEditModel;
import commons.sync.TagEdited;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class EditTagController {

    private final ShowCtrl showCtrl;
    private UserData userData;

    @FXML
    private Button cancelButton;
    @FXML
    private Button editButton;
    @FXML
    private TextField textField;
    @FXML
    private ColorPicker colorPicker;

    private Tag tag;

    @Inject
    public EditTagController (ShowCtrl showCtrl, UserData userData){
        this.showCtrl=showCtrl;
        this.userData = userData;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    /**
     * Edits a tag
     * Creates a editModel using appropriate fields, and sends a boardupdate to update server
     * and other clients
     * see commons.sync.TagEdited.apply() for update method
     */
    public void editTag(){
        TagEditModel model = new TagEditModel(textField.getText(), colorPickerToColor());
        IdResponseModel resp = userData.updateBoard(new TagEdited(tag.getBoardId(), tag, model));
        if (resp.getId() == -1) {
            showCtrl.showError(resp.getErrorMessage());
            showCtrl.cancel();
        }
        showCtrl.closePopUp();
    }

    /**
     * Converts javafx Color to a Color
     * @return
     */
    private Color colorPickerToColor(){
        String bgColor = colorToHex(colorPicker.getValue());
        String textColor = (getBrightness(colorPicker.getValue()) < 130) ? "#FFFFFF" : "#000000";
        Color res = new Color();
        res.setBackgroundColor(bgColor);
        res.setFontColor(textColor);
        return res;
    }

    /**
     * converts javaFX color to a hex representation
     * @param color Color to be converted
     * @return string with hex representation
     */
    private String colorToHex(javafx.scene.paint.Color color){
        String hexString = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        return hexString;
    }

    /**
     * Gets the perceived brightness of a javaFx Color
     * @param color Color to be converted
     * @return double [0,255] representing the perceived brightness of the color
     */
    private double getBrightness(javafx.scene.paint.Color color) {
        return Math.sqrt(Math.pow(0.299*color.getRed()*255,2)
                + Math.pow(0.587*color.getGreen()*255, 2)
                + Math.pow(0.114* color.getBlue()*255, 2)
        );
    }
}
