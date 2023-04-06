package client.scenes.tags;

import client.Services.TagService;
import client.scenes.*;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Task;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import javax.inject.Inject;

/**
 * AddTagController
 * Controller that handles creating tags
 *
 */

public class AddTagController {

    private final ShowCtrl showCtrl;
    @FXML
    private TextField name;
    @FXML
    private ColorPicker background;
    @FXML
    private ColorPicker font;
    private commons.Task task;
    private TagService tagService;

    @Inject
    public AddTagController(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.showCtrl=showCtrl;
        this.tagService = new TagService(userData, serverUtils);
    }

    public void setup(Task task) {
        this.task = task;
    }

    /**
     * addTag
     * Creates tag, adds it to the current board, and then updates the board.
     * See commons.sync.TagCreated.apply() for update method
     */
    public void addTag() {
        IdResponseModel response = tagService.addTag(name.getText(), background.getValue(),
                font.getValue());


        if (response.getId() == -1) {
            showCtrl.showError(response.getErrorMessage());
        }
        cancel();
    }

    public void cancel(){
        showCtrl.closePopUp();
    }
}
