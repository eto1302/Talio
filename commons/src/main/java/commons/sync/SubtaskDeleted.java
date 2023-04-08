package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class SubtaskDeleted extends BoardUpdate{
    private int subtaskID;
    private int taskID;
    private int listID;

    public SubtaskDeleted(int boardID, int subtaskID, int taskID, int listID){
        super(boardID);
        this.subtaskID = subtaskID;
        this.taskID = taskID;
        this.listID = listID;
    }

    public SubtaskDeleted() {}

    public int getSubtaskID() {
        return subtaskID;
    }

    public void setSubtaskID(int subtaskID) {
        this.subtaskID = subtaskID;
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
        return server.deleteSubtask(taskID, subtaskID);
    }

    @Override
    public void apply(IUserData data) {
        commons.List list = data.getCurrentBoard().getLists().stream()
                .filter(e -> e.getId() == listID).findFirst().orElse(null);
        commons.Task task = list.getTasks().stream().filter(e ->
                e.getId() == taskID).findFirst().orElse(null);
        task.getSubtasks().removeIf(e -> e.getId() == subtaskID);
        data.getShowCtrl().refreshSubtasks();
    }
}
