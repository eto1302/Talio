package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Color;
import commons.Subtask;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.TaskEditModel;
import commons.sync.TaskAdded;
import commons.sync.TaskDeleted;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final UserData userData;
    private final ServerUtils serverUtils;
    private BoardService boardService;

    public TaskService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.serverUtils = serverUtils;
        this.boardService = new BoardService(userData, serverUtils);
    }

    public List<Task> getTasksOrdered(int id) {
        return serverUtils.getTasksOrdered(id);
    }

    public Task getTask(int id) {
        return this.serverUtils.getTask(id);
    }

    public IdResponseModel editTask(Task task, commons.List list, int newIndex){
        TaskEditModel model = new TaskEditModel(task.getTitle(),
                task.getDescription(), newIndex, list, task.getColorId());
        return serverUtils.editTask(task.getId(), model);
    }

    public IdResponseModel addTask(String title, commons.List list) {
        Task task = Task.create(null, title, list.getId(), new ArrayList<Subtask>());
        int colorId = this.boardService.getCurrentBoard().getColors()
                .stream().filter(Color::getIsDefault).findFirst().get().getId();
        task.setTitle(title);
        task.setColorId(colorId);
        java.util.List<Task> tasks = serverUtils.getTaskByList(list.getId());
        task.setIndex(tasks.size());

        return userData.updateBoard(new
                TaskAdded(list.getBoardId(), list.getId(), task));
    }

    public IdResponseModel deleteTask(Task task) {
        return userData.updateBoard(new TaskDeleted(userData
                .getCurrentBoard().getId(), task.getId(), task.getListID()));
    }
}
