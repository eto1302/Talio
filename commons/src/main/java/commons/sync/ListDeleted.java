package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class ListDeleted extends BoardUpdate {

    private final int listId;

    public ListDeleted(int boardID, int listId) {
        super(boardID);
        this.listId = listId;
    }

    public int getListId() {
        return listId;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.deleteList(listId, super.getBoardID());
    }

    @Override
    public void apply(IUserData data) {
        data.getCurrentBoard().getLists().removeIf(list -> list.getId() == listId);
    }

}
