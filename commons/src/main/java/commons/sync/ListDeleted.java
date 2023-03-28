package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class ListDeleted extends BoardUpdate {

    private int listId;

    public ListDeleted(int boardID, int listId) {
        super(boardID);
        this.listId = listId;
    }

    public ListDeleted() {
        super();
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.deleteList(super.getBoardID(), listId);
    }

    @Override
    public void apply(IUserData data) {
        commons.List list = data.getCurrentBoard().getLists().stream()
                .filter(e -> e.getId() == listId).findFirst().orElse(null);
        data.getCurrentBoard().getLists().remove(list);
        data.getShowCtrl().deleteList(list);
    }

}
