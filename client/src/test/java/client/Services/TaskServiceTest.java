package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Color;
import commons.Task;
import commons.models.IdResponseModel;
import commons.sync.TaskDeleted;
import commons.sync.TaskEdited;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @MockBean
    private UserData mockUserData = mock(UserData.class);
    @MockBean
    private ServerUtils mockServerUtils = mock(ServerUtils.class);
    private TaskService taskService;
    private Task task;
    private Board board;
    private Color color;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(mockUserData, mockServerUtils);
        this.task = Task.create("descr", "test", 1, new ArrayList<>());
        this.color = Color.create("#FFFFFF", "#000000");
        this.color.setIsDefault(true);
        this.board = Board.create("test", "passwd",
                new ArrayList<>(), Arrays.asList(color), new ArrayList<>());
    }
    @Test
    public void getTaskTest(){
        when(mockServerUtils.getTask(any(Integer.class))).thenReturn(task);

        Task response = taskService.getTask(1);

        assertEquals(response, task);
    }

    @Test
    public void getTasksOrdered(){
        when(mockServerUtils.getTasksOrdered(any(Integer.class))).thenReturn(
                new ResponseEntity<>(new Task[]{task}, HttpStatus.OK));

        List<Task> response = taskService.getTasksOrdered(1);

        assertEquals(response, Arrays.asList(task));
    }

    @Test
    public void editTask(){
        when(mockUserData.updateBoard(any(TaskEdited.class)))
                .thenReturn(new IdResponseModel(1, null));
        IdResponseModel response = this.taskService.editTask(task, new commons.List(), 1);
        verify(mockUserData, times(1))
                .updateBoard(any(TaskEdited.class));
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }
//    @Test
//    public void addTask() {
//        when(mockUserData.getCurrentBoard()).thenReturn(board);
//        when(mockServerUtils.getTaskByList(any(Integer.class))).thenReturn(new ArrayList<>());
//        when(mockUserData.updateBoard(any(TaskAdded.class))).thenReturn(
//                new IdResponseModel(1, null));
//
//        IdResponseModel response = this.taskService.addTask("name",
//                new commons.List());
//
//        verify(mockUserData, times(1)).getCurrentBoard();
//        verify(mockServerUtils, times(1)).getTaskByList(any(Integer.class));
//        verify(mockUserData, times(1)).updateBoard(any(TaskAdded.class));
//        assertTrue(response.getId() != -1);
//        assertNull(response.getErrorMessage());
//    }
    @Test
    public void deleteTest(){
        when(mockUserData.getCurrentBoard()).thenReturn(board);
        when(mockUserData.updateBoard(any(TaskDeleted.class)))
                .thenReturn(new IdResponseModel(1, null));
        IdResponseModel response = this.taskService.deleteTask(task);
        verify(mockUserData, times(1)).updateBoard(any(TaskDeleted.class));
        verify(mockUserData, times(1)).getCurrentBoard();
        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }

}
