package client.sync;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.messaging.Messages.Message;
import commons.models.IdResponseModel;

public abstract class BoardUpdate implements Message {

    public static final String QUEUE = "board";

    private static UserData userData;

    public static void setUserData(UserData userData) {
        BoardUpdate.userData = userData;
    }

    private final int boardID;

    protected BoardUpdate(int boardID) {
        this.boardID = boardID;
    }

    public int getBoardID() {
        return boardID;
    }

    public String getSendQueue() {
        return "/app/" + QUEUE + boardID;
    }

    @Override
    public void consume() {
        if(userData != null && userData.getCurrentBoard() != null
                && userData.getCurrentBoard().getId() == boardID)
            apply(userData);
    }

    public abstract IdResponseModel sendToServer(ServerUtils server);

    public abstract void apply(UserData data);

}
