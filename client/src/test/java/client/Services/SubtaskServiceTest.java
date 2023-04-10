package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Subtask;
import commons.Task;
import commons.models.IdResponseModel;
import commons.sync.SubtaskAdded;
import commons.sync.SubtaskDeleted;
import commons.sync.SubtaskEdited;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SubtaskServiceTest {
    @MockBean
    private UserData mockUserData = mock(UserData.class);
    @MockBean
    private ServerUtils mockServerUtils = mock(ServerUtils.class);
    @MockBean
    private ListService mockListService = mock(ListService.class);
    @MockBean
    private TaskService mockTaskService = mock(TaskService.class);
    private SubtaskService subtaskService;
    private Task mockTask;
    private Subtask mockSubtask;
    private List<Subtask> mockSubtasks;
    private commons.List mockList;
    private IdResponseModel mockIdResponseModel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        subtaskService = new SubtaskService(
            mockUserData, mockServerUtils, mockListService, mockTaskService
        );

        // Set up test objects
        mockIdResponseModel = new IdResponseModel(1, null);
        mockList = new commons.List("list", 0, null);
        mockTask = new Task("Test task", "test", 1, null);
        mockSubtask = new Subtask("Test subtask", false, mockTask.getId());
        mockSubtasks = new LinkedList<>(Arrays.asList(mockSubtask));

        mockTask.setSubtasks(mockSubtasks);
        mockList.setTasks(new ArrayList<>(Arrays.asList(mockTask)));


        when(mockListService.getList(any(Integer.class))).thenReturn(mockList);
        when(mockTaskService.getTask(any(Integer.class))).thenReturn(mockTask);
    }

    @Test
    public void getSubtasksOrderedTest() {
        Subtask[] expected = new Subtask[1];
        when(mockServerUtils.getSubtasksOrdered(any(Integer.class)))
            .thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));

        List<Subtask> response = this.subtaskService.getSubtasksOrdered(1);

        verify(mockServerUtils, times(1))
            .getSubtasksOrdered(any(Integer.class));
        assertEquals(Arrays.asList(expected), response);
    }

    @Test
    public void getSubtasksByTaskTest() {
        when(mockServerUtils.getSubtasksByTask(any(Integer.class)))
            .thenReturn(new ResponseEntity<>(mockSubtasks.toArray(new Subtask[0]), HttpStatus.OK));

        List<Subtask> response = subtaskService.getSubtasksByTask(1);

        verify(mockServerUtils, times(1))
            .getSubtasksByTask(any(Integer.class));
        assertEquals(mockSubtasks, response);
    }

    @Test
    public void addTest() {
        when(mockServerUtils.getSubtasksByTask(any(Integer.class)))
            .thenReturn(new ResponseEntity<>(new Subtask[0], HttpStatus.OK));
        when(mockUserData.updateBoard(any(SubtaskAdded.class)))
            .thenReturn(mockIdResponseModel);

        IdResponseModel response = subtaskService.add("New subtask", mockTask);

        verify(mockUserData, times(1)).updateBoard(any(SubtaskAdded.class));
        assertEquals(mockIdResponseModel, response);
    }

    @Test
    public void deleteTest() {
        when(mockUserData.updateBoard(any(SubtaskDeleted.class)))
            .thenReturn(mockIdResponseModel);

        IdResponseModel response = subtaskService.delete(mockSubtask);

        verify(mockUserData, times(1)).updateBoard(any(SubtaskDeleted.class));
        assertEquals(mockIdResponseModel, response);
    }

    @Test
    public void setCheckedTest() {
        when(mockUserData.updateBoard(any(SubtaskEdited.class)))
            .thenReturn(mockIdResponseModel);

        IdResponseModel response = subtaskService.setChecked(mockSubtask, true, 0);

        verify(mockUserData, times(1)).updateBoard(any(SubtaskEdited.class));
        assertEquals(mockIdResponseModel, response);
    }

    @Test
    public void setDescriptionTest() {
        when(mockUserData.updateBoard(any(SubtaskEdited.class)))
            .thenReturn(mockIdResponseModel);

        IdResponseModel response = subtaskService.setDescription(mockSubtask, "New description", 0);

        verify(mockUserData, times(1)).updateBoard(any(SubtaskEdited.class));
        assertEquals(mockIdResponseModel, response);
    }

    @Test
    public void writeSubtaskOrderTest() {
        when(mockUserData.updateBoard(any(SubtaskEdited.class)))
            .thenReturn(mockIdResponseModel);

        subtaskService.writeSubtaskOrder(mockSubtasks);

        verify(mockUserData, times(mockSubtasks.size())).updateBoard(any(SubtaskEdited.class));
    }

    @Test
    public void writeSubtaskOrderEmptyListTest() {
        when(mockUserData.updateBoard(any(SubtaskEdited.class)))
            .thenReturn(mockIdResponseModel);

        subtaskService.writeSubtaskOrder(new ArrayList<>());

        verify(mockUserData, times(0)).updateBoard(any(SubtaskEdited.class));
    }
}