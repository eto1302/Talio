package commons.sync;

import commons.Task;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class TaskAdded extends BoardUpdate{
    private Task task;
    private int listID;

    public TaskAdded(int listID, Task task){
        this.task = task;
        this.listID = listID;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.addTask(task, listID);
        task.setId(id.getId());
        return id;
    }

    @Override
    public void apply(IUserData data) {
        //TODO
    }
}
