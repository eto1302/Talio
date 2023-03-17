package server.Services;

import commons.Board;
import commons.List;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.ListRepositoy;



@Service
public class ListService {

    private ListRepositoy listRepositoy;
    private BoardRepository boardRepository;

    public ListService(ListRepositoy listRepositoy, BoardRepository boardRepository){
        this.listRepositoy = listRepositoy;
        this.boardRepository = boardRepository;
    }


    public List getListById(int id){
        return this.listRepositoy.getById(id);
    }

    public java.util.List<commons.List> getAllLists() {
        return listRepositoy.findAll();
    }

    /**
     * Add list to its corresponding board and list repository.
     *
     * @param list list
     * @param boardId id of the board
     * @return id of the list , or -1 if there is no board of this id
     */
    public int addList(commons.List list, int boardId) {
        try {
            Board board = boardRepository.getById(boardId);
            board.getLists().add(list);
            list.setBoard(board);
            listRepositoy.save(list);
            return list.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    /**
     *  Remove the list from repository and the list of the board.
     *
     * @param listId id of the list
     * @param boardId if of the board
     * @return true if removal succeeds, false if there is no such board or list
     */
    public boolean removeList(int listId, int boardId) {
        try {
            Board board = boardRepository.getById(boardId);
            List list = listRepositoy.getById(listId);
            board.getLists().remove(list);
            listRepositoy.delete(list);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Rename the list by its id.
     *
     * @param listId id of the list
     * @param name new name of the list
     * @return true of renaming succeed, else false
     */
    public boolean editList(int listId, String name, String background, String font) {
        try {
            List list = listRepositoy.getById(listId);
            list.setName(name);
            list.setBackgroundColor(background);
            list.setFontColor(font);
            listRepositoy.save(list);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
