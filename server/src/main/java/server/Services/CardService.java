package server.Services;

import commons.Board;
import commons.Card;
import org.springframework.stereotype.Service;
import commons.Task;
import server.database.BoardRepository;
import server.database.CardRepository;

import java.util.*;

@Service
public class CardService {

    CardRepository cardRepository;
    BoardRepository boardRepository;
    public CardService(CardRepository cardRepository, BoardRepository boardRepository){
        this.cardRepository = cardRepository;
        this.boardRepository = boardRepository;
    }
    public Card getCard(){
        List<Task> tasks = new ArrayList<>();
        return new Card("Urgent!", tasks);
    }

    public Card getCardById(int id){
        return this.cardRepository.getById(id);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    /**
     * Add card to its corresponding board and card repository.
     *
     * @param card card
     * @param boardId id of the board
     * @return id of the card , or -1 if there is no board of this id
     */
    public int addCard(Card card, int boardId) {
        try {
            Board board = boardRepository.getById(boardId);
            board.getCards().add(card);
            card.setBoard(board);
            cardRepository.save(card);
            return card.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    /**
     *  Remove the card from repository and the card list of the board.
     *
     * @param cardId id of the card
     * @param boardId if of the board
     * @return true if removal succeeds, false if there is no such board or card
     */
    public boolean removeCard(int cardId, int boardId) {
        try {
            Board board = boardRepository.getById(boardId);
            Card card = cardRepository.getById(cardId);
            board.getCards().remove(card);
            cardRepository.delete(card);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Rename the card by its id.
     *
     * @param cardId id of the card
     * @param name new name of the card
     * @return true of renaming succeed, else false
     */
    public boolean renameCard(int cardId, String name) {
        try {
            Card card = cardRepository.getById(cardId);
            card.setName(name);
            cardRepository.save(card);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
