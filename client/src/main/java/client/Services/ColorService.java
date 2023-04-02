package client.Services;

import client.user.UserData;
import commons.Board;
import commons.models.ColorEditModel;
import commons.models.IdResponseModel;
import commons.sync.ColorAdded;
import commons.sync.ColorDeleted;
import commons.sync.ColorEdited;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public IdResponseModel editColor(int id, Color colorFont, Color colorBackground,
                                     boolean isDefault) {
        if(isDefault){
            return setDefault(id, colorFont, colorBackground);
        }
        Board board = this.userData.getCurrentBoard();
        if(id == -1) id = board.getBoardColor().getId();
        if(id == -2) id = board.getListColor().getId();
        commons.Color color = commons.Color.create(
                colorToHex(colorFont), colorToHex(colorBackground));
        ColorEditModel model = new ColorEditModel(
                color.getBackgroundColor(), color.getFontColor(), isDefault);

        IdResponseModel idResponseModel = userData.updateBoard(new ColorEdited(
                board.getId(), id, model));
        this.userData.openBoard(board.getId());
        return idResponseModel;
    }

    private IdResponseModel setDefault(int id, Color colorFont, Color colorBackground) {
        Optional<commons.Color> currentDefault = this.userData.getCurrentBoard().getColors()
                .stream().filter(commons.Color::getIsDefault).findFirst();

        if(currentDefault.isEmpty()){
            commons.Color toBeDefault = this.userData.getCurrentBoard().getColors().get(2);
            ColorEditModel edit = new ColorEditModel(colorToHex(colorBackground),
                    colorToHex(colorFont), true);
            userData.updateBoard(new ColorEdited(this.userData.getCurrentBoard().getId(),
                    id, edit));
        }

        ColorEditModel editCurrentDefault = new ColorEditModel(
                currentDefault.get().getBackgroundColor(),
                currentDefault.get().getFontColor(), false);
        IdResponseModel currentDefaultModel = userData.updateBoard(new ColorEdited(
                this.userData.getCurrentBoard().getId(),
                currentDefault.get().getId(), editCurrentDefault));
        if (currentDefaultModel.getId() == -1) {
            return currentDefaultModel;
        }

        ColorEditModel edit = new ColorEditModel(
                colorToHex(colorBackground),colorToHex(colorFont), true);
        IdResponseModel model = userData.updateBoard(new ColorEdited(
                this.userData.getCurrentBoard().getId(), id, edit));
        userData.openBoard(this.userData.getCurrentBoard().getId());
        return model;
    }

    public IdResponseModel deleteColor(commons.Color color) {
        if(color.getIsDefault()){
            return new IdResponseModel(-1, "Cannot delete default color");
        }
        IdResponseModel model = this.userData.deleteColor(
                new ColorDeleted(this.userData.getCurrentBoard().getId(), color.getId()));
        this.userData.openBoard(this.userData.getCurrentBoard().getId());
        return model;
    }
}
