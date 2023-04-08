package commons.sync;

import commons.List;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

import java.util.ArrayList;

public class ListAdded extends BoardUpdate {

    private List list;

    public ListAdded(int boardID, List list) {
        super(boardID);
        this.list = list;
    }

    public ListAdded() {
        super();
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.addList(list, super.getBoardID());
        list.setId(id.getId());
        return id;
    }

    @Override
    public void apply(IUserData data, IServerUtils serverUtils) {
        list.setTasks(new ArrayList<>());
        data.getCurrentBoard().getLists().add(list);
        data.getShowCtrl().addList(list);
    }

}
