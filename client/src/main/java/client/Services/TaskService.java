package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Task;

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
}
