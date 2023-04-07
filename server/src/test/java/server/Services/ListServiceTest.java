package server.Services;

import commons.Board;
import commons.Color;
import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.BoardRepository;
import server.database.ListRepositoy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
class ListServiceTest {

    @MockBean
    private ListRepositoy mockListRepo;
    @MockBean
    private BoardRepository mockBoardRepo;
    @Autowired
    private ListService mockService = new ListService(mockListRepo, mockBoardRepo);

    private transient List list = new List("List 1", 1,
            new ArrayList<>(Arrays.asList(
                    new Task("No description", "Task 1", 1, new ArrayList<>()))));

    private transient List list2 = new List("List 2", 1,
            new ArrayList<>(Arrays.asList(
                    new Task("Watching series", "Task 2", 2, new ArrayList<>()))));
    private transient Board board = new Board("default", "1234",
            new ArrayList<>(), new ArrayList<>(
                    Arrays.asList(new Color("#000000", "#FFFFFF"))),
            new ArrayList<>());

    private Random random;
    private ListEditModel model;
    @BeforeEach
    void setup(){
        random=new Random();
        model= new ListEditModel("New list");
    }

    @Test
    void testGetById(){
        when(mockListRepo.getListByID(anyInt())).thenReturn(list);

        assertEquals(list, mockService.getListById(anyInt()));
        verify(mockListRepo, times(1)).getListByID(anyInt());
    }
    @Test
    void testGetAllLists(){
        when(mockListRepo.findAll()).thenReturn(Arrays.asList(list, list2));

        assertEquals(2, mockService.getAllLists().size());
        verify(mockListRepo, times(1)).findAll();
        assertEquals(list2, mockService.getAllLists().get(1));
    }
    @Test
    void testGetAllListByBoard(){
        board.getLists().add(list);
        board.getLists().add(list2);
        when(mockBoardRepo.getBoardByID(anyInt())).thenReturn(board);
        when(mockBoardRepo.existsById(anyInt())).thenReturn(true);

        assertEquals(2, mockService.getAllListByBoard(anyInt()).size());
        verify(mockBoardRepo, times(1)).getBoardByID(anyInt());
        assertEquals(list2, mockService.getAllListByBoard(anyInt()).get(1));
        verify(mockBoardRepo, times(2)).getBoardByID(anyInt());
    }

    @Test
    void getAllListByBoardFail(){
        when(mockBoardRepo.existsById(anyInt())).thenReturn(false);

        assertThrows(NoSuchElementException.class, ()->{
            mockService.getAllListByBoard(anyInt());
        } );
    }

    @Test
    void testAddList(){
        when(mockBoardRepo.getById(anyInt())).thenReturn(board);

        IdResponseModel test = mockService.addList(list, random.nextInt());
        assertEquals(list.getId(), test.getId());
        verify(mockListRepo, times(1)).save(list);
    }

    @Test
    void testAddListFails(){
        when(mockBoardRepo.getById(anyInt()))
                .thenThrow(new NoSuchElementException("You are a failure"));

        IdResponseModel test = mockService.addList(list, random.nextInt());
        verify(mockListRepo, times(0)).save(list);
        assertEquals("You are a failure", test.getErrorMessage());
    }
    @Test
    void testRemoveList(){
        when(mockBoardRepo.getById(anyInt())).thenReturn(board);
        when(mockListRepo.getById(anyInt())).thenReturn(list);

        int randomId = random.nextInt();
        IdResponseModel test = mockService.removeList(randomId, random.nextInt());
        verify(mockListRepo, times(1)).delete(list);
        assertEquals(randomId, test.getId());
    }
    @Test
    void testRemoveListFails(){
        when(mockListRepo.getById(anyInt())).thenThrow(new NoSuchElementException("Failure"));

        int randomId = random.nextInt();
        IdResponseModel test = mockService.removeList(randomId, random.nextInt());

        verify(mockListRepo, times(0)).delete(list);
        verify(mockBoardRepo, times(1)).getById(anyInt());
        assertEquals(-1, test.getId());
    }
    @Test
    void testEditList(){
        when(mockListRepo.getListByID(anyInt())).thenReturn(list);
        int randomBoardId = random.nextInt();
        int randomListId=random.nextInt();

        IdResponseModel test = mockService.editList(randomBoardId, randomListId, model);

        verify(mockListRepo, times(1)).save(list);
        assertEquals(randomListId, test.getId());
    }
    @Test
    void testEditListFails(){
        when(mockListRepo.getListByID(anyInt())).thenThrow(new NoSuchElementException());
        int randomBoardId = random.nextInt();
        int randomListId=random.nextInt();

        IdResponseModel test = mockService.editList(randomBoardId, randomListId, model);

        verify(mockListRepo, times(1)).getListByID(randomListId);
        verify(mockListRepo, times(0)).save(list);
        assertEquals(-1, test.getId());
    }
}