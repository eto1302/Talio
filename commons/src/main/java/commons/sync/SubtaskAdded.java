package commons.sync;

import commons.Subtask;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class SubtaskAdded extends BoardUpdate{
    private Subtask subtask;
    private int taskID;

    public SubtaskAdded(int boardID, int taskID, Subtask subtask){
        super(boardID);
        this.subtask = subtask;
        this.taskID = taskID;
    }

    public SubtaskAdded(){super();};

    public Subtask getSubtask() {
        return subtask;
    }

    public void setSubtask(Subtask subtask) {
        this.subtask = subtask;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.addSubtask(subtask, taskID);
        subtask.setId(id.getId());
        return id;
    }

    @Override
    public void apply(IUserData data) {
        //TODO
    }
}
