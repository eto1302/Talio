package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Subtask;
import commons.models.IdResponseModel;
import commons.models.SubtaskEditModel;
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
import server.Services.SubtaskService;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class SubtaskControllerTest {

    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private SubtaskService mockService;

    private transient Subtask subtask1 = Subtask.create("Doing homework", false, 1);
    private transient Subtask subtask2 = Subtask.create("Random Subtask", true, 1);
    private IdResponseModel model;
    @BeforeEach
    void setupModel(){
        model = new IdResponseModel(1, null);
    }
    @Test
    void testGetSubtask() throws Exception{
        when(mockService.getSubtaskById(anyInt())).thenReturn(subtask1);

        ResultActions response = mock.perform(get("/subtask/get/20")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.description", CoreMatchers.is(subtask1.getDescription())));
    }
    @Test
    void testGetSubtasksByTask() throws Exception{
        when(mockService.getAllSubtasksByTaskID(anyInt())).thenReturn(Arrays.asList(subtask1, subtask2));

        ResultActions response = mock.perform(get("/subtask/getByTask/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(Arrays.asList(subtask1, subtask2).size())))
                .andExpect(jsonPath("$[0].checked", CoreMatchers.is(subtask1.isChecked())))
                .andExpect(jsonPath("$[1].description", CoreMatchers.is(subtask2.getDescription())));
    }
    @Test
    void getAllSubtasks() throws Exception{
        when(mockService.getAllSubtasks()).thenReturn(Arrays.asList(subtask1,subtask2));

        ResultActions response = mock.perform(get("/subtask/getAll")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(Arrays.asList(subtask1, subtask2).size())));
    }
    @Test
    void testGetSubtasksOrdered() throws Exception{
        when(mockService.getSubtasksOrdered(anyInt())).thenReturn(Arrays.asList(subtask2,subtask1));

        ResultActions response = mock.perform(get("/subtask/getOrdered/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(Arrays.asList(subtask2, subtask1).size())))
                .andExpect(jsonPath("$[0].description", CoreMatchers.is(subtask2.getDescription())));
    }
    @Test
    void testRemoveSubtask() throws Exception{
        when(mockService.removeSubtask(anyInt(), anyInt())).thenReturn(model);

        ResultActions response = mock.perform(delete("/subtask/delete/1/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(model.getId())));

        String content = response.andReturn().getResponse().getContentAsString();
        assertNull(mapper.readValue(content, IdResponseModel.class).getErrorMessage());
    }
    @Test
    void testAddSubtask() throws Exception{
        when(mockService.addSubtask(eq(subtask1), anyInt())).thenReturn(model);

        ResultActions response = mock.perform(post("/subtask/add/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subtask1)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(model.getId())));

        String content = response.andReturn().getResponse().getContentAsString();
        assertNull(mapper.readValue(content, IdResponseModel.class).getErrorMessage());
    }
    @Test
    void testEditSubtask() throws Exception{
        SubtaskEditModel subtaskEditModel =
                new SubtaskEditModel("Nothing", false, 1);
        when(mockService.editSubtask(anyInt(), eq(subtaskEditModel))).thenReturn(model);

        ResultActions response = mock.perform(put("/subtask/edit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subtaskEditModel)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(model.getId())));
    }
    @Test
    void getSubtaskFails() throws Exception{
        when(mockService.getSubtaskById(anyInt())).thenThrow(NoSuchElementException.class);

        ResultActions response = mock.perform(get("/subtask/get/20")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void getSubtasksByTaskFails() throws Exception{
        when(mockService.getAllSubtasksByTaskID(anyInt())).thenThrow(NoSuchElementException.class);

        ResultActions response = mock.perform(get("/subtask/getByTask/20")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}