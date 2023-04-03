package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.TaskEditModel;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import server.Services.TaskService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private TaskService mockService;

    private transient Task task1 = Task.create("No description yet",
            "Task 1", 1, new ArrayList<>());
    private transient  Task task2 = Task.create("Getting food",
            "Task 2", 1, new ArrayList<>());
    private IdResponseModel model;

    @BeforeEach
    void setupModel(){
        model = new IdResponseModel(1,null);
    }

    @Test
    void testGetTask() throws Exception{
        when(mockService.getTaskById(anyInt())).thenReturn(task1);

        ResultActions response = mock.perform(get("/task/get/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", CoreMatchers.is(task1.getTitle())));
    }

    @Test
    void testGetByList() throws Exception{
        when(mockService.getAllTaskByList(anyInt())).thenReturn(Arrays.asList(task1, task2));

        ResultActions response = mock.perform(get("/task/getByList/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()",
                        CoreMatchers.is(Arrays.asList(task1, task2).size())))
                .andExpect(jsonPath("$[0].description", CoreMatchers.is(task1.getDescription())))
                .andExpect(jsonPath("$[1].title", CoreMatchers.is(task2.getTitle())));
    }

    @Test
    void testGetAllTasks() throws Exception{
        when(mockService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        ResultActions response = mock.perform(get("/task/findAll")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()",
                        CoreMatchers.is(Arrays.asList(task1, task2).size())));
    }

    @Test
    void testAddTask() throws Exception{
        when(mockService.addTask(eq(task1), anyInt())).thenReturn(model);

        ResultActions response = mock.perform(post("/task/add/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(task1)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is((1))));

    }

    @Test
    void testEditTask() throws Exception{
        when(mockService.editTask(anyInt(), any(TaskEditModel.class))).thenReturn(model);
        TaskEditModel taskEditModel = new TaskEditModel("Watching movies",
                "Task 3", 1, new List(), 1);

        ResultActions response = mock.perform(put("/task/edit/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(taskEditModel)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(1)));

        String content = response.andReturn().getResponse().getContentAsString();
        assertNull(mapper.readValue(content, IdResponseModel.class).getErrorMessage());
    }
    @Test
    void testRemoveTask() throws Exception{
        when(mockService.removeTask(anyInt(), anyInt())).thenReturn(model);

        ResultActions response = mock.perform(delete("/task/remove/1/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(1)));
    }
    @Test
    void testGetTasksSorted() throws Exception{
        when(mockService.getTasksSortedByIndex(anyInt())).thenReturn(Arrays.asList(task2,task1));

        ResultActions response = mock.perform(get("/task/getSorted/4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].title", CoreMatchers.is(task2.getTitle())));
    }
    @Test
    void testGetTaskFails() throws Exception{
        when(mockService.getTaskById(anyInt())).thenThrow(NoSuchElementException.class);

        ResultActions response = mock.perform(get("/task/get/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testGetByListFails() throws Exception{
        when(mockService.getAllTaskByList(anyInt())).thenThrow(NoSuchElementException.class);

        ResultActions response = mock.perform(get("/task/getByList/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}