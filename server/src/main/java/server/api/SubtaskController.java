package server.api;

import commons.Subtask;
import commons.models.IdResponseModel;
import commons.models.SubtaskEditModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.SubtaskService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/subtask")
public class SubtaskController {

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<Subtask> getSubtask(@PathVariable int id){
        try {
            Subtask subtask = subtaskService.getSubtaskById(id);
            return ResponseEntity.ok(subtask);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getByTask/{taskID}")
    @ResponseBody
    public ResponseEntity<List<Subtask>> getSubtasksByTask (@PathVariable int taskID){
        try {
            List<Subtask> subtasks = subtaskService.getAllSubtasksByTaskID(taskID);
            return ResponseEntity.ok(subtasks);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity<List<Subtask>> getAllSubtasks(){
        List<Subtask> subtasks = subtaskService.getAllSubtasks();
        return ResponseEntity.ok(subtasks);
    }
    @GetMapping("/getOrdered/{taskId}")
    @ResponseBody
    public List<Subtask> getSubtasksOrdered(@PathVariable int taskId){
        return subtaskService.getSubtasksOrdered(taskId);
    }

    @DeleteMapping("/delete/{taskID}/{subtaskID}")
    public IdResponseModel removeSubtask(@PathVariable int taskID, @PathVariable int subtaskID){
        return subtaskService.removeSubtask(subtaskID, taskID);
    }

    @PostMapping ("/add/{taskID}")
    public IdResponseModel addSubtask(@PathVariable int taskID, @RequestBody Subtask subtask){
        return subtaskService.addSubtask(subtask, taskID);
    }

    @PutMapping("/edit/{subtaskID}")
    public IdResponseModel editSubtask(@PathVariable int subtaskID,
                                       @RequestBody SubtaskEditModel model){
        return subtaskService.editSubtask(subtaskID, model);
    }
}
