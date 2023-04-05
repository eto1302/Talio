package server.Services;

import commons.Subtask;
import commons.Task;
import commons.models.SubtaskEditModel;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.SubtaskRepository;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
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
}