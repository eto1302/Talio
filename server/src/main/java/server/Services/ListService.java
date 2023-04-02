package server.Services;

import commons.Board;
import commons.List;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.ListRepositoy;

import java.util.NoSuchElementException;
import java.util.Set;


@Service
public class ListService {

    private ListRepositoy listRepositoy;
    private BoardRepository boardRepository;

    public ListService(ListRepositoy listRepositoy, BoardRepository boardRepository){
        this.listRepositoy = listRepositoy;
        this.boardRepository = boardRepository;
    }


    public commons.List getListById(int id){
        return this.listRepositoy.getListByID(id);
    }

    public java.util.List<commons.List> getAllLists() {
        return listRepositoy.findAll();
    }

    public Set<List> getAllListByBoard(int boardId) throws NoSuchElementException {
        if(!boardRepository.existsById(boardId)) throw new NoSuchElementException();
        return boardRepository.getBoardByID(boardId).getLists();
    }

    /**
     * Add list to its corresponding board and list repository.
     *
     * @param list list
     * @param boardId id of the board
     * @return id of the list , or -1 if there is no board of this id
     */
    public IdResponseModel addList(commons.List list, int boardId) {
        try {
            Board board = boardRepository.getById(boardId);
            board.getLists().add(list);
            list.setBoard(board);
            list.setBoardId(boardId);
            listRepositoy.save(list);
            return new IdResponseModel(list.getId(), null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    /**
     *  Remove the list from repository and the list of the board.
     *
     * @param listId id of the list
     * @param boardId if of the board
     * @return true if removal succeeds, false if there is no such board or list
     */
    public IdResponseModel removeList(int listId, int boardId) {
        try {
            Board board = boardRepository.getById(boardId);
            List list = listRepositoy.getById(listId);
            board.getLists().remove(list);
            listRepositoy.delete(list);
            return new IdResponseModel(listId, null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    /**
     * Edits the list, setting it a new name, background and font color
     * @param boardId the id of the board the list is in
     * @param listId the id of the list to be edited
     * @param model the model in which we saved the new attributes
     * @return an appropriate response, containing the edited list's ID
     *           or -1 and an error message if the list doesn't exist
     */
    public IdResponseModel editList(int boardId, int listId, ListEditModel model) {
        try {
            List list = listRepositoy.getListByID(listId);
            list.setName(model.getName());
            listRepositoy.save(list);
            return new IdResponseModel(listId, null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

}
