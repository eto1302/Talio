package server.api;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<commons.Task> getTask(@PathVariable int id){
        try{
            commons.Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        }
    }
}
