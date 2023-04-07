package client.scenes.tags;

import client.Services.TagService;
import client.scenes.ShowCtrl;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Tag;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class EditTagController {

    private final ShowCtrl showCtrl;
    @FXML
    private TextField name;
    @FXML
    private ColorPicker background;
    @FXML
    private ColorPicker font;
    private Tag tag;
    private TagService tagService;

    @Inject
    public EditTagController (ShowCtrl showCtrl, UserData userData, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        this.tagService = new TagService(userData, serverUtils);
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
        this.name.setText(tag.getName());
        this.background.setValue(
                javafx.scene.paint.Color.web(tag.getColor().getBackgroundColor()));
        this.font.setValue(
                javafx.scene.paint.Color.web(tag.getColor().getFontColor()));
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
        IdResponseModel response = this.tagService.editTag(
                name.getText(), background.getValue(), font.getValue(), tag);
        if (response.getId() == -1) {
            showCtrl.showError(response.getErrorMessage());
        }
        showCtrl.closePopUp();
    }
}
