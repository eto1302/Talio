package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Color;
import commons.models.ColorEditModel;
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
import server.Services.ColorService;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
class ColorControllerTest {

    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ColorService mockService;

    private transient Color color1 = new Color("#FFFFFF", "#000000");
    private transient Color color2 = new Color("#101010", "#FF10FF");
    private IdResponseModel model;
    @BeforeEach
    void setup(){
        model = new IdResponseModel(1, null);
    }
    @Test
    void testGetColorById() throws Exception{
        when(mockService.getColorById(anyInt())).thenReturn(color1);

        ResultActions response = mock.perform(get("/color/find/10")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void testGetColorByIdFails() throws Exception{
        when(mockService.getColorById(anyInt())).thenThrow(NoSuchElementException.class);

        ResultActions response = mock.perform(get("/color/find/10")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testCreate() throws Exception{
        when(mockService.saveColor(color1)).thenReturn(model);

        ResultActions response = mock.perform(post("/color/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(color1)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(model.getId())));

        String content = response.andReturn().getResponse().getContentAsString();
        assertNull(mapper.readValue(content, IdResponseModel.class).getErrorMessage());
    }
    @Test
    void testSetToBoard() throws Exception{
        when(mockService.setToBoard(anyInt(), anyInt())).thenReturn(model);

        ResultActions response = mock.perform(put("/color/add/1/3")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(model.getId())));

        String content = response.andReturn().getResponse().getContentAsString();
        assertNull(mapper.readValue(content, IdResponseModel.class).getErrorMessage());
    }
    @Test
    void testGetAllColors() throws Exception{
        when(mockService.getAllColors()).thenReturn(Arrays.asList(color1, color2));

        ResultActions response = mock.perform(get("/color/findAll")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()",
                        CoreMatchers.is(Arrays.asList(color1, color2).size())))
                .andExpect(jsonPath("$[0].fontColor", CoreMatchers.is(color1.getFontColor())))
                .andExpect(jsonPath("$[1].backgroundColor",
                        CoreMatchers.is(color2.getBackgroundColor())));
    }
    @Test
    void testDeleteColor() throws Exception{
        when(mockService.deleteColor(anyInt(), anyInt())).thenReturn(model);

        ResultActions response = mock.perform(delete("/color/delete/2/3")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(model.getId())));
    }
    @Test
    void testEditColor() throws Exception{
        ColorEditModel colorEditModel = new ColorEditModel("#101011", "#FFDDDD", true);

        when(mockService.editColor(anyInt(), eq(colorEditModel))).thenReturn(model);

        ResultActions response = mock.perform(put("/color/edit/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(colorEditModel)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(model.getId())));
    }


}