package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.ColorEditModel;
import commons.models.IdResponseModel;

public class ColorEdited extends BoardUpdate {

    private int colorId;
    private ColorEditModel edit;

    public ColorEdited(int boardID, int colorId, ColorEditModel edit) {
        super(boardID);
        this.colorId = colorId;
        this.edit = edit;
    }

    public ColorEdited() {
        super();
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public ColorEditModel getEdit() {
        return edit;
    }

    public void setEdit(ColorEditModel edit) {
        this.edit = edit;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.editColor(colorId, edit);
    }

    @Override
    public void apply(IUserData data) {
        commons.Color color = data.getCurrentBoard().getTaskColors().stream()
                .filter(e -> e.getId() == colorId).findFirst().orElse(null);
        color.setFontColor(edit.getFontColor());
        color.setBackgroundColor(edit.getBackgroundColor());
        color.setDefault(edit.isDefault());
        //data.getShowCtrl().editColor(color);
    }

}