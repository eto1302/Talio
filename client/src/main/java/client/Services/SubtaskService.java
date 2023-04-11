package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Subtask;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.SubtaskEditModel;
import commons.sync.SubtaskAdded;
import commons.sync.SubtaskDeleted;
import commons.sync.SubtaskEdited;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SubtaskService {
    private final UserData userData;
    private final ServerUtils serverUtils;
    private ListService listService;
    private TaskService taskService;

    public SubtaskService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.serverUtils = serverUtils;
        this.listService = new ListService(userData, serverUtils);
        this.taskService = new TaskService(userData, serverUtils);
    }

    public SubtaskService(
        UserData userData, ServerUtils serverUtils,
        ListService listService, TaskService taskService
    ){
        this.userData = userData;
        this.serverUtils = serverUtils;
        this.listService = listService;
        this.taskService = taskService;
    }

    public List<Subtask> getSubtasksOrdered(int id) {
        var response = this.serverUtils.getSubtasksOrdered(id);
        if(!response.getStatusCode().is2xxSuccessful()){
            return new ArrayList<>();
        }
        return new LinkedList<Subtask>(Arrays.asList(response.getBody()));
    }

    public List<Subtask> getSubtasksByTask(int id) {
        var response = this.serverUtils.getSubtasksByTask(id);
        if(!response.getStatusCode().is2xxSuccessful()){
            return new ArrayList<>();
        }
        return new LinkedList<Subtask>(Arrays.asList(response.getBody()));
    }

    public IdResponseModel add(String name, Task task) {
        Subtask subtask = new Subtask(name, false, task.getId());
        java.util.List<Subtask> subtasks = this.getSubtasksByTask(task.getId());
        subtask.setIndex(subtasks.size());

        commons.List list = listService.getList(task.getListID());
        return userData.updateBoard(new
                SubtaskAdded(list.getBoardId(), list.getId(), task.getId(), subtask));
    }

    public IdResponseModel delete(Subtask subtask) {
        commons.Task task = taskService.getTask(subtask.getTaskID());
        commons.List list = listService.getList(task.getListID());
        return userData.updateBoard(new
                SubtaskDeleted(list.getBoardId(), subtask.getId(), task.getId(), list.getId()));
    }

    public IdResponseModel setChecked(Subtask subtask, boolean checked, int index) {
        commons.Task task = taskService.getTask(subtask.getTaskID());
        commons.List list = listService.getList(task.getListID());
        SubtaskEditModel model = new SubtaskEditModel(subtask.getDescription(),
                checked, index);
        return userData.updateBoard(new SubtaskEdited(list.getBoardId(),
                list.getId(), task.getId(), subtask.getId(), model));
    }

    public IdResponseModel setDescription(Subtask subtask, String desc, int index) {
        commons.Task task = taskService.getTask(subtask.getTaskID());
        commons.List list = listService.getList(task.getListID());
        SubtaskEditModel model = new SubtaskEditModel(desc,
                subtask.isChecked(), index);
        return userData.updateBoard(new SubtaskEdited(list.getBoardId(),
                list.getId(), task.getId(), subtask.getId(), model));
    }

    /**
     * Rearranges the subtasks' indexes after a drag event
     * @param orderedSubtasks the list of subtasks in a desired order
     */
    public void writeSubtaskOrder(java.util.List<Subtask> orderedSubtasks){
        int i = 0;
        if(orderedSubtasks.size() == 0)
            return;

        commons.Task task = taskService.getTask(orderedSubtasks.get(0).getTaskID());
        commons.List list = listService.getList(task.getListID());

        for(Subtask subtask : orderedSubtasks) {
            SubtaskEditModel model = new SubtaskEditModel(subtask.getDescription(),
                    subtask.isChecked(), i++);
            userData.updateBoard(new SubtaskEdited(list.getBoardId(), list.getId(),
                    task.getId(), subtask.getId(), model));
        }
    }

}
