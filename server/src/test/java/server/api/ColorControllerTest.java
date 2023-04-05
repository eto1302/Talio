package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Color;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import server.Services.ColorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@SpringBootTest
class ColorControllerTest {

    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ColorService mockService;

    private transient Color color1 = Color.create("#FFFFFF", "#000000");
    private transient Color color2 = Color.create("#101010", "#FF10FF");

    @Test
    void testGetColorById() throws Exception{
        when(mockService.getColorById(anyInt())).thenReturn(color1);

        ResultActions response = mock.perform(get("/color/find/10")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void testGetColorByIdFails() throws Exception{

    }
    @Test
    void testCreate() throws Exception{

    }


}