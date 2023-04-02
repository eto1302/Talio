package client.Services;

import client.user.UserData;
import commons.Board;
import commons.models.ColorEditModel;
import commons.models.IdResponseModel;
import commons.sync.ColorAdded;
import commons.sync.ColorEdited;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Service;

@Service
public class ColorService {

    private final UserData userData;

    public ColorService(UserData userData) {
        this.userData = userData;
    }

    public IdResponseModel addTaskColor(Color backgroundColor, Color fontColor) {
        commons.Color color = commons.Color.create(colorToHex(backgroundColor),
                colorToHex(fontColor));
        if(this.userData.getCurrentBoard().getColors().size() == 2) color.setIsDefault(true);
        IdResponseModel model = userData.updateBoard(new ColorAdded(
                this.userData.getCurrentBoard().getId(), color));
        userData.openBoard(this.userData.getCurrentBoard().getId());
        return model;
    }

    /**
     * Returns a hexadecimal string representation of javafx.scene.paint.Color.
     * @param color the color to be transformed
     * @return string representation of the color.
     */
    private String colorToHex(javafx.scene.paint.Color color){
        String hexString = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        return hexString;
    }

    public IdResponseModel editColor(Color colorFont, Color colorBackground) {
        Board board = this.userData.getCurrentBoard();
        commons.Color color = commons.Color.create(
                colorToHex(colorFont), colorToHex(colorBackground));
        ColorEditModel model = new ColorEditModel(
                color.getBackgroundColor(), color.getFontColor(), false);

        IdResponseModel idResponseModel = userData.updateBoard(new ColorEdited(
                board.getId(), board.getBoardColor().getId(), model));
        this.userData.openBoard(board.getId());
        return idResponseModel;
    }
}
