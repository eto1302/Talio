package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class TaskDeleted extends BoardUpdate{
    private int taskID;
    private int listID;

    public TaskDeleted(int taskID, int listID){
        this.taskID = taskID;
        this.listID = listID;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.removeTask(taskID, listID);
    }

    @Override
    public void apply(IUserData data) {
        //TODO
    }
}
