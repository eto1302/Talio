package server.card;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService service;

    public CardController(CardService service) {
        this.service = service;
    }

    @GetMapping()
    public Card getCard(){
        return service.getCard();
    }
}
