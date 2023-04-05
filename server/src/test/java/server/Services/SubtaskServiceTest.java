package server.Services;

import commons.Subtask;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.SubtaskEditModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.SubtaskRepository;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
class SubtaskServiceTest {

    @MockBean
    private TaskRepository mockTaskRepo;
    @MockBean
    private SubtaskRepository mockSubtaskRepo;

    @Autowired
    private SubtaskService mockService = new SubtaskService(mockTaskRepo, mockSubtaskRepo);

    private transient Subtask subtask1 = Subtask.create("Doing homework", false, 1);
    private transient Subtask subtask2 = Subtask.create("Random Subtask", true, 1);
    private transient Task task = Task.create("No description yet",
            "Task 1", 1, new ArrayList<>());
    private Random random;
    private SubtaskEditModel model;

    @BeforeEach
    void setup(){
        random=new Random();
        model=new SubtaskEditModel("Task", false, 1);
    }

    @Test
    void testGetSubtaskById(){
        when(mockSubtaskRepo.getSubtaskByID(anyInt())).thenReturn(subtask1);

        assertEquals(subtask1, mockService.getSubtaskById(anyInt()));
        verify(mockSubtaskRepo, times(1)).getSubtaskByID(anyInt());
    }

    @Test
    void testGetAllSubtasks(){
        when(mockSubtaskRepo.findAll()).thenReturn(Arrays.asList(subtask1, subtask2));

        java.util.List<Subtask> list = mockService.getAllSubtasks();
        assertEquals(2, list.size());
        assertEquals(subtask2, list.get(1));
        verify(mockSubtaskRepo, times(1)).findAll();
    }

    @Test
    void testGetSubtasksOrdered(){
        when(mockSubtaskRepo.getSubtasksOrdered(anyInt()))
                .thenReturn(Arrays.asList(subtask2, subtask1));

        java.util.List<Subtask> list = mockService.getSubtasksOrdered(anyInt());
        assertEquals(2, list.size());
        assertEquals(subtask1, list.get(1));
        verify(mockSubtaskRepo, times(1)).getSubtasksOrdered(anyInt());
    }

    @Test
    void testGetAllSubtasksByTaskID(){
        task.getSubtasks().add(subtask1);
        task.getSubtasks().add(subtask2);
        when(mockTaskRepo.existsById(anyInt())).thenReturn(true);
        when(mockTaskRepo.getById(anyInt())).thenReturn(task);

        java.util.List<Subtask> list = mockService.getAllSubtasksByTaskID(anyInt());
        assertEquals(2, list.size());
        assertEquals(subtask1, list.get(0));
        verify(mockTaskRepo, times(1)).getById(anyInt());
    }

    @Test
    void testGetAllSubtasksByTaskIDFails(){
        when(mockTaskRepo.existsById(anyInt())).thenReturn(false);

        assertThrows(NoSuchElementException.class, ()->{
            mockService.getAllSubtasksByTaskID(anyInt());
        });
    }

    @Test
    void testAddSubtask(){
        when(mockTaskRepo.getById(anyInt())).thenReturn(task);

        IdResponseModel test = mockService.addSubtask(subtask1, random.nextInt());
        verify(mockSubtaskRepo, times(1)).save(subtask1);
        assertEquals(subtask1.getId(), test.getId());
    }

    @Test
    void testAddSubtaskFails(){
        when(mockTaskRepo.getById(anyInt())).thenThrow(new NoSuchElementException());

        IdResponseModel test = mockService.addSubtask(subtask1, random.nextInt());
        verify(mockSubtaskRepo, times(0)).save(subtask1);
        assertEquals(-1, test.getId());
    }

    @Test
    void testRemoveSubtask(){
        when(mockSubtaskRepo.getSubtaskByID(anyInt())).thenReturn(subtask1);
        when(mockTaskRepo.getById(anyInt())).thenReturn(task);

        int taskId = random.nextInt(100);
        int subtaskId = random.nextInt(100);
        IdResponseModel test = mockService.removeSubtask(subtaskId, taskId);

        assertEquals(test.getId(), subtaskId);
        verify(mockSubtaskRepo, times(1)).delete(subtask1);
    }

    @Test
    void testRemoveSubtaskFails(){
        when(mockTaskRepo.getById(anyInt())).thenThrow(new NoSuchElementException());

        int taskId = random.nextInt(100);
        int subtaskId = random.nextInt(100);
        IdResponseModel test = mockService.removeSubtask(subtaskId, taskId);

        assertEquals(-1, test.getId());
        verify(mockSubtaskRepo, times(0)).delete(subtask1);
        verify(mockTaskRepo, times(1)).getById(anyInt());
        verify(mockSubtaskRepo, times(0)).getSubtaskByID(anyInt());
    }

    @Test
    void testEditSubtask(){
        when(mockSubtaskRepo.getSubtaskByID(anyInt())).thenReturn(subtask1);
        int id = random.nextInt();

        IdResponseModel test = mockService.editSubtask(id, model);
        assertEquals(id, test.getId());
        verify(mockSubtaskRepo, times(1)).save(subtask1);
    }

    @Test
    void testEditSubtaskFails(){
        when(mockSubtaskRepo.getSubtaskByID(anyInt())).thenThrow(new NoSuchElementException());
        int id = random.nextInt();

        IdResponseModel test = mockService.editSubtask(id, model);
        assertEquals(-1, test.getId());
        verify(mockSubtaskRepo, times(0)).save(subtask1);
    }
}