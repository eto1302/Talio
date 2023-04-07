package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Color;
import commons.models.IdResponseModel;
import commons.sync.ColorAdded;
import commons.sync.ColorDeleted;
import commons.sync.ColorEdited;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ColorServiceTest {
    @MockBean
    private UserData mockUserData = Mockito.mock(UserData.class);
    @MockBean
    private ServerUtils mockServerUtils = Mockito.mock(ServerUtils.class);
    private ColorService colorService;

    private Board board;
    private Color defaultColor;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        colorService = new ColorService(mockUserData, mockServerUtils);
    }

    @Test
    public void addTaskColorTest(){
        this.board = new Board("test", "psswrd", new ArrayList<>(),
                Arrays.asList(), new ArrayList<>());
        when(mockUserData.getCurrentBoard()).thenReturn(board);
        when(mockUserData.updateBoard(Mockito.any(ColorAdded.class))).thenReturn(
                new IdResponseModel(4, null));

        IdResponseModel response = colorService.addColor(javafx.scene.paint.Color.RED,
                javafx.scene.paint.Color.BLACK);

        verify(mockUserData, times(1)).updateBoard(
                Mockito.any(ColorAdded.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }
    @Test
    public void testEditColorDefault() {
        this.defaultColor = new Color("#FFFFFF", "#000000");
        this.defaultColor.setIsDefault(true);
        this.board = new Board("test", "psswrd", new ArrayList<>(),
                Arrays.asList(defaultColor), new ArrayList<>());
        when(mockUserData.getCurrentBoard()).thenReturn(board);
        when(mockUserData.updateBoard(any(ColorEdited.class))).thenReturn(
                new IdResponseModel(1, null));

        IdResponseModel response = colorService.editColor(1, javafx.scene.paint.Color.WHEAT,
                javafx.scene.paint.Color.BLACK, true);

        verify(mockUserData, times(2)).updateBoard(any(ColorEdited.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void testEditColorError() {
        this.defaultColor = new Color("#FFFFFF", "#000000");
        this.defaultColor.setIsDefault(true);
        this.board = new Board("test", "psswrd", new ArrayList<>(),
                Arrays.asList(defaultColor), new ArrayList<>());
        when(mockUserData.getCurrentBoard()).thenReturn(board);
        when(mockUserData.updateBoard(any(ColorEdited.class))).thenReturn(
                new IdResponseModel(-1, "Error"));

        IdResponseModel response = colorService.editColor(1, javafx.scene.paint.Color.WHEAT,
                javafx.scene.paint.Color.BLACK, true);

        verify(mockUserData, times(1)).updateBoard(any(ColorEdited.class));

        assertTrue(response.getId() == -1);
    }

    @Test
    public void testEditColorNotDefault() {
        this.board = new Board("test", "psswrd", new ArrayList<>(),
                Arrays.asList(), new ArrayList<>());
        when(mockUserData.getCurrentBoard()).thenReturn(board);
        when(mockUserData.updateBoard(any(ColorEdited.class))).thenReturn(
                new IdResponseModel(1, null));

        IdResponseModel response = colorService.editColor(1, javafx.scene.paint.Color.WHEAT,
                javafx.scene.paint.Color.BLACK, false);

        verify(mockUserData, times(1)).updateBoard(any(ColorEdited.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void testEditColorDefaultWithoutPreviousDefault() {
        Color color = new Color("#FFFFFF", "#000000");
        Color color1 = new Color("#FFFFFF", "#000000");
        this.board = new Board("test", "psswrd", new ArrayList<>(),
                Arrays.asList(color, color1, color1), new ArrayList<>());
        when(mockUserData.getCurrentBoard()).thenReturn(board);

        when(mockUserData.updateBoard(any(ColorEdited.class))).thenReturn(
                new IdResponseModel(1, null));

        IdResponseModel response = colorService.editColor(1, javafx.scene.paint.Color.WHEAT,
                javafx.scene.paint.Color.BLACK, true);

        verify(mockUserData, times(1)).updateBoard(any(ColorEdited.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void testDeleteColor() {
        Color color = new Color( "#000000", "#FFFFFF");
        this.board = new Board("test", "psswrd", new ArrayList<>(),
                Arrays.asList(), new ArrayList<>());
        when(mockUserData.getCurrentBoard()).thenReturn(board);


        when(mockUserData.updateBoard(any(ColorDeleted.class))).thenReturn(
                new IdResponseModel(1, null));

        IdResponseModel response = colorService.deleteColor(color);

        verify(mockUserData, times(1)).updateBoard(any(ColorDeleted.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void testDeleteColorError() {
        this.defaultColor = new Color("#FFFFFF", "#000000");
        this.defaultColor.setIsDefault(true);
        this.board = new Board("test", "psswrd", new ArrayList<>(),
                Arrays.asList(), new ArrayList<>());
        when(mockUserData.getCurrentBoard()).thenReturn(board);


        when(mockUserData.updateBoard(any(ColorDeleted.class))).thenReturn(
                new IdResponseModel(-1, "Cannot delete default color"));

        IdResponseModel response = colorService.deleteColor(this.defaultColor);

        verify(mockUserData, times(0)).updateBoard(any(ColorDeleted.class));

        assertTrue(response.getId() == -1);
        assertEquals("Cannot delete default color", response.getErrorMessage());
    }

    @Test
    public void getColorTest(){
        this.defaultColor = new Color("#FFFFFF", "#000000");
        when(mockServerUtils.getColor(any(Integer.class))).thenReturn(defaultColor);

        Color actual = colorService.getColor(1);
        verify(mockServerUtils, times(1)).getColor(any(Integer.class));
        assertEquals(actual, defaultColor);
    }
}