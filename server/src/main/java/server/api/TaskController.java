package server.api;

import org.springframework.web.bind.annotation.*;
import server.Services.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String getTask(@PathVariable int id){
        return this.taskService.getTaskById(id).toString();
    }
}
