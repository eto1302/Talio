package client.utils;

import commons.*;
import commons.Tag;
import commons.models.*;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServerUtilsTest {

    @MockBean
    private RestTemplate mockClient = mock(RestTemplate.class);

    private ServerUtils serverUtils;
    private RestClientException exception;
    private String url = "http://localhost:8080/";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.serverUtils = new ServerUtils(mockClient);
        this.serverUtils.setUrl(url);
        this.exception = new RestClientException("exception");
    }

    @Test
    public void addBoardTest() {
        when(mockClient.postForObject(eq(url + "board/create"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.serverUtils.addBoard(new Board());

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void addBoardExceptionTest() {
        when(mockClient.postForObject(eq(url + "board/create"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.addBoard(new Board());

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void getBoardTest() {
        when(mockClient.getForEntity(eq(url + "board/find/1"), eq(Board.class)))
                .thenReturn(new ResponseEntity<Board>(new Board(), HttpStatus.OK));

        Board board = this.serverUtils.getBoard(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Board.class));
        assertEquals(new Board(), board);
    }

    @Test
    public void getBoardExceptionTest() {
        when(mockClient.getForEntity(eq(url + "board/find/1"), eq(Board.class)))
                .thenThrow(exception);

        Board board = this.serverUtils.getBoard(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Board.class));
        assertNull(board);
    }

    @Test
    public void deleteBoardTest() {
        when(mockClient.exchange(eq(url + "board/delete/1"), eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<IdResponseModel>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.deleteBoard(1);

        verify(mockClient, times(1)).exchange(any(String.class), eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void deleteBoardExceptionTest() {
        when(mockClient.exchange(eq(url + "board/delete/1"), eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.deleteBoard(1);

        verify(mockClient, times(1)).exchange(any(String.class), eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void getColorTest() {
        when(mockClient.getForEntity(eq(url + "color/find/1"), eq(Color.class)))
                .thenReturn(new ResponseEntity<Color>(new Color(), HttpStatus.OK));

        Color color = this.serverUtils.getColor(1);

        verify(mockClient, times(1))
                .getForEntity(eq(url + "color/find/1"), eq(Color.class));
        assertEquals(new Color(), color);
    }

    @Test
    public void getColorExceptionTest() {
        when(mockClient.getForEntity(eq(url + "color/find/1"), eq(Color.class)))
                .thenThrow(exception);

        Color color = this.serverUtils.getColor(1);

        verify(mockClient, times(1))
                .getForEntity(eq(url + "color/find/1"), eq(Color.class));
        assertNull(color);
    }

    @Test
    public void editBoardWrongIdTest() {
        HttpEntity<BoardEditModel> req = new HttpEntity<>(new BoardEditModel());
        when(mockClient.exchange(eq(url + "board/edit/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(2, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editBoard(1, new BoardEditModel());

        verify(mockClient, times(1)).exchange(eq(url + "board/edit/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Board doesn't match", response.getErrorMessage());
    }

    @Test
    public void editBoardTest() {
        HttpEntity<BoardEditModel> req = new HttpEntity<>(new BoardEditModel());
        when(mockClient.exchange(eq(url + "board/edit/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editBoard(1, new BoardEditModel());

        verify(mockClient, times(1)).exchange(eq(url + "board/edit/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void editBoardExceptionTest() {
        HttpEntity<BoardEditModel> req = new HttpEntity<>(new BoardEditModel());
        when(mockClient.exchange(eq(url + "board/edit/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.editBoard(1, new BoardEditModel());

        verify(mockClient, times(1)).exchange(eq(url + "board/edit/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void deleteColorTest() {
        when(mockClient.exchange(eq(url + "color/delete/" + 1 + "/" + 1),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<IdResponseModel>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.deleteColor(1, 1);

        verify(mockClient, times(1)).exchange(
                eq(url + "color/delete/" + 1 + "/" + 1),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void deleteColorExceptionTest() {
        when(mockClient.exchange(eq(url + "color/delete/" + 1 + "/" + 1),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenThrow(new RestClientException("test"));

        IdResponseModel response = this.serverUtils.deleteColor(1, 1);

        verify(mockClient, times(1)).exchange(
                eq(url + "color/delete/" + 1 + "/" + 1),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void addColorTest() {
        when(mockClient.postForObject(eq(url + "color/add"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.serverUtils.addColor(new Color());

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void addColorExceptionTest() {
        when(mockClient.postForObject(eq(url + "color/add"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.addColor(new Color());

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void setColorToBoardTest() {
        HttpEntity<commons.Color> req = new HttpEntity<Color>(new Color());
        when(mockClient.exchange(eq(url + "color/add/0/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.setColorToBoard(new Color(), 1);

        verify(mockClient, times(1)).exchange(eq(url + "color/add/0/1"),
                eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void setColorToBoardExceptionTest() {
        HttpEntity<commons.Color> req = new HttpEntity<Color>(new Color());
        when(mockClient.exchange(eq(url + "color/add/0/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.setColorToBoard(new Color(), 1);

        verify(mockClient, times(1)).exchange(eq(url + "color/add/0/1"),
                eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void editColorWrongIdTest() {
        HttpEntity<ColorEditModel> req = new HttpEntity<>(new ColorEditModel(
                "#FFFFFF", "#000000", true));
        when(mockClient.exchange(eq(url + "color/edit/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(2, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editColor(1, new ColorEditModel(
                "#FFFFFF", "#000000", true));

        verify(mockClient, times(1)).exchange(eq(url + "color/edit/1"),
                eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Color doesn't match", response.getErrorMessage());
    }

    @Test
    public void editColorTest() {
        HttpEntity<ColorEditModel> req = new HttpEntity<>(new ColorEditModel(
                "#FFFFFF", "#000000", true));
        when(mockClient.exchange(eq(url + "color/edit/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editColor(1, new ColorEditModel(
                "#FFFFFF", "#000000", true));

        verify(mockClient, times(1)).exchange(eq(url + "color/edit/1"),
                eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void editColorExceptionTest() {
        HttpEntity<ColorEditModel> req = new HttpEntity<>(new ColorEditModel(
                "#FFFFFF", "#000000", true));
        when(mockClient.exchange(eq(url + "color/edit/1"), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.editColor(1, new ColorEditModel(
                "#FFFFFF", "#000000", true));

        verify(mockClient, times(1)).exchange(eq(url + "color/edit/1"),
                eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void getBoardByInviteKeyTest() {
        when(mockClient.getForEntity(eq(url + "board/getByInvite/aaaaaaaaaaaaaaa"),
                eq(Board.class)))
                .thenReturn(new ResponseEntity<Board>(new Board(), HttpStatus.OK));

        Board board = this.serverUtils.getBoardByInviteKey("aaaaaaaaaaaaaaa");

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Board.class));
        assertEquals(new Board(), board);
    }

    @Test
    public void getBoardByInviteKeyExceptionTest() {
        when(mockClient.getForEntity(eq(url + "board/getByInvite/aaaaaaaaaaaaaaa"),
                eq(Board.class)))
                .thenThrow(exception);

        Board board = this.serverUtils.getBoardByInviteKey("aaaaaaaaaaaaaaa");

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Board.class));
        assertNull(board);
    }

    @Test
    public void getAllBoardsTest() {
        Board[] expected = new Board[2];
        when(mockClient.getForEntity(eq(url + "board/findAll"),
                eq(Board[].class)))
                .thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));

        Board[] boards = this.serverUtils.getAllBoards();

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Board[].class));
        assertEquals(expected, boards);
    }

    @Test
    public void getAllBoardsExceptionTest() {
        when(mockClient.getForEntity(eq(url + "board/findAll"),
                eq(Board[].class)))
                .thenThrow(exception);
        Board[] boards = this.serverUtils.getAllBoards();

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Board[].class));
        assertNull(boards);
    }

    @Test
    public void getBoardsUpdatedTest() {
        Board[] expected = new Board[2];
        when(mockClient.getForEntity(eq(url + "board/findAllUpdated"),
                eq(Board[].class)))
                .thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));

        Board[] boards = this.serverUtils.getBoardsUpdated();

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Board[].class));
        assertEquals(expected, boards);
    }

    @Test
    public void getBoardsUpdatedExceptionTest() {
        when(mockClient.getForEntity(eq(url + "board/findAllUpdated"),
                eq(Board[].class)))
                .thenThrow(exception);
        Board[] boards = this.serverUtils.getBoardsUpdated();

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Board[].class));
        assertNull(boards);
    }

    @Test
    public void verifyAdminTest() {
        when(mockClient.getForEntity(eq(url + "board/verify/aaa"),
                eq(Boolean.class)))
                .thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        boolean actual = this.serverUtils.verifyAdmin("aaa");

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Boolean.class));
        assertEquals(true, actual);
    }

    @Test
    public void verifyAdminExceptionTest() {
        when(mockClient.getForEntity(eq(url + "board/verify/aaa"),
                eq(Boolean.class)))
                .thenThrow(exception);

        boolean actual = this.serverUtils.verifyAdmin("aaa");

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Boolean.class));
        assertFalse(actual);
    }

    @Test
    public void addListTest() {
        when(mockClient.postForObject(eq(url + "list/add/1"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.serverUtils.addList(new List(), 1);

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void addListExceptionTest() {
        when(mockClient.postForObject(eq(url + "list/add/1"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.addList(new List(), 1);

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void deleteListTest() {
        when(mockClient.exchange(eq(url + "list/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<IdResponseModel>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.deleteList(1, 1);

        verify(mockClient, times(1)).exchange(
                eq(url + "list/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void deleteListExceptionTest() {
        when(mockClient.exchange(eq(url + "list/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenThrow(new RestClientException("test"));

        IdResponseModel response = this.serverUtils.deleteList(1, 1);

        verify(mockClient, times(1)).exchange(
                eq(url + "list/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void editListWrongIdTest() {
        HttpEntity<ListEditModel> req = new HttpEntity<>(new ListEditModel());
        when(mockClient.exchange(eq(url + "list/edit/" + 1 + "/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(2, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editList(1, 1, new ListEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "list/edit/" + 1 + "/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("List doesn't match", response.getErrorMessage());
    }

    @Test
    public void editListTest() {
        HttpEntity<ListEditModel> req = new HttpEntity<>(new ListEditModel());
        when(mockClient.exchange(eq(url + "list/edit/" + 1 + "/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editList(1, 1, new ListEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "list/edit/" + 1 + "/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void editListExceptionTest() {
        HttpEntity<ListEditModel> req = new HttpEntity<>(new ListEditModel());
        when(mockClient.exchange(eq(url + "list/edit/" + 1 + "/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.editList(1, 1, new ListEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "list/edit/" + 1 + "/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void getListTest() {
        when(mockClient.getForEntity(eq(url + "list/get/1"), eq(List.class)))
                .thenReturn(new ResponseEntity<>(new List(), HttpStatus.OK));

        List list = this.serverUtils.getList(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(List.class));
        assertEquals(new List(), list);
    }

    @Test
    public void getListExceptionTest() {
        when(mockClient.getForEntity(eq(url + "list/get/1"), eq(List.class)))
                .thenThrow(exception);

        List list = this.serverUtils.getList(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(List.class));
        assertNull(list);
    }

    @Test
    public void addTaskTest() {
        when(mockClient.postForObject(eq(url + "task/add/1"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.serverUtils.addTask(new Task(), 1);

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void addTaskExceptionTest() {
        when(mockClient.postForObject(eq(url + "task/add/1"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.addTask(new Task(), 1);

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void deleteTaskTest() {
        when(mockClient.exchange(eq(url + "task/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<IdResponseModel>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.deleteTask(1, 1);

        verify(mockClient, times(1)).exchange(
                eq(url + "task/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void deleteTaskExceptionTest() {
        when(mockClient.exchange(eq(url + "task/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenThrow(new RestClientException("test"));

        IdResponseModel response = this.serverUtils.deleteTask(1, 1);

        verify(mockClient, times(1)).exchange(
                eq(url + "task/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void editTaskWrongIdTest() {
        HttpEntity<TaskEditModel> req = new HttpEntity<>(new TaskEditModel());
        when(mockClient.exchange(eq(url + "task/edit/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(2, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editTask(1, new TaskEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "task/edit/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Task doesn't match", response.getErrorMessage());
    }

    @Test
    public void editTaskTest() {
        HttpEntity<TaskEditModel> req = new HttpEntity<>(new TaskEditModel());
        when(mockClient.exchange(eq(url + "task/edit/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editTask(1, new TaskEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "task/edit/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void editTaskExceptionTest() {
        HttpEntity<TaskEditModel> req = new HttpEntity<>(new TaskEditModel());
        when(mockClient.exchange(eq(url + "task/edit/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.editTask(1, new TaskEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "task/edit/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void getTaskTest() {
        when(mockClient.getForEntity(eq(url + "task/get/1"), eq(Task.class)))
                .thenReturn(new ResponseEntity<>(new Task(), HttpStatus.OK));

        Task task = this.serverUtils.getTask(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Task.class));
        assertEquals(new Task(), task);
    }

    @Test
    public void getTaskExceptionTest() {
        when(mockClient.getForEntity(eq(url + "task/get/1"), eq(Task.class)))
                .thenThrow(exception);

        Task task = this.serverUtils.getTask(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Task.class));
        assertNull(task);
    }

    @Test
    public void addSubtaskTest() {
        when(mockClient.postForObject(eq(url + "subtask/add/1"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.serverUtils.addSubtask(new Subtask(), 1);

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void addSubtaskExceptionTest() {
        when(mockClient.postForObject(eq(url + "subtask/add/1"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenThrow(new RestClientException("exception"));

        IdResponseModel response = this.serverUtils.addSubtask(new Subtask(), 1);

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void getSubtaskTest() {
        when(mockClient.getForEntity(eq(url + "subtask/get/1"), eq(Subtask.class)))
                .thenReturn(new ResponseEntity<>(new Subtask(), HttpStatus.OK));

        Subtask subtask = this.serverUtils.getSubtask(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Subtask.class));
        assertEquals(new Subtask(), subtask);
    }

    @Test
    public void getSubtaskExceptionTest() {
        when(mockClient.getForEntity(eq(url + "subtask/get/1"), eq(Subtask.class)))
                .thenThrow(new RestClientException("exception"));

        Subtask subtask = this.serverUtils.getSubtask(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Subtask.class));
        assertNull(subtask);
    }

    @Test
    public void getTaskByListTest() {
        Task[] tasks = new Task[1];
        when(mockClient.getForEntity(eq(url + "task/getByList/1"), eq(Task[].class)))
                .thenReturn(new ResponseEntity<>(tasks, HttpStatus.OK));

        Task[] expected = this.serverUtils.getTaskByList(1).getBody();

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Task[].class));
        assertEquals(tasks, expected);
    }

    @Test
    public void getTasksOrderedTest() {
        Task[] tasks = new Task[1];
        when(mockClient.getForEntity(eq(url + "task/getOrdered/1"), eq(Task[].class)))
                .thenReturn(new ResponseEntity<>(tasks, HttpStatus.OK));

        Task[] expected = this.serverUtils.getTasksOrdered(1).getBody();

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Task[].class));
        assertEquals(tasks, expected);
    }

    @Test
    public void deleteSubtaskTest() {
        when(mockClient.exchange(eq(url + "subtask/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<IdResponseModel>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.deleteSubtask(1, 1);

        verify(mockClient, times(1))
                .exchange(eq(url + "subtask/delete/1/1"),
                        eq(HttpMethod.DELETE),
                        eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void deleteSubtaskExceptionTest() {
        when(mockClient.exchange(eq(url + "subtask/delete/1/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.deleteSubtask(1, 1);

        verify(mockClient, times(1))
                .exchange(eq(url + "subtask/delete/1/1"),
                        eq(HttpMethod.DELETE),
                        eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void editSubtaskWrongIdTest() {
        HttpEntity<SubtaskEditModel> req = new HttpEntity<>(new SubtaskEditModel());
        when(mockClient.exchange(eq(url + "subtask/edit/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(2, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editSubtask(1, new SubtaskEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "subtask/edit/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Subtask doesn't match", response.getErrorMessage());
    }

    @Test
    public void editSubtaskTest() {
        HttpEntity<SubtaskEditModel> req = new HttpEntity<>(new SubtaskEditModel());
        when(mockClient.exchange(eq(url + "subtask/edit/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editSubtask(1, new SubtaskEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "subtask/edit/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void editSubtaskExceptionTest() {
        HttpEntity<SubtaskEditModel> req = new HttpEntity<>(new SubtaskEditModel());
        when(mockClient.exchange(eq(url + "subtask/edit/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.editSubtask(1, new SubtaskEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "subtask/edit/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void getSubtasksByTaskTest() {
        Subtask[] subtasks = new Subtask[1];
        when(mockClient.getForEntity(eq(url + "subtask/getByTask/1"), eq(Subtask[].class)))
                .thenReturn(new ResponseEntity<>(subtasks, HttpStatus.OK));

        Subtask[] expected = this.serverUtils.getSubtasksByTask(1).getBody();

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Subtask[].class));
        assertEquals(subtasks, expected);
    }

    @Test
    public void addTagToTaskTest() {
        when(mockClient.postForObject(eq(url + "tag/addToTask/1"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenReturn(new IdResponseModel(1, null));

        IdResponseModel response = this.serverUtils.addTagToTask(new Tag(), 1);

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void addTagToTaskExceptionTest() {
        when(mockClient.postForObject(eq(url + "tag/addToTask/1"),
                any(HttpEntity.class), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.addTagToTask(new Tag(), 1);

        verify(mockClient, times(1)).postForObject(any(String.class),
                any(HttpEntity.class), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void editTagWrongIdTest() {
        HttpEntity<TagEditModel> req = new HttpEntity<>(new TagEditModel());
        when(mockClient.exchange(eq(url + "tag/edit/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(2, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editTag(1, new TagEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "tag/edit/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Tag doesn't match", response.getErrorMessage());
    }

    @Test
    public void editTagTest() {
        HttpEntity<TagEditModel> req = new HttpEntity<>(new TagEditModel());
        when(mockClient.exchange(eq(url + "tag/edit/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.editTag(1, new TagEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "tag/edit/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void editTagExceptionTest() {
        HttpEntity<TagEditModel> req = new HttpEntity<>(new TagEditModel());
        when(mockClient.exchange(eq(url + "tag/edit/" + 1), eq(HttpMethod.PUT),
                eq(req), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.editTag(1, new TagEditModel());

        verify(mockClient, times(1))
                .exchange(eq(url + "tag/edit/" + 1),
                        eq(HttpMethod.PUT),
                        eq(req), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void deleteTagTest() {
        when(mockClient.exchange(eq(url + "tag/delete/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenReturn(new ResponseEntity<IdResponseModel>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.deleteTag(1);

        verify(mockClient, times(1))
                .exchange(eq(url + "tag/delete/1"),
                        eq(HttpMethod.DELETE),
                        eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void deleteTagExceptionTest() {
        when(mockClient.exchange(eq(url + "tag/delete/1"),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.deleteTag(1);

        verify(mockClient, times(1))
                .exchange(eq(url + "tag/delete/1"),
                        eq(HttpMethod.DELETE),
                        eq(null), eq(IdResponseModel.class));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }

    @Test
    public void getTagTest() {
        when(mockClient.getForEntity(eq(url + "tag/get/1"), eq(Tag.class)))
                .thenReturn(new ResponseEntity<>(new Tag(), HttpStatus.OK));

        var actual = this.serverUtils.getTag(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Tag.class));
        assertEquals(new Tag(), actual.getBody());
    }

    @Test
    public void getTagsByTask() {
        Tag[] expected = new Tag[1];
        when(mockClient.getForEntity(eq(url + "tag/getByTask/1"), eq(Tag[].class)))
                .thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));

        var actual = this.serverUtils.getTagsByTask(1);

        verify(mockClient, times(1))
                .getForEntity(any(String.class), eq(Tag[].class));
        assertEquals(expected, actual.getBody());
    }

    @Test
    public void removeTagFromTaskTest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tagId", String.valueOf(1));
        params.put("taskId", String.valueOf(1));

        when(mockClient.exchange(eq(url + "/tag/removeFromTask/" + 1 + "/" + 1),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class),
                eq(params)))
                .thenReturn(new ResponseEntity<IdResponseModel>(
                        new IdResponseModel(1, null), HttpStatus.OK));

        IdResponseModel response = this.serverUtils.removeTagFromTask(1, 1);

        verify(mockClient, times(1))
                .exchange(eq(url + "/tag/removeFromTask/" + 1 + "/" + 1),
                        eq(HttpMethod.DELETE),
                        eq(null), eq(IdResponseModel.class),
                        eq(params));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void removeTagFromTaskExceptionTest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tagId", String.valueOf(1));
        params.put("taskId", String.valueOf(1));

        when(mockClient.exchange(eq(url + "/tag/removeFromTask/" + 1 + "/" + 1),
                eq(HttpMethod.DELETE),
                eq(null), eq(IdResponseModel.class),
                eq(params)))
                .thenThrow(exception);

        IdResponseModel response = this.serverUtils.removeTagFromTask(1, 1);

        verify(mockClient, times(1))
                .exchange(eq(url + "/tag/removeFromTask/" + 1 + "/" + 1),
                        eq(HttpMethod.DELETE),
                        eq(null), eq(IdResponseModel.class),
                        eq(params));
        assertTrue(response.getId() == -1);
        assertEquals("Oops, failed to connect to server...", response.getErrorMessage());
    }
}
