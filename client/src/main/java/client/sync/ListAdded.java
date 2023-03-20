package client.sync;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;

public class ListAdded extends BoardUpdate {

    private final List list;

    public ListAdded(int boardID, List list) {
        super(boardID);
        assert list != null;
        this.list = list;
    }

    public List getList() {
        return list;
    }

    @Override
    public boolean sendToServer(ServerUtils server) {
        int id = server.addlist(list, super.getBoardID());
        if(id == -1)
            return false;
        list.setId(id);
        return true;
    }

    @Override
    public void apply(UserData data) {
        data.getCurrentBoard().getLists().add(list);
    }

}
