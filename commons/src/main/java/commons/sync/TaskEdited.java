package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;
import commons.models.TaskEditModel;

public class TaskEdited extends BoardUpdate{
    private int taskID;
    private TaskEditModel taskEditModel;

    public TaskEdited(int taskID, TaskEditModel taskEditModel){
        this.taskID = taskID;
        this.taskEditModel = taskEditModel;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.editTask(taskID, taskEditModel);
        return id;
    }

    @Override
    public void apply(IUserData data) {
        //TODO
    }
}
