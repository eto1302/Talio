package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Color;
import commons.models.IdResponseModel;
import commons.sync.BoardDeleted;
import commons.sync.BoardEdited;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class BoardServiceTest {
    @MockBean
    private UserData mockUserData = mock(UserData.class);
    @MockBean
    private ServerUtils mockServerUtils = mock(ServerUtils.class);
    private BoardService boardService;
    private Board board;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        boardService = new BoardService(mockUserData, mockServerUtils);
        this.board = Board.create("test", "pass", new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
    }
    @Test
    public void addBoardTest(){
        when(mockServerUtils.addColor(Mockito.any(Color.class)))
                .thenReturn(new IdResponseModel(1, null));
        when(mockServerUtils.addBoard(Mockito.any(Board.class)))
                .thenReturn(new IdResponseModel(1, null));
        when(mockServerUtils.setColorToBoard(Mockito.any(Color.class), Mockito.any(Integer.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.boardService.addBoard("Test");

        verify(mockServerUtils, times(2)).addColor(
                Mockito.any(Color.class));
        verify(mockServerUtils, times(1)).addBoard(
                Mockito.any(Board.class));
        verify(mockServerUtils, times(2))
                .setColorToBoard(Mockito.any(Color.class), Mockito.any(Integer.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }
    @Test
    public void addBoardBoardColorErrorTest(){
        when(mockServerUtils.addColor(Mockito.any(Color.class)))
                .thenReturn(new IdResponseModel(-1, "Error"));
        when(mockServerUtils.addBoard(Mockito.any(Board.class)))
                .thenReturn(new IdResponseModel(1, null));
        when(mockServerUtils.setColorToBoard(Mockito.any(Color.class), Mockito.any(Integer.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.boardService.addBoard("Test");

        verify(mockServerUtils, times(1)).addColor(
                Mockito.any(Color.class));

        assertTrue(response.getId() == -1);
        assertNotNull(response.getErrorMessage());
    }
    @Test
    public void addBoardBoardErrorTest(){
        when(mockServerUtils.addColor(Mockito.any(Color.class)))
                .thenReturn(new IdResponseModel(1, null));
        when(mockServerUtils.addBoard(Mockito.any(Board.class)))
                .thenReturn(new IdResponseModel(-1, "Error"));
        when(mockServerUtils.setColorToBoard(Mockito.any(Color.class), Mockito.any(Integer.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.boardService.addBoard("Test");

        verify(mockServerUtils, times(2)).addColor(
                Mockito.any(Color.class));
        verify(mockServerUtils, times(1)).addBoard(
                Mockito.any(Board.class));

        assertTrue(response.getId() == -1);
        assertNotNull(response.getErrorMessage());
    }
    @Test
    public void addBoardSetColorErrorTest(){
        when(mockServerUtils.addColor(Mockito.any(Color.class)))
                .thenReturn(new IdResponseModel(1, null));
        when(mockServerUtils.addBoard(Mockito.any(Board.class)))
                .thenReturn(new IdResponseModel(1, null));
        when(mockServerUtils.setColorToBoard(Mockito.any(Color.class), Mockito.any(Integer.class)))
                .thenReturn(new IdResponseModel(-1, "Error"));

        IdResponseModel response = this.boardService.addBoard("Test");

        verify(mockServerUtils, times(2)).addColor(
                Mockito.any(Color.class));
        verify(mockServerUtils, times(1)).addBoard(
                Mockito.any(Board.class));
        verify(mockServerUtils, times(1))
                .setColorToBoard(Mockito.any(Color.class), Mockito.any(Integer.class));

        assertTrue(response.getId() == -1);
        assertNotNull(response.getErrorMessage());
    }
    @Test
    public void getBoardTest(){
        when(mockServerUtils.getBoard(Mockito.any(Integer.class)))
                .thenReturn(board);

        Board actual = this.boardService.getBoard(1);

        verify(mockServerUtils, times(1)).getBoard(
                Mockito.any(Integer.class));

        assertEquals(board, actual);
    }
    @Test
    public void getCurrentBoardTest(){
        when(mockUserData.getCurrentBoard())
                .thenReturn(board);

        Board actual = this.boardService.getCurrentBoard();

        verify(mockUserData, times(1)).getCurrentBoard();

        assertEquals(board, actual);
    }
    @Test
    public void refreshTest(){
        this.boardService.refresh();

        verify(mockUserData, times(1)).refresh();
    }
    @Test
    public void enterTest(){
        when(mockUserData.openBoard(any(Integer.class))).thenReturn(board);
        this.boardService.enterBoard(1);

        verify(mockUserData, times(1)).openBoard(any(Integer.class));
        verify(mockUserData, times(1)).saveToDisk();
    }
    @Test
    public void leaveTest(){
        this.boardService.leaveBoard(1);

        verify(mockUserData, times(1)).leaveBoard(any(Integer.class));
        verify(mockUserData, times(1)).saveToDisk();
    }
    @Test
    public void copyInviteTest(){
        when(mockServerUtils.getBoard(any(Integer.class))).thenReturn(board);
        this.boardService.copyInviteLink(1);

        verify(mockServerUtils, times(1)).getBoard(any(Integer.class));
    }
    @Test
    public void searchMissingTest(){
        when(mockServerUtils.getBoardByInviteKey(any(String.class))).thenReturn(null);
        IdResponseModel response = this.boardService.search("testing");

        verify(mockServerUtils, times(1)).getBoardByInviteKey(
                any(String.class));
    }
    @Test
    public void searchTest(){
        when(mockServerUtils.getBoardByInviteKey(any(String.class))).thenReturn(board);
        when(mockUserData.openBoard(any(Integer.class))).thenReturn(board);
        IdResponseModel response = this.boardService.search("testing");

        verify(mockServerUtils, times(1)).getBoardByInviteKey(
                any(String.class));
        verify(mockUserData, times(1)).joinBoard(any(Integer.class),
                any(String.class));
        verify(mockUserData, times(1)).saveToDisk();
        verify(mockUserData, times(1)).openBoard(any(Integer.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }
    @Test
    public void editTest(){
        when(mockUserData.updateBoard(any(BoardEdited.class))).thenReturn(
                new IdResponseModel(1, null));
        when(mockUserData.openBoard(any(Integer.class))).thenReturn(board);
        when(mockUserData.getCurrentBoard()).thenReturn(board);

        IdResponseModel response = this.boardService.editBoard("new");

        verify(mockUserData, times(1)).updateBoard(any(BoardEdited.class));
        verify(mockUserData, times(1)).openBoard(any(Integer.class));
        verify(mockUserData, times(2)).getCurrentBoard();
    }
    @Test
    public void deleteTest(){
        when(mockUserData.deleteBoard(any(BoardDeleted.class))).thenReturn(
                new IdResponseModel(1, null));
        when(mockUserData.getCurrentBoard()).thenReturn(board);

        IdResponseModel response = this.boardService.delete();

        verify(mockUserData, times(1)).getCurrentBoard();
        verify(mockUserData, times(1)).leaveBoard(any(Integer.class));
        verify(mockUserData, times(1)).saveToDisk();
        verify(mockUserData, times(1)).deleteBoard(any(BoardDeleted.class));
    }
}
