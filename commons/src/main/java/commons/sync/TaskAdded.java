package commons.sync;

import commons.Task;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

import java.util.ArrayList;
import java.util.Arrays;

public class TaskAdded extends BoardUpdate {

    private Task task;
    private int listID;

    public TaskAdded(int boardID, int listID, Task task) {
        super(boardID);
        this.task = task;
        this.listID = listID;
    }

    public TaskAdded() {
        super();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.addTask(task, listID);
        task.setId(id.getId());
        return id;
    }

    @Override
    public void apply(IUserData data, IServerUtils serverUtils) {
        commons.List list = data.getCurrentBoard().getLists().stream()
                .filter(e -> e.getId() == listID).findFirst().orElse(null);
        if(list.getTasks() == null || list.getTasks().isEmpty()) {
            list.setTasks(new ArrayList<>(Arrays.asList(
                    serverUtils.getTasksOrdered(list.getId()).getBody())));}
        list.getTasks().add(task);
        data.getShowCtrl().addTask(task, list);
    }
}
