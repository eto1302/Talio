package server.api;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Services.TagService;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class TagControllerTest {

    private int nextInt;
    private TagController controller;
    private MyRandom random;
    private TestTagRepository tagRepo;
    private TestTaskRepository taskRepo;
    private TestBoardRepository boardRepo;

    @BeforeEach
    public void setup() {
        random = new MyRandom();
        tagRepo = new TestTagRepository();
        taskRepo = new TestTaskRepository();
        boardRepo = new TestBoardRepository();
        controller = new TagController(new TagService(tagRepo, taskRepo, boardRepo));
        controller.setRandom(random);
    }

    @Test
    public void cannotAddNullName() {
        var actual = controller.add(getTestTag(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addTag() {
        var actual = controller.add(getTestTag("test"));
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void getEmptyFindALl(){
        var actual = controller.getAll();
        var expected = new ArrayList<Tag>();
        assertEquals(expected, actual);
    }

    @Test
    public void findAll(){
        Tag testTag = getTestTag("test");
        controller.add(testTag);
        var actual = controller.getAll();
        var expected = new ArrayList<Tag>();
        expected.add(testTag);
        assertEquals(expected, actual);
    }

    @Test
    public void emptyGetById(){
        var actual = controller.getTagById(22);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getById(){
        var testTag = getTestTag("test");
        controller.add(testTag);

        var actual = controller.getTagById(tagRepo.getTags().get(0).getId()).getBody();

        assertEquals(testTag, actual);
    }

    @Test
    public void getRandom() {
        controller.add(getTestTag("test"));
        controller.add(getTestTag("test1"));
        this.nextInt = 1;
        var actual = controller.getRandom();

        assertEquals("test1", actual.getBody().getName());
    }

    @Test
    public void databaseIsUsed() {
        controller.add(getTestTag("test"));
        tagRepo.getCalledMethods().contains("save");
    }

    private static Tag getTestTag(String q) {
        return Tag.create(q, "#000000");
    }

    @SuppressWarnings("serial")
    public class MyRandom extends Random {
        private boolean wasCalled = false;

        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }
    }
}
