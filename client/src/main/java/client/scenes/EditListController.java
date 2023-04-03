package client.scenes;

import client.Services.ListService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;
import commons.models.IdResponseModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class EditListController {
    @FXML
    private TextField newTitle;

    private ShowCtrl showCtrl;
    private List list;
    private ListService listService;

    @Inject
    private EditListController(ShowCtrl showCtrl, ServerUtils server, UserData userData) {
        this.showCtrl = showCtrl;
        this.listService = new ListService(userData, server);
    }

    public void cancel(){
        showCtrl.cancel();
    }


    /**
     * Sets the values of the fields according to our list's information
     * @param list the list to be edited
     */
    public void setup(List list){
        this.list = list;
        newTitle.setText(list.getName());
    }

    /**
     * Gets the values from the fields and edits the list accordingly.
     */
    public void edit(){
        IdResponseModel response = this.listService.editList(list, newTitle.getText());

        if (response.getId() == -1) {
            showCtrl.cancel();
            showCtrl.showError(response.getErrorMessage());
            return;
        }

        showCtrl.cancel();
    }
}
