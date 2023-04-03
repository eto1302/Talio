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

import java.util.ArrayList;
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
        return this.tagRepository.getById(id);
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
            Board board = boardRepository.getBoardByID(tag.getBoardId());
            tag.setBoard(board);
//            task.getTags().add(tag);
            if(tag.getTasks() == null) {tag.setTasks(new ArrayList<>());}
            tag.getTasks().add(task);
            tag.getTaskIDs().add(taskID);
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
            tagRepository.save(tag);
            return new IdResponseModel(tag.getId(), null);
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public IdResponseModel removeFromTask(int tagID, int taskID){
        try{
            Tag tag = tagRepository.getById(tagID);
            Task task = taskRepository.getTaskById(taskID);
            Board board = boardRepository.getBoardByID(tag.getBoardId());
            task.getTags().remove(tag);
            tag.getTasks().remove(task);
            tag.getTaskIDs().remove((Integer) taskID);
//            if(tag.getTasks().size() == 0){
//                tagRepository.delete(tag);
//            }
            tagRepository.save(tag);
            return new IdResponseModel(tagID, null);
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public IdResponseModel removeFromBoard(int tagID, int boardID){
        try{
            Tag tag = tagRepository.getById(tagID);
            Board board = boardRepository.getBoardByID(boardID);
            board.getTags().remove(tag);
            tagRepository.delete(tag);
            return new IdResponseModel(tagID, null);
        }
        catch(Exception e){
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public IdResponseModel editTag(int tagID, TagEditModel model){
        try{
            Tag tag = tagRepository.getById(tagID);
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
