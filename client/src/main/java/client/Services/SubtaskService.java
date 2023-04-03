package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Subtask;

import java.util.List;

public class SubtaskService {
    private final UserData userData;
    private final ServerUtils serverUtils;

    public SubtaskService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.serverUtils = serverUtils;
    }

    public List<Subtask> getSubtasksOrdered(int id) {
        return this.serverUtils.getSubtasksOrdered(id);
    }
}
