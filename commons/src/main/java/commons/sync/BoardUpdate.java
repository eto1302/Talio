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
        defaultImpl = ListDeleted.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ListAdded.class, name = "listAdded"),
    @JsonSubTypes.Type(value = ListDeleted.class, name = "listDeleted"),
    @JsonSubTypes.Type(value = ListEdited.class, name = "listEdited"),
    @JsonSubTypes.Type(value = BoardEdited.class, name = "boardEdited"),
    @JsonSubTypes.Type(value = BoardDeleted.class, name = "boardDeleted"),
    @JsonSubTypes.Type(value = TaskAdded.class, name = "taskAdded"),
    @JsonSubTypes.Type(value = TaskEdited.class, name = "taskEdited"),
    @JsonSubTypes.Type(value = TaskDeleted.class, name = "taskDeleted"),
    @JsonSubTypes.Type(value = ColorAdded.class, name = "colorAdded"),
    @JsonSubTypes.Type(value = ColorEdited.class, name = "colorEdited"),
    @JsonSubTypes.Type(value = ColorDeleted.class, name = "colorDeleted"),
        @JsonSubTypes.Type(value = TagAddedToTask.class, name = "tagAddedToTask"),
        @JsonSubTypes.Type(value = TagCreated.class, name= "tagCreated"),
        @JsonSubTypes.Type(value = TagDeleted.class, name = "tagDeleted"),
        @JsonSubTypes.Type(value = TagEdited.class, name = "tagEdited"),
        @JsonSubTypes.Type(value = TagRemovedFromTask.class, name = "tagRemovedFromTask")
})
public abstract class BoardUpdate implements Message {

    public static final String QUEUE = "board";

    private static IUserData userData;

    public static void setUserData(IUserData userData) {
        BoardUpdate.userData = userData;
    }

    private int boardID;

    private boolean consumed;

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

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public static IUserData getUserData() {
        return userData;
    }

    public String getSendQueue() {
        return "/app/" + QUEUE + boardID;
    }

    @Override
    public void consume() {
        if(!consumed){
            if(userData != null && userData.getCurrentBoard() != null
                    && userData.getCurrentBoard().getId() == boardID)
                apply(userData);
            consumed = true;
        }
    }

    public abstract IdResponseModel sendToServer(IServerUtils server);

    public abstract void apply(IUserData data);

}
