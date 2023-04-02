package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import javax.inject.Inject;

public class UnlockBoardController {

    private final ShowCtrl showCtrl;

    @FXML
    private PasswordField passwordField;

    @Inject
    public UnlockBoardController(ShowCtrl showCtrl) {
        this.showCtrl=showCtrl;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void confirm() {
        cancel();
    }

}
