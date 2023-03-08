package server.Services;

import commons.Board;
import org.springframework.stereotype.Service;
import commons.Card;
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
        Card firstCard = new Card("To Do", new ArrayList<>());
        Set<Card> cards = new HashSet<>();
        cards.add(firstCard);

        return new Board("TEAM", "12345", cards);
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
}
