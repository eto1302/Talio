package server.card;

import org.springframework.stereotype.Service;
import server.task.Task;

import java.util.*;

@Service
public class CardService {

    public Card getCard(){
        List<Task> tasks = new ArrayList<>();
        return new Card("Urgent!", tasks);
    }
}
