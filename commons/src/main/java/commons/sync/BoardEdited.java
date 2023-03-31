package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;

public class BoardEdited extends BoardUpdate {

    private final int boardId;
    private final BoardEditModel edit;

    public BoardEdited(int boardId, BoardEditModel edit) {
        this.boardId = boardId;
        this.edit = edit;
    }

    public int getBoardId() {
        return boardId;
    }

    public BoardEditModel getEdit() {
        return edit;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.editBoard(boardId, edit);
    }

    @Override
    public void apply(IUserData data) {
        data.getCurrentBoard().setName(edit.getName());
    }

}
