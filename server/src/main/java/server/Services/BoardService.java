package server.Services;

import commons.Board;
import commons.List;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class BoardService {
    BoardRepository boardRepository;
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }
    public Board getBoard(){
        commons.List firstList = List.create("To Do", new ArrayList<>());
        Set<commons.List> lists = new HashSet<>();
        lists.add(firstList);

        return Board.create("TEAM", "12345", lists);
    }

    /**
     * Create a board and save it in the database.
     *
     * @param board the content of the board
     * @return the id of the board
     * @throws Exception if creation fails.
     */
    public int saveBoard(Board board) throws Exception {
        try {
            boardRepository.save(board);
            return board.getId();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public Board getBoardById(int id){
        return boardRepository.getBoardByID(id);
    }

    public java.util.List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
}
