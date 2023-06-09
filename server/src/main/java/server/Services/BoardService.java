package server.Services;

import commons.Board;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

@Service
public class BoardService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private BoardRepository boardRepository;
    private String adminPassword;
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    /**
     * Create a board and save it in the database.
     *
     * @param board the content of the board
     * @return the id of the board, or the error message if creation fails.
     */
    public IdResponseModel saveBoard(Board board) {
        try {
            boardRepository.save(board);
            return new IdResponseModel(board.getId(), null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public IdResponseModel deleteBoard(int boardId) {
        try {
            if (!boardRepository.existsById(boardId))
                return new IdResponseModel(-1, "Board does not exist");
            boardRepository.deleteById(boardId);
            return new IdResponseModel(boardId, null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public boolean verifyAdminPassword(String password){
        return password.equals(adminPassword);
    }

    public Board getBoardById(int id){
        return boardRepository.getBoardByID(id);
    }

    public java.util.List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    /**
     * Gets the board with the corresponding invite key
     * @param inviteKey the invite key of the board
     * @return the board
     */
    public Board getBoardByInviteKey(String inviteKey){
        return boardRepository.getBoardByInviteKey(inviteKey);
    }

    public IdResponseModel editBoard(int boardId, BoardEditModel model) {
        try {
            Board board = boardRepository.getBoardByID(boardId);
            board.setName(model.getName());
            board.setPassword(model.getPassword());
            boardRepository.save(board);
            return new IdResponseModel(boardId, null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public void fireBoardUpdateEvent() {
        BoardsUpdatedEvent boardsUpdatedEvent = new BoardsUpdatedEvent(this, null);
        applicationEventPublisher.publishEvent(boardsUpdatedEvent);
    }
}
