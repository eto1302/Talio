package server.api;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Board;
import commons.BoardSummary;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.Services.BoardService;
import server.Services.BoardsUpdatedListener;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardsUpdatedListener boardsUpdatedListener;

    public BoardController(BoardService boardService, BoardsUpdatedListener boardsUpdatedListener) {
        this.boardService = boardService;
        this.boardsUpdatedListener = boardsUpdatedListener;
    }

    /**
     * Get a board by its id.
     * @param id the id to the board
     * @return the board or a bad request if there is no such board.
     */
    @GetMapping("/find/{id}")
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
        IdResponseModel response = boardService.saveBoard(board);
        boardService.fireBoardUpdateEvent();
        return response;
    }

    /**
     *  This method used long polling to get the updated boards.
     *  Created an initial deferred result and add it to the listener,
     *  when the boardList is updated, the listener will fire the event and
     *  set the result of the deferred result and return it to the client.
     *
     *  The annotation @JsonView(BoardSummary.class) is used to filter the fields,
     *  only the fields with the annotation @JsonView(BoardSummary.class) will be returned,
     *  as not all the fields are needed to be displayed in the admin scene.
     *
     * @return the updated boardList.
     */
    @GetMapping("/findAllUpdated")
    @JsonView(BoardSummary.class)
    public DeferredResult<List<Board>> getBoardsUpdated() {
        DeferredResult<List<Board>> deferredResult = new DeferredResult<>();
        boardsUpdatedListener.addDeferredResult(deferredResult);
        return deferredResult;
    }

    @GetMapping("/findAll")
    @JsonView(BoardSummary.class)
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
        IdResponseModel response = boardService.deleteBoard(id);
        boardService.fireBoardUpdateEvent();
        return response;
    }

    @GetMapping("/verify/{password}")
    public boolean verifyAdminPassword(@PathVariable String password) {
        return boardService.verifyAdminPassword(password);
    }
    @PostMapping("/edit/{boardId}")
    public IdResponseModel editList(@PathVariable int boardId, @RequestBody ListEditModel model) {
        IdResponseModel response = boardService.editBoard(boardId, model);
        boardService.fireBoardUpdateEvent();
        return response;
    }
}
