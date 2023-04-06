package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Tag;
import commons.models.IdResponseModel;
import commons.models.TagEditModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import server.Services.TagService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient ObjectMapper mapper;

    @MockBean
    private transient TagService tagServiceMock;

    private Tag tag1, tag2;

    @BeforeEach
    public void setUp() {
        tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("Tag1");
        tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("Tag2");
    }

    @Test
    public void getTagByIdShouldReturnTag() throws Exception {
        when(tagServiceMock.getTagById(1)).thenReturn(tag1);

        mockMvc.perform(get("/tag/get/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(tag1.getName()));
    }


    @Test
    public void addShouldReturnSavedTagWhenRequestBodyIsValid() throws Exception {
        Tag newTag = new Tag();
        newTag.setName("New Tag");

        when(tagServiceMock.save(any(Tag.class))).thenReturn(newTag);

        mockMvc.perform(post("/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newTag)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(newTag.getName()));
    }

    @Test
    public void addShouldReturnBadRequestWhenRequestBodyIsNull() throws Exception {
        mockMvc.perform(post("/tag"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addShouldReturnBadRequestWhenTagNameIsNullOrEmpty() throws Exception {
        Tag invalidTag = new Tag();

        mockMvc.perform(post("/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidTag)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetByTaskReturnsTagsForValidId() throws Exception {
        int taskId = 1;
        List<Tag> tags = Arrays.asList(tag1, tag2);
        given(tagServiceMock.getAllTagsByTask(taskId)).willReturn(tags);

        mockMvc.perform(get("/tag/getByTask/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(tag1.getId())))
                .andExpect(jsonPath("$[0].name", is(tag1.getName())))
                .andExpect(jsonPath("$[1].id", is(tag2.getId())))
                .andExpect(jsonPath("$[1].name", is(tag2.getName())));

        // Assert
        verify(tagServiceMock, times(1)).getAllTagsByTask(taskId);
        verifyNoMoreInteractions(tagServiceMock);
    }

    @Test
    void testGetByTaskReturnsEmptyListForInvalidId() throws Exception {
        int taskId = -1;
        when(tagServiceMock.getAllTagsByTask(taskId)).thenReturn(new ArrayList<>());

        var res = mockMvc.perform(get("/tag/getByTask/{id}", taskId)).andReturn();
        assertEquals("[]", res.getResponse().getContentAsString());

        verify(tagServiceMock, times(1)).getAllTagsByTask(taskId);
        verifyNoMoreInteractions(tagServiceMock);
    }

    @Test
    public void getByBoardReturnsListOfTags() throws Exception {
        int boardId = 1;
        List<Tag> expectedTags = Arrays.asList(tag1, tag2);
        given(tagServiceMock.getAllTagsByBoard(boardId)).willReturn(expectedTags);

        mockMvc.perform(get("/tag/getByBoard/{id}", boardId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedTags)));
    }

    @Test
    public void getByBoardReturnsBadRequestWhenExceptionThrown() throws Exception {
        int boardId = 1;
        given(tagServiceMock.getAllTagsByBoard(boardId)).willThrow(new RuntimeException());

        mockMvc.perform(get("/tag/getByBoard/{id}", boardId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }


    @Test
    public void getAllShouldReturnAllTags() throws Exception {
        when(tagServiceMock.findAll()).thenReturn(Arrays.asList(tag1, tag2));

        mockMvc.perform(get("/tag/findAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(tag1.getName()))
                .andExpect(jsonPath("$[1].name").value(tag2.getName()));
    }

    @Test
    public void addTagToTaskSuccess() throws Exception {
        int taskId = 1;
        Tag tag = new Tag();
        tag.setName("Test Tag");

        IdResponseModel response = new IdResponseModel();
        response.setId(1);

        Mockito.when(tagServiceMock.addTagToTask(tag, taskId)).thenReturn(response);

        mockMvc.perform(post("/tag/addToTask/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId())));
    }

    @Test
    void addTagToBoard() throws Exception {
        int boardId = 1;

        when(tagServiceMock.addTagToBoard(tag1, boardId)).thenReturn(new IdResponseModel(1,""));

        ResultActions resultActions = mockMvc.perform(
                post("/tag/addToBoard/{id}", boardId)
                        .content(mapper.writeValueAsString(tag1))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void addTagToBoardBadRequest() throws Exception {
        int boardId = 1;

        ResultActions resultActions = mockMvc.perform(
                post("/tag/addToBoard/{id}", boardId)
                        .content(mapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void editTag() throws Exception {
        int id = 1;
        TagEditModel model = new TagEditModel();
        model.setName("Edited Tag");

        Tag editedTag = new Tag();
        editedTag.setId(id);
        editedTag.setName(model.getName());

        IdResponseModel expectedResponse = new IdResponseModel(id, null);

        given(tagServiceMock.editTag(id, model)).willReturn(expectedResponse);

        mockMvc.perform(put("/tag/edit/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(model)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedResponse.getId()));
    }

    @Test
    void editTagWhenEditTagFails() throws Exception {
        int id = 1;
        TagEditModel model = new TagEditModel();
        model.setName("Edited Tag");

        given(tagServiceMock.editTag(id, model)).willReturn(new IdResponseModel(-1, null));

        mockMvc.perform(put("/tag/edit/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(model)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(-1));
    }

    @Test
    public void removeTagReturnsOkResponse() throws Exception {
        int tagId = 1;
        IdResponseModel responseModel = new IdResponseModel(tagId, null);
        when(tagServiceMock.removeFromBoard(tagId)).thenReturn(responseModel);

        mockMvc.perform(delete("/tag/remove/" + tagId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseModel)));
    }

    @Test
    void testAddTag() throws Exception {
        String tagJson = mapper.writeValueAsString(tag1);
        when(tagServiceMock.save(tag1)).thenReturn(tag1);

        MvcResult mvcResult = mockMvc.perform(
                        post("/tag")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(tagJson)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        Tag savedTag = mapper.readValue(responseJson, Tag.class);
        assertEquals("Tag1", savedTag.getName());
        assertNotNull(savedTag.getId());
    }


    @Test
    public void testGetRandomTag() throws Exception {
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        when(tagServiceMock.findAll()).thenReturn(tags);
        when(tagServiceMock.count()).thenReturn(2L);

        MvcResult mvcResult = mockMvc.perform(get("/tag/rnd"))
                .andExpect(status().isOk())
                .andReturn();

        Tag result = mapper.readValue(mvcResult.getResponse().getContentAsString(), Tag.class);
        assertThat(tags, hasItem(result));
    }

    @Test
    public void testRemoveFromTask() throws Exception {
        IdResponseModel responseModel = new IdResponseModel();
        responseModel.setId(1);
        responseModel.setErrorMessage("Tag removed from task");
        when(tagServiceMock.removeFromTask(1, 1)).thenReturn(responseModel);

        MvcResult mvcResult = mockMvc.perform(delete("/tag/removeFromTask/1/1"))
                .andExpect(status().isOk())
                .andReturn();

        IdResponseModel result = mapper.readValue
                (mvcResult.getResponse().getContentAsString(), IdResponseModel.class);
        assertEquals(responseModel, result);
    }
}
