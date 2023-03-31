package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class ColorDeleted extends BoardUpdate{
    private int colorId;

    public ColorDeleted(int boardID, int colorId) {
        super(boardID);
        this.colorId = colorId;
    }

    public ColorDeleted() {
        super();
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.deleteColor(super.getBoardID(), colorId);
    }

    @Override
    public void apply(IUserData data) {
        commons.Color color = data.getCurrentBoard().getTaskColors().stream()
                .filter(e -> e.getId() == colorId).findFirst().orElse(null);
        data.getCurrentBoard().getTaskColors().remove(color);
        data.getShowCtrl().deleteTaskColor(color);
    }
}
