package commons.sync;

import commons.List;
import commons.Task;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;
import commons.models.SubtaskEditModel;

import java.util.ArrayList;
import java.util.Arrays;

public class SubtaskEdited extends BoardUpdate{
    private int taskID;
    private int subtaskID;
    private SubtaskEditModel subtaskEditModel;

    public SubtaskEdited(int boardID, int taskID, int subtaskID, SubtaskEditModel subtaskEditModel){
        super(boardID);
        this.taskID = taskID;
        this.subtaskID = subtaskID;
        this.subtaskEditModel = subtaskEditModel;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.editSubtask(subtaskID, subtaskEditModel);
        return id;
    }

    @Override
    public void apply(IUserData data, IServerUtils serverUtils) {
        Task task = null;
        for(List list : data.getCurrentBoard().getLists()){
            if(list.getTasks() == null || list.getTasks().isEmpty()) {
                list.setTasks(new ArrayList<>(Arrays.asList(
                        serverUtils.getTasksOrdered(list.getId()).getBody())));}
            for(Task curr : list.getTasks()){
                if(curr.getId() == taskID){
                    task = curr;
                }
            }
        }
        if(task == null) return;
        if(task.getSubtasks() == null || task.getSubtasks().isEmpty()) {
            task.setSubtasks(new ArrayList<>(Arrays.asList(
                    serverUtils.getSubtasksOrdered(taskID).getBody())));}
        commons.Subtask subtask = task.getSubtasks().stream().filter(e ->
                e.getId() == subtaskID).findFirst().orElse(null);
        subtask.setDescription(subtaskEditModel.getDescription());
        subtask.setIndex(subtaskEditModel.getIndex());
        subtask.setChecked(subtaskEditModel.isChecked());
        //data.getShowCtrl().refreshBoardCtrl();
    }
}
