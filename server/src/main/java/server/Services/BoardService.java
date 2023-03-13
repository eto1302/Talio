package server.Services;

import commons.Board;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

@Service
public class BoardService {
    private BoardRepository boardRepository;
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
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
