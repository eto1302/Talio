package client.scenes.tags;

import client.scenes.ShowCtrl;
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

    @Inject
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
    public EditTagController (ShowCtrl showCtrl){
        this.showCtrl=showCtrl;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void editTag(){
        TagEditModel model = new TagEditModel(textField.getText(), colorPickerToColor());
        IdResponseModel resp = userData.updateBoard(new TagEdited(tag.getBoardId(), tag, model));
        showCtrl.closePopUp();
    }

    private Color colorPickerToColor(){
        String bgColor = colorToHex(colorPicker.getValue());
        String textColor = (colorPicker.getValue().getBrightness() < 0.7) ? "#FFFFFF" : "#000000";
        Color res = new Color();
        res.setBackgroundColor(bgColor);
        res.setFontColor(textColor);
        return res;
    }

    private String colorToHex(javafx.scene.paint.Color color){
        String hexString = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        return hexString;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
