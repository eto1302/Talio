package client.scenes;

import client.Services.ListService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.inject.Inject;


public class AddListController {

    @FXML
    private TextField nameField;
    private final ShowCtrl showCtrl;
    private ListService listService;

    @Inject
    public AddListController(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.showCtrl = showCtrl;
        this.listService = new ListService(userData,serverUtils);
    }

    public void cancel(){
        reset();
        showCtrl.cancel();
    }

    /**
     * Creates a list based on user input
     */
    public void addList() {
        IdResponseModel response = this.listService.addList(nameField.getText());
        if (response.getId() == -1) {
            showCtrl.showError(response.getErrorMessage());
            showCtrl.cancel();
            return;
        }
        cancel();
    }

    public void reset() {
        nameField.clear();
    }
}
