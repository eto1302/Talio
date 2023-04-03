package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;
import commons.models.SubtaskEditModel;

public class SubtaskEdited extends BoardUpdate{
    private int taskID;
    private int subtaskID;
    private SubtaskEditModel subtaskEditModel;

    public SubtaskEdited(int boardID, int taskID, int subtaskID, SubtaskEditModel subtaskEditModel){
        super(boardID);
        this.taskID = taskID;
        this.subtaskID = subtaskID;
        this.subtaskEditModel = subtaskEditModel;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.editSubtask(subtaskID, subtaskEditModel);
        return id;
    }

    @Override
    public void apply(IUserData data) {
        //TODO
    }
}
