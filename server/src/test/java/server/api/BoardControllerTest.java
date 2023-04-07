package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Color;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;
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
import org.springframework.web.context.request.async.DeferredResult;
import server.Services.BoardService;
import server.Services.BoardsUpdatedListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Autowired
    private transient ObjectMapper objectMapper;
    @MockBean
    private transient BoardService mockService;

    private transient Board board = new Board("name", "pwd",
            null, new ArrayList<>(
                    Arrays.asList(new Color("#000000", "#FFFFFF"),
                            new Color("#000000", "#FFFFFF"))),
            new ArrayList<>());

    private transient Board board2 = new Board("default", "1234",
            null, new ArrayList<>(
                    Arrays.asList(new Color("#000000", "#FFFFFF"))),
            new ArrayList<>());

    private IdResponseModel model;
    @MockBean
    private BoardsUpdatedListener mockListener;
    @BeforeEach
    void setupModel(){
        model = new IdResponseModel(1, null);
    }
    @Test
    void testCreate() throws Exception {
        when(mockService.saveBoard(board)).thenReturn(model);

        ResultActions response = mockMvc.perform(post("/board/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(board)))
                .andExpect(status().isOk());

        String boardStr = response.andReturn().getResponse().getContentAsString();
        IdResponseModel model = objectMapper.readValue(boardStr, IdResponseModel.class);
        assertEquals(null, model.getErrorMessage());
    }
    @Test
    void testGetBoardByID() throws Exception{
        when(mockService.getBoardById(anyInt())).thenReturn(board);

        ResultActions response = mockMvc.perform(get("/board/find/3")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(board.getName())));
    }
    @Test
    void testGetBoardByIDFails() throws Exception{
        when(mockService.getBoardById(anyInt())).thenThrow(NoSuchElementException.class);

        ResultActions response = mockMvc.perform(get("/board/find/3")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testGetBoardsUpdated() throws Exception{
        DeferredResult<Board[]> deferredResult = new DeferredResult<>();
        doNothing().when(mockListener).addDeferredResult(deferredResult);

        ResultActions response = mockMvc.perform(get("/board/findAllUpdated")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void testGetAllBoards() throws Exception{
        when(mockService.getAllBoards()).thenReturn(Arrays.asList(board, board2));

        ResultActions response = mockMvc.perform(get("/board/findAll")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()",
                        CoreMatchers.is(Arrays.asList(board, board2).size())))
                .andExpect(jsonPath("$[0].name", CoreMatchers.is(board.getName())))
                .andExpect(jsonPath("$[1].password", CoreMatchers.is(board2.getPassword())));
    }
    @Test
    void testGetBoardByInviteKey() throws Exception{
        when(mockService.getBoardByInviteKey(anyString())).thenReturn(board);

        ResultActions response = mockMvc.perform(get("/board/getByInvite/udhavbj")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.password", CoreMatchers.is(board.getPassword())));
    }
    @Test
    void testGetByInviteKeyFails() throws Exception{
        when(mockService.getBoardByInviteKey(anyString())).thenThrow(NoSuchElementException.class);

        ResultActions response = mockMvc.perform(get("/board/getByInvite/udhDFaafvbj")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testDeleteBoard() throws Exception{
        when(mockService.deleteBoard(anyInt())).thenReturn(model);

        ResultActions response = mockMvc.perform(delete("/board/delete/4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(model.getId())));

        String content = response.andReturn().getResponse().getContentAsString();
        assertNull(objectMapper.readValue(content, IdResponseModel.class).getErrorMessage());
    }
    @Test
    void testVerifyAdminPassword() throws Exception{
        when(mockService.verifyAdminPassword(anyString())).thenReturn(true);

        ResultActions response = mockMvc.perform(get("/board/verify/HJGDUFkjbJSjuwANCJEIU")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }
    @Test
    void testEditBoard() throws Exception{
        BoardEditModel boardEditModel = new BoardEditModel("Hello", "password");

        when(mockService.editBoard(anyInt(), eq(boardEditModel))).thenReturn(model);

        ResultActions response = mockMvc.perform(put("/board/edit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardEditModel)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(model.getId())));

        String content = response.andReturn().getResponse().getContentAsString();
        assertNull(objectMapper.readValue(content, IdResponseModel.class).getErrorMessage());
    }
}
