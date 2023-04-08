package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;
import commons.models.TaskEditModel;

import java.util.ArrayList;
import java.util.Arrays;

public class TaskEdited extends BoardUpdate {

    private int listID;
    private int taskID;
    private TaskEditModel taskEditModel;

    public TaskEdited(int boardID, int listID, int taskID, TaskEditModel taskEditModel) {
        super(boardID);
        this.listID = listID;
        this.taskID = taskID;
        this.taskEditModel = taskEditModel;
    }

    public TaskEdited() {
        super();
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

    public TaskEditModel getTaskEditModel() {
        return taskEditModel;
    }

    public void setTaskEditModel(TaskEditModel taskEditModel) {
        this.taskEditModel = taskEditModel;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.editTask(taskID, taskEditModel);
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
        task.setTitle(taskEditModel.getTitle());
        task.setDescription(taskEditModel.getDescription());
        data.getShowCtrl().refreshBoardCtrl();
    }

}
