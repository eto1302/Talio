package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Task;
import commons.models.TaskEditModel;

import java.util.List;

public class TaskService {
    private final UserData userData;
    private final ServerUtils serverUtils;

    public TaskService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.serverUtils = serverUtils;
    }

    public List<Task> getTasksOrdered(int id) {
        return serverUtils.getTasksOrdered(id);
    }

    public Task getTask(int id) {
        return this.serverUtils.getTask(id);
    }

    public void editTask(Task task, commons.List list, int newIndex){
        TaskEditModel model = new TaskEditModel(task.getTitle(),
                task.getDescription(), newIndex, list, task.getColorId());
        serverUtils.editTask(task.getId(), model);
    }
}
