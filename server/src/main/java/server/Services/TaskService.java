package server.Services;

import commons.models.IdResponseModel;
import commons.models.TaskEditModel;
import org.springframework.stereotype.Service;
import server.database.ListRepositoy;
import server.database.TaskRepository;

import java.util.NoSuchElementException;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    private ListRepositoy listRepositoy;

    public TaskService(TaskRepository taskRepository, ListRepositoy listRepositoy){
        this.taskRepository = taskRepository;
        this.listRepositoy = listRepositoy;
    }

    public commons.Task getTaskById(int id){
        return this.taskRepository.getTaskById(id);
    }

    public java.util.List<commons.Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public java.util.List<commons.Task> getTasksSortedByIndex(int listId){
        return taskRepository.getTasksByIndex(listId);
    }

    public java.util.List<commons.Task> getAllTaskByList(int listID) throws NoSuchElementException {
        if(!listRepositoy.existsById(listID)){
            throw new NoSuchElementException();
        }
        return listRepositoy.getListByID(listID).getTasks();
    }

    public IdResponseModel addTask(commons.Task task, int listID){
        try{
            commons.List list = listRepositoy.getListByID(listID);
            list.getTasks().add(task);
            task.setList(list);
            task.setListID(listID);
            taskRepository.save(task);
            return new IdResponseModel(task.getId(), null);
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public IdResponseModel removeTask(int taskID, int listID){
        try{
            commons.List list = listRepositoy.getListByID(listID);
            commons.Task task = taskRepository.getTaskById(taskID);
            list.getTasks().remove(task);
            taskRepository.delete(task);
            return new IdResponseModel(taskID, null);
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public IdResponseModel editTask(int taskID, TaskEditModel model){
        try{
            commons.Task task = taskRepository.getTaskById(taskID);
            task.setTitle(model.getTitle());
            task.setDescription(model.getDescription());
            task.setList(model.getList());
            task.setListID(model.getList().getId());
            task.setIndex(model.getIndex());
            taskRepository.save(task);
            return new IdResponseModel(taskID, null);
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }
}
