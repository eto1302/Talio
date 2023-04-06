package server.Services;

import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.TaskEditModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.ListRepositoy;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @MockBean
    private TaskRepository mockTaskRepo;
    @MockBean
    private ListRepositoy mockListRepo;
    @Autowired
    private TaskService mockService = new TaskService(mockTaskRepo, mockListRepo);

    private transient Task task1 = Task.create("No description yet",
            "Task 1", 1, new ArrayList<>());
    private transient  Task task2 = Task.create("Getting food",
            "Task 2", 1, new ArrayList<>());
    private transient List list = List.create("List 2", 1,
            new ArrayList<>());
    private Random random;
    private TaskEditModel model;

    @BeforeEach
    void setup(){
        random=new Random();
        model=new TaskEditModel("Task", "If you read this lmao", 10, list, 1);
    }

    @Test
    void testGetTaskById(){
        when(mockTaskRepo.getTaskById(anyInt())).thenReturn(task1);

        assertEquals(task1, mockService.getTaskById(anyInt()));
        verify(mockTaskRepo, times(1)).getTaskById(anyInt());
    }

    @Test
    void testGetAllTasks(){
        when(mockTaskRepo.findAll()).thenReturn(Arrays.asList(task1, task2));

        java.util.List<Task> list = mockService.getAllTasks();
        assertEquals(2, list.size());
        assertEquals(task2, list.get(1));
        verify(mockTaskRepo, times(1)).findAll();
    }

    @Test
    void testGetTasksSortedByIndex(){
        when(mockTaskRepo.getTasksByIndex(anyInt())).thenReturn(Arrays.asList(task2, task1));

        java.util.List<Task> list = mockService.getTasksOrderedByIndex(anyInt());
        assertEquals(2, list.size());
        assertEquals(task1, list.get(1));
        verify(mockTaskRepo, times(1)).getTasksByIndex(anyInt());
    }

    @Test
    void testGetAllTasksByList(){
        list.getTasks().add(task1);
        list.getTasks().add(task2);
        when(mockListRepo.existsById(anyInt())).thenReturn(true);
        when(mockListRepo.getListByID(anyInt())).thenReturn(list);

        java.util.List<Task> list = mockService.getAllTaskByList(anyInt());
        assertEquals(2, list.size());
        assertEquals(task1, list.get(0));
        verify(mockListRepo, times(1)).getListByID(anyInt());
    }

    @Test
    void testGetAllTasksByListFails(){
        when(mockListRepo.existsById(anyInt())).thenReturn(false);

        assertThrows(NoSuchElementException.class, ()->{
            mockService.getAllTaskByList(anyInt());
        });
    }

    @Test
    void testAddTask(){
        when(mockListRepo.getListByID(anyInt())).thenReturn(list);

        IdResponseModel test = mockService.addTask(task1, random.nextInt());
        verify(mockTaskRepo, times(1)).save(task1);
        assertEquals(task1.getId(), test.getId());
    }

    @Test
    void testAddTaskFails(){
        when(mockListRepo.getListByID(anyInt())).thenThrow(new NoSuchElementException());

        IdResponseModel test = mockService.addTask(task1, random.nextInt());
        verify(mockTaskRepo, times(0)).save(task1);
        assertEquals(-1, test.getId());
    }

    @Test
    void testRemoveTask(){
        when(mockListRepo.getListByID(anyInt())).thenReturn(list);
        when(mockTaskRepo.getTaskById(anyInt())).thenReturn(task1);

        int taskId = random.nextInt(100);
        int listId = random.nextInt(100);
        IdResponseModel test = mockService.deleteTask(taskId, listId);

        assertEquals(test.getId(), taskId);
        verify(mockTaskRepo, times(1)).delete(task1);
    }

    @Test
    void testRemoveTaskFails(){
        when(mockListRepo.getListByID(anyInt())).thenThrow(new NoSuchElementException());

        int taskId = random.nextInt(100);
        int listId = random.nextInt(100);
        IdResponseModel test = mockService.deleteTask(taskId, listId);

        assertEquals(-1, test.getId());
        verify(mockTaskRepo, times(0)).delete(task1);
        verify(mockTaskRepo, times(0)).getTaskById(anyInt());
        verify(mockListRepo, times(1)).getListByID(anyInt());
    }

    @Test
    void testEditTask(){
        when(mockTaskRepo.getTaskById(anyInt())).thenReturn(task1);
        int id = random.nextInt();

        IdResponseModel test = mockService.editTask(id, model);
        assertEquals(id, test.getId());
        verify(mockTaskRepo, times(1)).save(task1);
    }

    @Test
    void testEditTaskFails(){
        when(mockTaskRepo.getTaskById(anyInt())).thenThrow(new NoSuchElementException());
        int id = random.nextInt();

        IdResponseModel test = mockService.editTask(id, model);
        assertEquals(-1, test.getId());
        verify(mockTaskRepo, times(0)).save(task1);
    }
}