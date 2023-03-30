package server.Services;

import commons.Subtask;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.SubtaskEditModel;
import org.springframework.stereotype.Service;
import server.database.SubtaskRepository;
import server.database.TaskRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SubtaskService {

    private TaskRepository taskRepository;
    private SubtaskRepository subtaskRepository;

    public SubtaskService(TaskRepository taskRepository, SubtaskRepository subtaskRepository) {
        this.taskRepository = taskRepository;
        this.subtaskRepository = subtaskRepository;
    }

    /**
     * returns the subtask with a specific id
     * @param id the id of the subtask
     * @return the subtask
     */
    public Subtask getSubtaskById(int id){
        return subtaskRepository.getSubtaskByID(id);
    }

    /**
     * Gets all the subtasks from the database
     * @return the list of all the subtasks
     */
    public List<Subtask> getAllSubtasks(){
        return subtaskRepository.findAll();
    }

    /**
     * Gets all subtasks associated with a certain task
     * @param taskID the id of the associated task
     * @return the list of all subtasks associated with a task
     * @throws NoSuchElementException in case the associated task doesn't exist
     */
    public List<Subtask> getAllSubtasksByTaskID (int taskID) throws NoSuchElementException {
        if (!taskRepository.existsById(taskID))
            throw new NoSuchElementException();
        Task task = taskRepository.getById(taskID);
        return task.getSubtasks();
    }

    public List<Subtask> getSubtasksOrdered (int taskID){
        return subtaskRepository.getSubtasksOrdered(taskID);
    }

    /**
     * Adds a subtask to the associated task and in its respective repository
     * @param subtask the subtask to be added
     * @param taskID the id of the associated task
     * @return the appropriate response, containing either the subtask's new ID,
     *      or -1 and an error message if there is no task with such ID
     */
    public IdResponseModel addSubtask(Subtask subtask, int taskID) {
        try {
            Task task = taskRepository.getById(taskID);
            task.getSubtasks().add(subtask);
            subtask.setTask(task);
            subtask.setTaskID(taskID);
            subtaskRepository.save(subtask);
            return new IdResponseModel(subtask.getId(), null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    /**
     * Removes the Subtask from the associated task, and from the repository
     * @param subtaskId the id of the subtask to be deleted
     * @param taskId id of the associated task
     * @return an appropriate response, containing the deleted subtask's ID
     *      or -1 and an error message if the subtask or task doesn't exist
     */
    public IdResponseModel removeSubtask(int subtaskId, int taskId) {
        try {
            Task task = taskRepository.getById(taskId);
            Subtask subtask = subtaskRepository.getSubtaskByID(subtaskId);
            task.getSubtasks().remove(subtask);
            subtaskRepository.delete(subtask);
            return new IdResponseModel(subtaskId, null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    /**
     * Edits the subtask, setting it a new description and status
     * @param subtaskID the id of the subtask
     * @param model of the editable attributes
     * @return an appropriate response, containing the edited subtask's ID
     *           or -1 and an error message if the subtask doesn't exist
     */
    public IdResponseModel editSubtask(int subtaskID, SubtaskEditModel model) {
        try {
            Subtask subtask = subtaskRepository.getSubtaskByID(subtaskID);
            subtask.setChecked(model.isChecked());
            subtask.setDescription(model.getDescription());
            subtask.setIndex(model.getIndex());
            subtaskRepository.save(subtask);
            return new IdResponseModel(subtaskID, null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

}
