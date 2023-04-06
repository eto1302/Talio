package server.Services;

import commons.Board;
import commons.Color;
import commons.models.ColorEditModel;
import commons.models.IdResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.BoardRepository;
import server.database.ColorRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ColorServiceTest {

    @MockBean
    private ColorRepository mockColorRepo;
    @MockBean
    private BoardRepository mockBoardRepo;

    @Autowired
    private ColorService mockService = new ColorService(mockColorRepo, mockBoardRepo);

    private transient Color color = Color.create("#101010", "#FF10FF");
    private transient Color color2 = Color.create("#102012", "#FF10DD");
    private transient Board board = Board.create("name", "pwd",
            null, new ArrayList<>(), new ArrayList<>());
    private ColorEditModel model;

    @BeforeEach
    void setup(){
        model = new ColorEditModel("#444444", "#222222", false);
    }

    @Test
    void testSaveColor(){
        IdResponseModel test = mockService.saveColor(color);
        assertEquals(color.getId(), test.getId());
        assertNull(test.getErrorMessage());

        verify(mockColorRepo, times(1)).save(color);
    }

    @Test
    void testSaveColorFails(){
        when(mockColorRepo.save(color)).thenThrow(new RuntimeException("Failed"));

        IdResponseModel test = mockService.saveColor(color);
        assertEquals(-1, test.getId());
        assertEquals("Failed", test.getErrorMessage());
    }

    @Test
    void testDeleteColor(){
        when(mockColorRepo.getById(anyInt())).thenReturn(color);
        when(mockBoardRepo.getById(anyInt())).thenReturn(board);

        Random random = new Random();
        int randomIdBoard = random.nextInt(100);
        int randomIdColor=random.nextInt(100);
        IdResponseModel test = mockService.deleteColor(randomIdBoard, randomIdColor);

        assertEquals(randomIdColor, test.getId());
        assertNull(test.getErrorMessage());

        verify(mockBoardRepo, times(1)).getById(anyInt());
        verify(mockColorRepo, times(1)).getById(anyInt());
        verify(mockColorRepo, times((1))).delete(color);
    }

    @Test
    void testDeleteColorFail(){
        when(mockBoardRepo.getById(anyInt())).thenThrow(new NoSuchElementException("Failed"));

        IdResponseModel test = mockService.deleteColor(1, 1);
        assertEquals(-1, test.getId());
        assertEquals("Failed", test.getErrorMessage());
        verify(mockColorRepo, times(0)).getById(1);
        verify(mockBoardRepo, times(1)).getById(anyInt());
    }

    @Test
    void testGetColorById(){
        when(mockColorRepo.getById(anyInt())).thenReturn(color);

        assertEquals(color, mockService.getColorById(anyInt()));
    }

    @Test
    void testGetAllColors(){
        when(mockColorRepo.findAll()).thenReturn(Arrays.asList(color, color2));

        List<Color> list = mockService.getAllColors();
        assertEquals(2, list.size());
        assertEquals(color, list.get(0));
        verify(mockColorRepo, times(1)).findAll();
    }

    @Test
    void testSetToBoard(){
        Random random = new Random();
        int randomIdColor=random.nextInt(100);
        color.setId(randomIdColor);
        int randomIdBoard = random.nextInt(100);

        when(mockColorRepo.getById(anyInt())).thenReturn(color);
        when(mockBoardRepo.getById(anyInt())).thenReturn(board);

        IdResponseModel test = mockService.setToBoard(randomIdColor, randomIdBoard);

        assertEquals(randomIdColor, test.getId());
        verify(mockColorRepo, times(1)).save(color);
    }

    @Test
    void testSetToBoardFails(){
        when(mockColorRepo.getById(anyInt())).thenThrow(new NoSuchElementException("Failed"));

        Random random = new Random();
        int randomIdBoard = random.nextInt(100);
        int randomIdColor=random.nextInt(100);
        IdResponseModel test = mockService.setToBoard(randomIdColor, randomIdBoard);

        assertEquals(-1, test.getId());
        assertEquals("Failed", test.getErrorMessage());
        verify(mockColorRepo, times(0)).save(color);
        verify(mockColorRepo, times(1)).getById(anyInt());
    }

    @Test
    void testEditColor(){
        when(mockColorRepo.getById(anyInt())).thenReturn(color);

        Random random = new Random();
        int randomIdColor=random.nextInt(100);
        IdResponseModel test = mockService.editColor(randomIdColor, model);

        assertEquals(randomIdColor, test.getId());
        verify(mockColorRepo, times(1)).save(color);
    }

    @Test
    void testEditColorFails(){
        when(mockColorRepo.getById(anyInt())).thenThrow(new NoSuchElementException());

        Random random = new Random();
        int randomIdColor=random.nextInt(100);
        IdResponseModel test = mockService.editColor(randomIdColor, model);

        assertEquals(-1, test.getId());
        verify(mockColorRepo, times(0)).save(color);
    }
}