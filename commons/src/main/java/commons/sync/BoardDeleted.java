package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class BoardDeleted extends BoardUpdate {

    public BoardDeleted(int boardID) {
        super(boardID);
    }

    public BoardDeleted() {
        super();
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.deleteBoard(super.getBoardID());
    }

    @Override
    public void consume() {
        IUserData userData = super.getUserData();
        if(userData != null) apply(userData);
    }

    @Override
    public void apply(IUserData data) {
        data.getShowCtrl().refreshAdminBoards();
    }


    @Override
    public String getSendQueue() {
        //TODO: For now, only admins are notified of board deletions,
        // we should send the message to the board queue as well later.
        return "/app/admin";
    }
}
