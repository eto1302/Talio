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
import commons.sync.TaskEdited;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
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
        ResponseEntity<Task[]> response = serverUtils.getTasksOrdered(id);
        if(!response.getStatusCode().is2xxSuccessful()){
            return new ArrayList<>();
        }
        return Arrays.asList(response.getBody());
    }

    public Task getTask(int id) {
        return this.serverUtils.getTask(id);
    }

    public IdResponseModel editTask(Task task, commons.List list, int newIndex){
        TaskEditModel model = new TaskEditModel(task.getTitle(),
                task.getDescription(), newIndex, list, task.getColorId());
        return userData.updateBoard(new TaskEdited(list.getBoardId(),
                list.getId(), task.getId(), model));
    }

    public IdResponseModel addTask(String title, commons.List list) {
        Task task = new Task(null, title, list.getId(), new ArrayList<Subtask>());
        int colorId = this.boardService.getCurrentBoard().getColors()
                .stream().filter(Color::getIsDefault).findFirst().get().getId();
        task.setTitle(title);
        task.setColorId(colorId);
        ResponseEntity<Task[]> response = serverUtils.getTaskByList(list.getId());
        if(!response.getStatusCode().is2xxSuccessful()){
            return new IdResponseModel(-1, "Couldn't get tasks by list");
        }
        Task[] tasks = response.getBody();
        task.setIndex(tasks.length);

        return userData.updateBoard(new
                TaskAdded(list.getBoardId(), list.getId(), task));
    }

    public IdResponseModel deleteTask(Task task) {
        return userData.updateBoard(new TaskDeleted(userData
                .getCurrentBoard().getId(), task.getId(), task.getListID()));
    }
}
