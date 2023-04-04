package client.scenes;

import client.user.UserData;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;
import commons.sync.BoardEdited;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import javax.inject.Inject;

public class LockBoardController {

    private final ShowCtrl showCtrl;

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private UserData userData;

    @Inject
    public LockBoardController(ShowCtrl showCtrl, UserData userData) {
        this.showCtrl = showCtrl;
        this.userData = userData;
    }

    public void reset() {
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    public void cancel(){
        showCtrl.cancel();
    }

    public void confirm() {
        if(!passwordField.getText().equals(confirmPasswordField.getText())) {
            showCtrl.showError("Passwords do not match");
            return;
        }
        if(passwordField.getText().length() == 0) {
            showCtrl.showError("You must enter a password");
            return;
        }
        if(passwordField.getText().contains("\\")) {
            showCtrl.showError("Password may not contain a '\\'");
            return;
        }

        BoardEditModel model = new BoardEditModel(userData
                .getCurrentBoard().getName(), passwordField.getText());
        IdResponseModel id = userData.updateBoard(new BoardEdited(
                userData.getCurrentBoard().getId(), model));

        if(id.getId() == -1) {
            showCtrl.showError(id.getErrorMessage());
            return;
        }
        cancel();
    }

    public void lockBoard() {
        if(userData.getCurrentBoard().getPassword() == null ||
                userData.getCurrentBoard().getPassword().length() == 0) {
            showCtrl.showError("Cannot lock a board without a password");
            return;
        }

        userData.setCurrentBoardLocked(true);
        cancel();
    }

    public void removePassword() {
        if(userData.getCurrentBoard().getPassword() == null ||
                userData.getCurrentBoard().getPassword().length() == 0) {
            showCtrl.showError("Board already has no password");
            return;
        }

        BoardEditModel model = new BoardEditModel(userData
                .getCurrentBoard().getName(), "");
        IdResponseModel id = userData.updateBoard(new BoardEdited(
                userData.getCurrentBoard().getId(), model));

        if(id.getId() == -1) {
            showCtrl.showError(id.getErrorMessage());
            return;
        }
        cancel();
    }

}
