package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ErrorController {

    private final ShowCtrl showCtrl;

    @FXML
    private Text errorMessage;

    @Inject
    public ErrorController(ShowCtrl showCtrl) {
        this.showCtrl = showCtrl;
    }

    public void setErrorMessage(String message) {
        errorMessage.setText(message);
    }

    public void cancel() {
        showCtrl.closePopUp();
    }

}
