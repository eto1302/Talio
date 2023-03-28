package server.api;

import commons.Task;
import commons.models.IdResponseModel;
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

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<commons.Task> getTask(@PathVariable int id){
        try{
            commons.Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getByList/{id}")
    public ResponseEntity<java.util.List<Task>> getByList(@PathVariable int id){
        try{
            java.util.List<Task> tasks = taskService.getAllTaskByList(id);
            return ResponseEntity.ok(tasks);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findAll")
    @ResponseBody
    public java.util.List<Task> getAllTasks(){
        return this.taskService.getAllTasks();
    }

    @PostMapping("/add/{listID}")
    public IdResponseModel addTask(@PathVariable int listID, @RequestBody commons.Task task){
        return this.taskService.addTask(task, listID);
    }

    @PostMapping("/edit/{taskID}")
    public IdResponseModel editTask(@PathVariable int taskID,
                                    @RequestBody commons.models.TaskEditModel model){
        return this.taskService.editTask(taskID, model);
    }

    @GetMapping("/remove/{taskID}/{listID}")
    public IdResponseModel removeTask(@PathVariable int taskID, @PathVariable int listID){
        return this.taskService.removeTask(taskID, listID);
    }

    @GetMapping("/getSorted/{listId}")
    @ResponseBody
    public java.util.List<Task> getTasksSorted(@PathVariable int listId){
        return this.taskService.getTasksSortedByIndex(listId);
    }
}
