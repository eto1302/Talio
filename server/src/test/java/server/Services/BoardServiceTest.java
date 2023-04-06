package server.Services;

import commons.Board;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BoardServiceTest {

    @MockBean
    private transient BoardRepository mockBoardRepository;

    @Autowired
    private transient BoardService boardService = new BoardService(mockBoardRepository);

    private transient Board board = Board.create("name", "pwd",
            null, new ArrayList<>(), new ArrayList<>());

    @Test
    void saveBoard() {
        IdResponseModel model = boardService.saveBoard(board);

        assertNull(model.getErrorMessage());
        assertEquals(board.getId(), model.getId());
        verify(mockBoardRepository, times(1)).save(board);
    }

    @Test
    void saveBoardFails() {
        when(mockBoardRepository.save(board)).thenThrow(new RuntimeException("message"));
        IdResponseModel model = boardService.saveBoard(board);

        assertEquals(-1, model.getId());
        assertEquals("message", model.getErrorMessage());
    }

    @Test
    void testDeleteBoard() {
        int boardId = board.getId();
        when(mockBoardRepository.existsById(boardId)).thenReturn(true);
        IdResponseModel response = boardService.deleteBoard(boardId);

        verify(mockBoardRepository, times(1)).deleteById(boardId);
        assertEquals(boardId, response.getId());
        assertNull(response.getErrorMessage());
    }

    @Test
    void testDeleteBoardError() {
        int boardId = board.getId();
        when(mockBoardRepository.existsById(boardId)).thenReturn(false);
        IdResponseModel response = boardService.deleteBoard(boardId);
        verify(mockBoardRepository, times(0)).deleteById(boardId);

        assertEquals(-1, response.getId());
        assertNotNull(response.getErrorMessage());
    }

    @Test
    void testGetBoardById() {
        when(mockBoardRepository.getBoardByID(1)).thenReturn(board);
        Board resultBoard = boardService.getBoardById(1);

        assertEquals(board, resultBoard);
    }

    @Test
    void testGetAllBoards() {
        Board board2 = new Board();
        List<Board> boards = new ArrayList<>();
        boards.add(board);
        boards.add(board2);
        when(mockBoardRepository.findAll()).thenReturn(boards);
        List<Board> resultBoards = boardService.getAllBoards();

        assertEquals(boards, resultBoards);
    }

    @Test
    void testGetBoardByInviteKey() {
        String inviteKey = "testKey";
        board.setInviteKey(inviteKey);
        when(mockBoardRepository.getBoardByInviteKey(inviteKey)).thenReturn(board);

        Board result = boardService.getBoardByInviteKey(inviteKey);

        assertNotNull(result);
        assertEquals(board, result);
    }

    @Test
    void testEditBoard() {
        int boardId = board.getId();
        String name = "New Name";
        String password = "newPassword";
        BoardEditModel model = new BoardEditModel();
        model.setName(name);
        model.setPassword(password);
        board.setName("Old Name");
        board.setPassword("oldPassword");

        when(mockBoardRepository.getBoardByID(boardId)).thenReturn(board);
        IdResponseModel result = boardService.editBoard(boardId, model);

        assertNotNull(result);
        assertEquals(boardId, result.getId());
        assertNull(result.getErrorMessage());
        assertEquals(name, board.getName());
        assertEquals(password, board.getPassword());
        verify(mockBoardRepository, times(1)).save(board);
    }
}
