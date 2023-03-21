package client.sync;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;
import commons.models.IdResponseModel;

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
    public IdResponseModel sendToServer(ServerUtils server) {
        IdResponseModel id = server.addList(list, super.getBoardID());
        list.setId(id.getId());
        return id;
    }

    @Override
    public void apply(UserData data) {
        data.getCurrentBoard().getLists().add(list);
    }

}
