package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.List;
import commons.models.IdResponseModel;
import commons.sync.ListAdded;
import commons.sync.ListDeleted;
import commons.sync.ListEdited;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ListServiceTest {
    @MockBean
    private UserData mockUserData = mock(UserData.class);
    @MockBean
    private ServerUtils mockServerUtils = mock(ServerUtils.class);
    private ListService listService;
    private Board board;
    private List list;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        listService = new ListService(mockUserData, mockServerUtils);
        board = Board.create("test", "pass", new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
        list = List.create("test", 1, new ArrayList<>());
    }

    @Test
    public void addListTest(){
        when(mockUserData.getCurrentBoard())
                .thenReturn(board);
        when(mockUserData.updateBoard(any(ListAdded.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.listService.addList("test");

        verify(mockUserData, times(2)).getCurrentBoard();
        verify(mockUserData, times(1)).updateBoard(
                Mockito.any(ListAdded.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void editListTest(){
        when(mockUserData.updateBoard(any(ListEdited.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.listService.editList(new List(), "test");

        verify(mockUserData, times(1)).updateBoard(
                Mockito.any(ListEdited.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }
    @Test
    public void deleteListTest(){
        when(mockUserData.updateBoard(any(ListDeleted.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.listService.deleteList(new List());

        verify(mockUserData, times(1)).updateBoard(
                Mockito.any(ListDeleted.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }
    @Test
    public void getListTest(){
        when(mockServerUtils.getList(any(Integer.class))).thenReturn(list);

        List actual = this.listService.getList(1);

        verify(mockServerUtils, times(1)).getList(
                Mockito.any(Integer.class));

        assertEquals(list, actual);
    }

}
