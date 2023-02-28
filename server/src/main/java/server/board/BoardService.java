package server.board;

import org.springframework.stereotype.Service;
import server.card.Card;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class BoardService {

    public Board getBoard(){
        Card firstCard = new Card("To Do", new ArrayList<>());
        Set<Card> cards = new HashSet<>();
        cards.add(firstCard);

        return new Board("TEAM", "12345", cards);
    }
}
