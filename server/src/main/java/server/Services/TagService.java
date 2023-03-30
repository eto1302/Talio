package server.Services;

import commons.Board;
import commons.Tag;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.TagEditModel;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.TagRepository;
import server.database.TaskRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TagService {
    private TagRepository tagRepository;
    private TaskRepository taskRepository;
    private BoardRepository boardRepository;

    public TagService(TagRepository tagRepository,
                      TaskRepository taskRepository, BoardRepository boardRepository){
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
        this.boardRepository = boardRepository;
    }


    public Tag getTagById(int id) {
        return this.tagRepository.getTagById(id);
    }

    public java.util.List<Tag> getAllTagsByTask(int taskID) throws NoSuchElementException {
        if(!taskRepository.existsById(taskID)){
            throw new NoSuchElementException();
        }
        return taskRepository.getTaskById(taskID).getTags();
    }

    public java.util.List<Tag> getAllTagsByBoard(int boardID) throws NoSuchElementException {
        if(!boardRepository.existsById(boardID)){
            throw new NoSuchElementException();
        }
        return boardRepository.getBoardByID(boardID).getTags();
    }

    public IdResponseModel addTagToTask(Tag tag, int taskID){
        try{
            Task task = taskRepository.getTaskById(taskID);
            task.getTags().add(tag);
            tag.setTask(task);
            tag.setTaskID(taskID);
            tag.setBoardID(-1);
            tagRepository.save(tag);
            return new IdResponseModel(tag.getId(), null);
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public IdResponseModel addTagToBoard(Tag tag, int boardID){
        try{
            Board board = boardRepository.getBoardByID(boardID);
            board.getTags().add(tag);
            tag.setBoard(board);
            tag.setBoardID(boardID);
            tag.setTaskID(-1);
            tagRepository.save(tag);
            return new IdResponseModel(tag.getId(), null);
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public IdResponseModel removeTag(int tagID){
        try{
            Tag tag = tagRepository.getTagById(tagID);
            if(tag.getTaskID() != -1){
                Task task = taskRepository.getTaskById(tag.getTaskID());
                task.getTags().remove(tag);
                tagRepository.delete(tag);
                return new IdResponseModel(tagID, null);
            }
            else{
                Board board = boardRepository.getBoardByID(tag.getBoardID());
                board.getTags().remove(tag);
                tagRepository.delete(tag);
                return new IdResponseModel(tagID, null);
            }
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public IdResponseModel editTag(int tagID, TagEditModel model){
        try{
            Tag tag = tagRepository.getTagById(tagID);
            tag.setName(model.getName());
            tag.setColor(model.getColor());
            tagRepository.save(tag);
            return new IdResponseModel(tagID, null);
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public List<Tag> findAll() { return tagRepository.findAll(); }

    public Tag save(Tag tag) { return tagRepository.save(tag); }

    public long count() {return tagRepository.count();}
}
