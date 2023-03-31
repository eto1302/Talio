package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;

public class ListEdited extends BoardUpdate {

    private int listId;
    private ListEditModel edit;

    public ListEdited(int boardID, int listId, ListEditModel edit) {
        super(boardID);
        this.listId = listId;
        this.edit = edit;
    }

    public ListEdited() {
        super();
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public ListEditModel getEdit() {
        return edit;
    }

    public void setEdit(ListEditModel edit) {
        this.edit = edit;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.editList(super.getBoardID(), listId, edit);
    }

    @Override
    public void apply(IUserData data) {
        commons.List list = data.getCurrentBoard().getLists().stream()
                .filter(e -> e.getId() == listId).findFirst().orElse(null);
        list.setName(edit.getName());
        data.getShowCtrl().editList(list);
    }

}
