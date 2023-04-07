package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Subtask;
import commons.Task;
import commons.models.IdResponseModel;
import commons.sync.SubtaskAdded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubtaskService {
    private final UserData userData;
    private final ServerUtils serverUtils;
    private ListService listService;

    public SubtaskService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.serverUtils = serverUtils;
        this.listService = new ListService(userData, serverUtils);
    }

    public List<Subtask> getSubtasksOrdered(int id) {
        var response = this.serverUtils.getSubtasksOrdered(id);
        if(!response.getStatusCode().is2xxSuccessful()){
            return new ArrayList<>();
        }
        return Arrays.asList(response.getBody());
    }

    public List<Subtask> getSubtasksByTask(int id) {
        var response = this.serverUtils.getSubtasksByTask(id);
        if(!response.getStatusCode().is2xxSuccessful()){
            return new ArrayList<>();
        }
        return Arrays.asList(response.getBody());
    }

    public IdResponseModel add(String name, Task task) {
        Subtask subtask = new Subtask(name, false, task.getId());
        java.util.List<Subtask> subtasks = this.getSubtasksByTask(task.getId());
        subtask.setIndex(subtasks.size());

        commons.List list = listService.getList(task.getListID());
        return userData.updateBoard(new
                SubtaskAdded(list.getBoardId(), task.getId(), subtask));
    }
}
