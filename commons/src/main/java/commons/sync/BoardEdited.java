package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;

public class BoardEdited extends BoardUpdate {

    private BoardEditModel edit;

    public BoardEdited(int boardId, BoardEditModel edit) {
        super(boardId);
        this.edit = edit;
    }

    public BoardEdited() {
        super();
    }

    public BoardEditModel getEdit() {
        return edit;
    }

    public void setEdit(BoardEditModel edit) {
        this.edit = edit;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.editBoard(super.getBoardID(), edit);
    }

    @Override
    public void apply(IUserData data, IServerUtils serverUtils) {
        data.getCurrentBoard().setName(edit.getName());
        data.getCurrentBoard().setPassword(edit.getPassword());
        data.getShowCtrl().showBoard();
    }

}
