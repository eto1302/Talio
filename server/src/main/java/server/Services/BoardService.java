package server.Services;

import commons.Board;
import commons.List;
import commons.models.BoardIdResponseModel;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class BoardService {
    private BoardRepository boardRepository;
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
     * @return the id of the board, or the error message if creation fails.
     */
    public BoardIdResponseModel saveBoard(Board board) {
        try {
            boardRepository.save(board);
            return new BoardIdResponseModel(board.getId(), null);
        } catch (Exception e) {
            return new BoardIdResponseModel(-1, e.getMessage());
        }
    }


    public Board getBoardById(int id){
        return boardRepository.getBoardByID(id);
    }

    public java.util.List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
}
