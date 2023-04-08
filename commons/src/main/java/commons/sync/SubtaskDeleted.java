package commons.sync;

import commons.List;
import commons.Task;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

import java.util.ArrayList;
import java.util.Arrays;

public class SubtaskDeleted extends BoardUpdate{
    private int subtaskID;
    private int taskID;

    public SubtaskDeleted(int subtaskID, int taskID){
        this.subtaskID = subtaskID;
        this.taskID = taskID;
    }


    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.deleteSubtask(taskID, subtaskID);
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
        //data.getShowCtrl().deleteSubtask(listID, taskID);
    }
}
