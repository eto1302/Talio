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

    public Board getBoardById(int id){
        return boardRepository.getById(id);
    }
}
