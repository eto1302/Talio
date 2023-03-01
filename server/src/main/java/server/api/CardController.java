package server.api;

import org.springframework.web.bind.annotation.*;
import server.Services.CardService;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String getCard(@PathVariable int id){
        return this.cardService.getCardById(id).toString();
    }
}
