package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class SubtaskDeleted extends BoardUpdate{
    private int subtaskID;
    private int taskID;

    public SubtaskDeleted(int subtaskID, int taskID){
        this.subtaskID = subtaskID;
        this.taskID = taskID;
    }


    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.deleteSubtask(taskID, subtaskID);
    }

    @Override
    public void apply(IUserData data, IServerUtils serverUtils) {
        //TODO
    }
}
