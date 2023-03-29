package server.Services;

import commons.Board;
import commons.Color;
import commons.models.IdResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.BoardRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class BoardServiceTest {

    @MockBean
    private transient BoardRepository mockBoardRepository;

    @Autowired
    private transient BoardService boardService = new BoardService(mockBoardRepository);

    private transient Board board = Board.create("name", "pwd",
            null,
            Color.create("#000000", "#FFFFFF"),
            Color.create("#000000", "#FFFFFF"));

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
}
