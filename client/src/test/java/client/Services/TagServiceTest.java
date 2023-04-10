package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import commons.models.IdResponseModel;
import commons.sync.TagCreated;
import commons.sync.TagEdited;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TagServiceTest {
    @MockBean
    private UserData mockUserData = mock(UserData.class);
    @MockBean
    private ServerUtils mockServerUtils = mock(ServerUtils.class);
    private TagService tagService;
    private Board board;
    private Tag tag;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        tagService = new TagService(mockUserData, mockServerUtils);
        board = new Board("board", null, null, null, new ArrayList<>());
        tag = new Tag("test", new commons.Color("#FFFFFF", "#000000"));
        when(mockUserData.getCurrentBoard()).thenReturn(board);
    }

    @Test
    public void getTagByTaskTest() {
        ResponseEntity<Tag[]> responseEntity = new ResponseEntity<>(
            new Tag[]{tag},
            HttpStatus.OK);
        when(mockServerUtils.getTagsByTask(any(Integer.class))).thenReturn(responseEntity);

        List<Tag> resp = tagService.getTagByTask(1);

        assertNotNull(resp);
        assertEquals(1, resp.size());
        assertEquals(tag, resp.get(0));
    }

    @Test
    public void getTagByBoardTest() {
        ResponseEntity<Tag[]> responseEntity = new ResponseEntity<>(
            new Tag[]{tag},
            HttpStatus.OK);
        when(mockServerUtils.getTagByBoard(any(Integer.class))).thenReturn(responseEntity);

        List<Tag> resp = tagService.getTagByBoard();

        assertNotNull(resp);
        assertEquals(1, resp.size());
        assertEquals(tag, resp.get(0));
    }

    @Test
    public void addTagTest() {
        IdResponseModel idResponseModel = new IdResponseModel(1, null);
        when(mockUserData.updateBoard(any(TagCreated.class))).thenReturn(idResponseModel);

        IdResponseModel resp = tagService.addTag("test", Color.BLACK, Color.WHITE);

        assertNotNull(resp);
        assertTrue(resp.getId() != -1);
        assertNull(resp.getErrorMessage());
    }

    @Test
    public void editTagTest() {
        tag.setBoardId(1);
        IdResponseModel idResponseModel = new IdResponseModel(1, null);
        when(mockUserData.updateBoard(any(TagEdited.class))).thenReturn(idResponseModel);

        IdResponseModel resp = tagService.editTag("test-edit", Color.BLACK, Color.WHITE, tag);

        assertNotNull(resp);
        assertTrue(resp.getId() != -1);
        assertNull(resp.getErrorMessage());
    }

    @Test
    public void getTagByTaskBadRequestTest() {
        ResponseEntity<Tag[]> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(mockServerUtils.getTagsByTask(any(Integer.class))).thenReturn(responseEntity);

        List<Tag> resp = tagService.getTagByTask(1);

        assertNotNull(resp);
        assertTrue(resp.isEmpty());
    }

    @Test
    public void getTagByBoardBadRequestTest() {
        ResponseEntity<Tag[]> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(mockServerUtils.getTagByBoard(any(Integer.class))).thenReturn(responseEntity);

        List<Tag> resp = tagService.getTagByBoard();

        assertNotNull(resp);
        assertTrue(resp.isEmpty());
    }

    @Test
    public void addTagFailsTest() {
        IdResponseModel idResponseModel = new IdResponseModel(-1, "Error message");
        when(mockUserData.updateBoard(any(TagCreated.class))).thenReturn(idResponseModel);

        IdResponseModel resp = tagService.addTag("test", Color.BLACK, Color.WHITE);

        assertNotNull(resp);
        assertEquals(-1, resp.getId());
        assertNotNull(resp.getErrorMessage());
    }

    @Test
    public void editTagFailsTest() {
        IdResponseModel idResponseModel = new IdResponseModel(-1, "Error message");
        when(mockUserData.updateBoard(any(TagEdited.class))).thenReturn(idResponseModel);

        IdResponseModel resp = tagService.editTag("test-edit", Color.BLACK, Color.WHITE, tag);

        assertNotNull(resp);
        assertEquals(-1, resp.getId());
        assertNotNull(resp.getErrorMessage());
    }
}
