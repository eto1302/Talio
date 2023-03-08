package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Get a board by its id.
     *
     * @param id the id to the board
     * @return the board or a bad request if there is no such board.
     */
    @GetMapping("/find/{id}")
    @ResponseBody
    public ResponseEntity<Board> getBoardByID(@PathVariable int id) {
        try {
            Board board = boardService.getBoardById(id);
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Create a new board and save it in the database.
     *
     * @param creationRequestModel creationRequestModel
     * @return  the id of the created Board if the creation is successful,otherwise return bad request.
     */
    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody Board board) {
        try {
            int id = boardService.saveBoard(board);
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(-1);
        }
    }
}
