package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import javax.inject.Inject;

public class LockBoardController {

    private final ShowCtrl showCtrl;

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @Inject
    public LockBoardController(ShowCtrl showCtrl) {
        this.showCtrl=showCtrl;
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void confirm() {
        cancel();
    }

    public void lockBoard() {

    }

    public void removePassword() {

    }

}
