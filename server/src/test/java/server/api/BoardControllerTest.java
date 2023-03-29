package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Color;
import commons.models.IdResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import server.database.BoardRepository;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Autowired
    private transient ObjectMapper objectMapper;

    @MockBean
    private transient BoardRepository mockBoardRepo;

    private transient Board board = Board.create("name", "pwd",
            null, Color.create("#000000", "#FFFFFF"),
            Color.create("#000000", "#FFFFFF"), new ArrayList<>());


    @Test
    void testCreate() throws Exception {
        ResultActions response = mockMvc.perform(post("/board/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(board)))
                .andExpect(status().isOk());

        String boardStr = response.andReturn().getResponse().getContentAsString();
        IdResponseModel model = objectMapper.readValue(boardStr, IdResponseModel.class);
        assertEquals(null, model.getErrorMessage());
        verify(mockBoardRepo, times(1)).save(board);
    }
}
