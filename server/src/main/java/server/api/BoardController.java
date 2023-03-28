package server.api;

import commons.Board;
import commons.models.IdResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.BoardService;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Get a board by its id.
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
     * @return  the id of the created Board
     * if the creation is successful,otherwise return bad request.
     */
    @PostMapping("/create")
    public IdResponseModel create(@RequestBody Board board) {
        return boardService.saveBoard(board);
    }

    @GetMapping("/findAll")
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/getByInvite/{inviteKey}")
    @ResponseBody
    public ResponseEntity<Board> getBoardByInviteKey(@PathVariable String inviteKey){
        try{
            Board board = boardService.getBoardByInviteKey(inviteKey);
            return ResponseEntity.ok(board);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/delete/{id}")
    public IdResponseModel deleteBoard(@PathVariable int id) {
        return boardService.deleteBoard(id);
    }

    @GetMapping("/verify/{password}")
    public boolean verifyAdminPassword(@PathVariable String password) {
        return boardService.verifyAdminPassword(password);
    }
    
}
