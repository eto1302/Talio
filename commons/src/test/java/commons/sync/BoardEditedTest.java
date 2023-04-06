package commons.sync;

import commons.*;
import commons.mocks.IServerUtils;
import commons.mocks.IShowCtrl;
import commons.mocks.IUserData;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardEditedTest {
    private BoardEdited boardEdited;
    @MockBean
    private IUserData mockUserData = mock(IUserData.class);
    @MockBean
    private IServerUtils mockServerUtils = mock(IServerUtils.class);

    private Board board;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
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
        when(mockUserData.getShowCtrl()).thenReturn(new IShowCtrl() {
            @Override
            public Object addList(List list) {return null;}
            @Override
            public void cancel(){}
            @Override
            public void editList(List list) {}
            @Override
            public void deleteList(List list) {}
            @Override
            public void addTask(Task task, List list) {}
            @Override
            public void deleteTask(Task task) {}
            @Override
            public void refreshAdminBoards() {}
            @Override
            public void refreshBoardCtrl() {}
            @Override
            public Object addTaskColor(Color color) {return null;}
            @Override
            public void deleteTaskColor(Color color) {}
            @Override
            public void editColor(Color color) {}
            @Override
            public void showBoard() {}
            @Override
            public void showColorPicker() {}
            @Override
            public void addTagToTask(Tag tag, Task task) {}
            @Override
            public void addTag(Tag tag) {}
            @Override
            public void deleteTag(Tag tag) {}
            @Override
            public void editTag(Tag tag) {}
            @Override
            public void removeTagFromTask(Tag tag, Task task) {}
            @Override
            public boolean isColorPickerOpen(){
                return false;
            }
        });

        this.boardEdited.apply(mockUserData);

        verify(mockUserData, times(2)).getCurrentBoard();
        verify(mockUserData, times(1)).getShowCtrl();
        assertEquals("test", board.getName());
    }

}
