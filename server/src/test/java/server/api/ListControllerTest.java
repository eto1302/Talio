package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import server.Services.ListService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
class ListControllerTest {

    @Autowired
    private transient MockMvc mock;
    @Autowired
    private transient ObjectMapper mapper;
    @MockBean
    private transient ListService mockService;

    private transient List list = List.create("List 1", 1,
            new ArrayList<>(Arrays.asList(
                    Task.create("No description", "Task 1", 1, new ArrayList<>()))));

    private transient List list2 = List.create("List 2", 1,
            new ArrayList<>(Arrays.asList(
                    Task.create("Watching series", "Task 2", 2, new ArrayList<>()))));

    @Test
    void testAddList() throws Exception{
        when(mockService.addList(list, 1)).thenReturn(new IdResponseModel(1, null));

        ResultActions response = mock.perform(post("/list/add/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(list)));

        response.andExpect(MockMvcResultMatchers.status().isOk());

        String listContent = response.andReturn().getResponse().getContentAsString();
        IdResponseModel model = mapper.readValue(listContent, IdResponseModel.class);
        assertNull(model.getErrorMessage());
    }

    @Test
    void testGetList() throws Exception{
        int listId=1;
        when(mockService.getListById(listId)).thenReturn(list);

        ResultActions response = mock.perform(get("/list/get/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(list)));

        response.andExpect(status().isOk());

        String listContent = response.andReturn().getResponse().getContentAsString();
        List entity = mapper.readValue(listContent, List.class);

        assertEquals("List 1", entity.getName());
        assertEquals(1, entity.getBoardId());
    }

    @Test
    void testGetByBoard() throws Exception{
        int boardId = 1;
        when(mockService.getAllListByBoard(boardId)).thenReturn(Arrays.asList(list));

        ResultActions response = mock.perform(get("/list/getByBoard/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(list)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(Arrays.asList(list).size())));
    }

    @Test
    void testGetAllLists() throws Exception{
        when(mockService.getAllLists()).thenReturn(Arrays.asList(list, list2));

        ResultActions response = mock.perform(get("/list/findAll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(list)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(Arrays.asList(list, list2).size())))
                .andExpect(jsonPath("$[0].name", CoreMatchers.is(list.getName())))
                .andExpect(jsonPath("$[1].boardId", CoreMatchers.is(list2.getBoardId())));


    }

    @Test
    void testEditList() throws Exception{
        IdResponseModel model = new IdResponseModel(1, null);
        ListEditModel listEditModel = new ListEditModel("List 100");
        when(mockService.editList(1,1, listEditModel)).thenReturn(model);

        ResultActions response = mock.perform(put("/list/edit/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(listEditModel)));

        verify(mockService, times(1)).editList(1,1, listEditModel);
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(1)));
    }

    @Test
    void testRemoveList() throws Exception{
        when(mockService.removeList(1,1)).thenReturn(new IdResponseModel(1,null));

        ResultActions response = mock.perform(delete("/list/delete/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(list)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(1)));
    }
    @Test
    void testGetByBoardFail() throws Exception{
        when(mockService.getAllListByBoard(anyInt())).thenThrow(NoSuchElementException.class);

        ResultActions response = mock.perform(get("/list/getByBoard/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(list)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testGetListFail() throws Exception{
        when(mockService.getListById(anyInt())).thenThrow(NoSuchElementException.class);

        ResultActions response = mock.perform(get("/list/get/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(list)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}