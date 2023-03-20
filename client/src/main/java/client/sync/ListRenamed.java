package client.sync;

import client.user.UserData;
import client.utils.ServerUtils;

public class ListRenamed extends BoardUpdate {

    private final int listId;
    private final String name;

    public ListRenamed(int boardID, int listId, String name) {
        super(boardID);
        this.listId = listId;
        this.name = name;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean sendToServer(ServerUtils server) {
        return server.renameList(name, listId);
    }

    @Override
    public void apply(UserData data) {
        data.getCurrentBoard().getLists().stream().filter(e -> e.getId() == super.getBoardID())
                .forEach(e -> e.setName(name));
    }

}
