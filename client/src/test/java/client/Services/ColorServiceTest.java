package client.Services;

import client.user.UserData;
import commons.Board;
import commons.Color;
import commons.models.IdResponseModel;
import commons.sync.ColorAdded;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ColorServiceTest {
    @MockBean
    private UserData mockUserData = Mockito.mock(UserData.class);
    private ColorService colorService;

    private Board board;
    private Color color;
    private Color color1;
    private Color color2;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        colorService = new ColorService(mockUserData);
        this.board = Board.create("test", "psswrd", new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void addTaskColorTest(){
        Mockito.when(mockUserData.getCurrentBoard()).thenReturn(board);
        Mockito.when(mockUserData.updateBoard(Mockito.any(ColorAdded.class))).thenReturn(
                new IdResponseModel(4, null));

        IdResponseModel response = colorService.addTaskColor(javafx.scene.paint.Color.RED,
                javafx.scene.paint.Color.BLACK);

        Mockito.verify(mockUserData, Mockito.times(1)).updateBoard(
                Mockito.any(ColorAdded.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }
}