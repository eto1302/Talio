package commons.sync;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import commons.messaging.Messages.Message;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = ListAdded.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ListAdded.class, name = "listAdded"),
    @JsonSubTypes.Type(value = ListDeleted.class, name = "listDeleted"),
    @JsonSubTypes.Type(value = ListEdited.class, name = "listEdited"),
    @JsonSubTypes.Type(value = BoardDeleted.class, name = "boardDeleted"),
    @JsonSubTypes.Type(value = BoardEdited.class, name = "boardEdited")
})
public abstract class BoardUpdate implements Message {

    public static final String QUEUE = "board";

    private static IUserData userData;

    public static void setUserData(IUserData userData) {
        BoardUpdate.userData = userData;
    }

    private int boardID;

    public BoardUpdate(int boardID) {
        this.boardID = boardID;
    }

    public BoardUpdate() {
    }

    public int getBoardID() {
        return boardID;
    }

    public void setBoardID(int boardID) {
        this.boardID = boardID;
    }

    public static IUserData getUserData() {
        return userData;
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

    public abstract IdResponseModel sendToServer(IServerUtils server);

    public abstract void apply(IUserData data);

}
