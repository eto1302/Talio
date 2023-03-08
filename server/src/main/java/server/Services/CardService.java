package server.Services;

import commons.Card;
import org.springframework.stereotype.Service;
import commons.Task;
import server.database.CardRepository;

import java.util.*;

@Service
public class CardService {

    CardRepository cardRepository;
    public CardService(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }
    public Card getCard(){
        List<Task> tasks = new ArrayList<>();
        return new Card("Urgent!", tasks);
    }

    public Card getCardById(int id){
        return this.cardRepository.getById(id);
    }

}
