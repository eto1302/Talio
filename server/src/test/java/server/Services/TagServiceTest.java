package server.Services;

import commons.Board;
import commons.Tag;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.TagEditModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.BoardRepository;
import server.database.TagRepository;
import server.database.TaskRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TagServiceTest {

    @MockBean
    private transient BoardRepository boardRepository;
    @MockBean
    private transient TagRepository tagRepository;
    @MockBean
    private transient TaskRepository taskRepository;
    @Autowired
    private transient TagService tagService =
            new TagService(tagRepository, taskRepository, boardRepository);

    private Tag tag1, tag2;
    private Board board;
    private Task task;

    @BeforeEach
    void setUp() {
        tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("Tag1");
        tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("Tag2");
        board = new Board();
        task = new Task();
        task.setId(1);
    }

    @Test
    void testGetTagById() {
        when(tagRepository.getById(1)).thenReturn(tag1);

        Tag result = tagService.getTagById(1);
        assertNotNull(result);
        assertEquals(tag1, result);
    }

    @Test
    void testGetAllTagsByTask() {
        task.getTags().add(tag1);
        when(taskRepository.existsById(1)).thenReturn(true);
        when(taskRepository.getTaskById(1)).thenReturn(task);

        List<Tag> result = tagService.getAllTagsByTask(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tag1, result.get(0));
    }

//    @Test
//    void testGetAllTagsByTaskNoSuchElementException() {
//        when(taskRepository.existsById(1)).thenReturn(false);
//        assertThrows(NoSuchElementException.class, () -> tagService.getAllTagsByTask(1));
//    }

    @Test
    void testGetAllTagsByBoard() {
        int boardID = board.getId();
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        board.setTags(tags);
        when(boardRepository.existsById(boardID)).thenReturn(true);
        when(boardRepository.getBoardByID(boardID)).thenReturn(board);
        List<Tag> result = tagService.getAllTagsByBoard(boardID);
        assertEquals(tags, result);
    }

    @Test
    void addTagToTaskSuccess() {
        tag1.setBoard(board);

        when(tagRepository.getById(tag1.getId())).thenReturn(tag1);
        when(taskRepository.getTaskById(task.getId())).thenReturn(task);
        when(boardRepository.getBoardByID(tag1.getBoardId())).thenReturn(board);

        IdResponseModel response = tagService.addTagToTask(tag1, task.getId());

        assertEquals(tag1.getId(), response.getId());
        assertNull(response.getErrorMessage());
        assertTrue(tag1.getTasks().contains(task));
        assertTrue(tag1.getTaskIDs().contains(task.getId()));
        verify(tagRepository, times(1)).save(tag1);
    }

    @Test
    void addTagToTaskError() {
        when(tagRepository.getById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException("Tag not found"));

        IdResponseModel response = tagService.addTagToTask(new Tag(), task.getId());

        assertEquals(-1, response.getId());
        assertNotNull(response.getErrorMessage());
        verify(tagRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addTagError() {
        when(taskRepository.getTaskById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException("Task not found"));

        IdResponseModel response = tagService.addTagToTask(tag1, 1);

        assertEquals(-1, response.getId());
        assertNotNull(response.getErrorMessage());
        verify(tagRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addTagToBoardSuccess() {
        when(boardRepository.getBoardByID(board.getId())).thenReturn(board);

        IdResponseModel response = tagService.addTagToBoard(tag1, board.getId());

        assertThat(board.getTags(), hasItem(tag1));
        assertEquals(tag1.getBoard(), board);
        verify(tagRepository, times(1)).save(tag1);
        assertEquals(tag1.getId(), response.getId());
        assertNull(response.getErrorMessage());
    }

    @Test
    void addTagToBoardError() {
        int boardId = board.getId();
        when(boardRepository.getBoardByID(boardId)).thenReturn(null);

        IdResponseModel response = tagService.addTagToBoard(tag1, boardId);

        verify(boardRepository, never()).save(any(Board.class));
        verify(tagRepository, never()).save(any(Tag.class));
        assertEquals(-1, response.getId());
        assertNotNull(response.getErrorMessage());
    }

    @Test
    void testRemove() {
        task.setId(1);
        task.getTags().add(tag1);
        tag1.getTasks().add(task);
        tag1.getTaskIDs().add(1);
        when(tagRepository.getById(tag1.getId())).thenReturn(tag1);
        when(taskRepository.getTaskById(task.getId())).thenReturn(task);

        IdResponseModel response = tagService.removeFromTask(tag1.getId(), task.getId());

        assertEquals(tag1.getId(), response.getId());
        assertNull(response.getErrorMessage());
        assertFalse(task.getTags().contains(tag1));
        assertFalse(tag1.getTasks().contains(task));
        assertFalse(tag1.getTaskIDs().contains(1));
        verify(tagRepository, times(1)).save(tag1);
    }

    @Test
    void testRemoveInvalidTagId() {
        int invalidTagId = 100;
        Task task = new Task();
        task.setId(1);
        when(tagRepository.getById(invalidTagId))
                .thenThrow(new EntityNotFoundException("msg"));

        IdResponseModel response = tagService.removeFromTask(invalidTagId, task.getId());

        assertEquals(-1, response.getId());
        assertNotNull(response.getErrorMessage());
        verify(tagRepository, never()).save(any());
    }

    @Test
    void testRemoveFromTaskInvalidTaskId() {
        int invalidTaskId = 100;
        Mockito.when(taskRepository.getTaskById(invalidTaskId))
                .thenThrow(new EntityNotFoundException("msg"));

        IdResponseModel response = tagService.removeFromTask(tag1.getId(), invalidTaskId);

        assertEquals(-1, response.getId());
        assertNotNull(response.getErrorMessage());
        verify(tagRepository, never()).save(any());
    }

    @Test
    void testRemoveFromBoard() {
        when(tagRepository.getById(1)).thenReturn(tag1);

        IdResponseModel result = tagService.removeFromBoard(1);
        assertEquals(1, result.getId());
        assertNull(result.getErrorMessage());

        verify(tagRepository).delete(tag1);
    }

    @Test
    void testRemoveFromBoardNonExistTag() {
        when(tagRepository.getById(1))
                .thenThrow(new EntityNotFoundException("msg"));

        IdResponseModel result = tagService.removeFromBoard(1);
        assertEquals(-1, result.getId());
        assertNotNull(result.getErrorMessage());

        verify(tagRepository, never()).delete(any(Tag.class));
    }

    @Test
    void testEditTag() {
        tag1.setName("Tag1");
        TagEditModel model = new TagEditModel();
        model.setName("NewName");
        when(tagRepository.getById(tag1.getId())).thenReturn(tag1);

        IdResponseModel result = tagService.editTag(tag1.getId(), model);

        verify(tagRepository).save(tag1);
        assertEquals(tag1.getId(), result.getId());
        assertNull(result.getErrorMessage());
        assertEquals(model.getName(), tag1.getName());
    }

    @Test
    void editTagTagNotFound() {
        TagEditModel model = new TagEditModel();
        model.setName("NewName");
        int tagId = 1;
        when(tagRepository.getById(tagId))
                .thenThrow(new EntityNotFoundException("msg"));

        IdResponseModel result = tagService.editTag(tagId, model);

        verify(tagRepository, never()).save(any());
        assertEquals(-1, result.getId());
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void testFindAll() {
        when(tagRepository.findAll()).thenReturn(Arrays.asList(tag1, tag2));
        List<Tag> result = tagService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(tag1));
        assertTrue(result.contains(tag2));
    }

    @Test
    void testSave() {
        when(tagRepository.save(any(Tag.class))).thenReturn(tag1);
        Tag result = tagService.save(tag1);

        assertEquals(tag1, result);
    }

    @Test
    void testCount() {
        when(tagRepository.count()).thenReturn(2L);
        long result = tagService.count();

        assertEquals(2L, result);
    }
}