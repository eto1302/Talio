package server.Services;

import commons.Task;
import org.springframework.stereotype.Service;
import server.database.TaskRepository;

@Service
public class TaskService {

    TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public Task getTaskById(int id){
        return this.taskRepository.getById(id);
    }

}
