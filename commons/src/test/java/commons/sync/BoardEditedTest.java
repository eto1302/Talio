package commons.sync;

import commons.Board;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardEditedTest {
    private BoardEdited boardEdited;
    private IUserData mockUserData = mock(IUserData.class);
    private IServerUtils mockServerUtils = mock(IServerUtils.class);

    private Board board;

    @BeforeEach
    void setup(){
        this.boardEdited = new BoardEdited(1,
                new BoardEditModel("test", "password"));
        this.board = Board.create("test123", "pass", new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void getBoardIdTest(){
        assertEquals(1, this.boardEdited.getBoardID());
    }

    @Test
    public void getEditTest(){
        assertEquals(new BoardEditModel("test", "password"), this.boardEdited.getEdit());
    }

    @Test
    public void sendToServerTest(){
        when(mockServerUtils.editBoard(any(Integer.class), any(BoardEditModel.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.boardEdited.sendToServer(mockServerUtils);

        verify(mockServerUtils, times(1))
                .editBoard(any(Integer.class), any(BoardEditModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void applyTest(){
        when(mockUserData.getCurrentBoard()).thenReturn(board);

        this.boardEdited.apply(mockUserData);

        verify(mockUserData, times(1)).getCurrentBoard();
        assertEquals("test", board.getName());
    }

}
