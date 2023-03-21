package client.sync;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;

public class ListEdited extends BoardUpdate {

    private final int listId;
    private final ListEditModel edit;

    public ListEdited(int boardID, int listId, ListEditModel edit) {
        super(boardID);
        this.listId = listId;
        this.edit = edit;
    }

    public int getListId() {
        return listId;
    }

    public ListEditModel getEdit() {
        return edit;
    }

    @Override
    public IdResponseModel sendToServer(ServerUtils server) {
        return server.editList(super.getBoardID(), listId, edit);
    }

    @Override
    public void apply(UserData data) {
        data.getCurrentBoard().getLists().stream().filter(e -> e.getId() == listId)
                .forEach(e -> {
                    e.setName(edit.getName());
                    e.setBackgroundColor(edit.getBackgroundColor());
                    e.setFontColor(edit.getFontColor());
                });
    }

}
