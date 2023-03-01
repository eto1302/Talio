package server.api;

import commons.Board;
import org.springframework.web.bind.annotation.*;
import server.Services.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String getBoardByID(@PathVariable int id) {
        Board board = boardService.getBoardById(id);
        return board.toString();
    }
}
