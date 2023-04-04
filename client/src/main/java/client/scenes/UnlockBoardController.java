package client.scenes;

import client.user.UserData;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;

import javax.inject.Inject;

public class UnlockBoardController {

    private final ShowCtrl showCtrl;

    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox remember;

    private UserData userData;

    @Inject
    public UnlockBoardController(ShowCtrl showCtrl, UserData userData) {
        this.showCtrl = showCtrl;
        this.userData = userData;
    }

    public void setCurrentPassword(String password) {
        this.passwordField.setText(password);
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void confirm() {
        String password = passwordField.getText();
        if(password.length() == 0) {
            showCtrl.showError("You must enter a password");
            return;
        }
        if(!password.equals(userData.getCurrentBoard().getPassword())) {
            showCtrl.showError("Incorrect password entered");
            return;
        }

        if(remember.isSelected()) {
            userData.getBoards().put(userData.getCurrentBoard().getId(), password);
            userData.saveToDisk();
        }
        userData.setCurrentBoardLocked(false);
        cancel();
    }

}
