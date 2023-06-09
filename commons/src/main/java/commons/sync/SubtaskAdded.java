package commons.sync;

import commons.Subtask;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

import java.util.ArrayList;
import java.util.Arrays;

public class SubtaskAdded extends BoardUpdate{
    private Subtask subtask;
    private int listID;
    private int taskID;

    public SubtaskAdded(int boardID, int listID, int taskID, Subtask subtask){
        super(boardID);
        this.subtask = subtask;
        this.listID = listID;
        this.taskID = taskID;
    }

    public SubtaskAdded(){super();}

    public Subtask getSubtask() {
        return subtask;
    }

    public void setSubtask(Subtask subtask) {
        this.subtask = subtask;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
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
    public void apply(IUserData data, IServerUtils serverUtils) {
        commons.List list = data.getCurrentBoard().getLists().stream()
                .filter(e -> e.getId() == listID).findFirst().orElse(null);
        if(list.getTasks() == null || list.getTasks().isEmpty()) {
            list.setTasks(new ArrayList<>(Arrays.asList(
                    serverUtils.getTasksOrdered(list.getId()).getBody())));}
        commons.Task task = list.getTasks().stream().filter(e ->
                e.getId() == taskID).findFirst().orElse(null);
        if(task == null) return;
        if(task.getSubtasks() == null || task.getSubtasks().isEmpty()) {
            task.setSubtasks(new ArrayList<>(Arrays.asList(
                    serverUtils.getSubtasksOrdered(taskID).getBody())));}
        task.getSubtasks().add(subtask);
        data.getShowCtrl().refreshSubtasks();
    }
}
