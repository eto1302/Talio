package server.api;

import commons.Card;
import org.springframework.web.bind.annotation.*;
import server.Services.CardService;

import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/find/{id}")
    @ResponseBody
    public Card getCard(@PathVariable int id){
        return this.cardService.getCardById(id);
    }

    @GetMapping("/findAll")
    @ResponseBody
    public List<Card> getAllCards(){
        return this.cardService.getAllCards();
    }

    @PostMapping("/add/{boardId}")
    public int addCard(@PathVariable int boardId, @RequestBody Card card) {
        return cardService.addCard(card, boardId);
    }

    @GetMapping("/rename/{cardId}/{name}")
    public boolean rename(@PathVariable int cardId, @PathVariable String name) {
        return cardService.renameCard(cardId, name);
    }

    @GetMapping("/delete/{boardId}/{cardId}")
    public boolean delete(@PathVariable int boardId, @PathVariable int cardId) {
        return cardService.removeCard(cardId, boardId);
    }
}
