package client.sync;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;

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
    public boolean sendToServer(ServerUtils server) {
        return server.deleteList(listId, super.getBoardID());
    }

    @Override
    public void apply(UserData data) {
        data.getCurrentBoard().getLists().removeIf(list -> list.getId() == listId);
    }

}
