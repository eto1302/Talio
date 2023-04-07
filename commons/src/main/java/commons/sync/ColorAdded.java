package commons.sync;

import commons.Color;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class ColorAdded extends BoardUpdate {
    private Color color;

    public ColorAdded(int boardID, Color color) {
        super(boardID);
        this.color = color;
    }

    public ColorAdded() {
        super();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.addColor(color);
        color.setId(id.getId());
        server.setColorToBoard(color,  super.getBoardID());
        return id;
    }

    @Override
    public void apply(IUserData data, IServerUtils serverUtils) {
        if(color.getBoardId() == data.getCurrentBoard().getId()) {
            color.setBoard(data.getCurrentBoard());
            if(data.getCurrentBoard().getColors() != null)
                data.getCurrentBoard().getColors().add(color);
            if(data.getShowCtrl().isColorPickerOpen()) {
                data.getShowCtrl().cancel();
                data.getShowCtrl().showColorPicker();
            }
        }
    }

}
