package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

import java.util.ArrayList;
import java.util.Arrays;

public class TaskDeleted extends BoardUpdate{
    private int taskID;
    private int listID;

    public TaskDeleted(int boardID, int taskID, int listID) {
        super(boardID);
        this.taskID = taskID;
        this.listID = listID;
    }

    public TaskDeleted() {
        super();
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.deleteTask(taskID, listID);
    }

    @Override
    public void apply(IUserData data, IServerUtils serverUtils) {
        commons.List list = data.getCurrentBoard().getLists().stream()
                .filter(e -> e.getId() == listID).findFirst().orElse(null);
        if(list.getTasks() == null || list.getTasks().isEmpty()) {
            list.setTasks(new ArrayList<>(Arrays.asList(
                serverUtils.getTasksOrdered(list.getId()).getBody())));}
        data.getShowCtrl().deleteTask(listID, taskID);
    }

}
