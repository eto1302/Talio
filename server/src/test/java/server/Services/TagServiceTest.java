package server.Services;

import commons.Color;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestTagRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TagServiceTest {

    private TestTagRepository repo;
    private TagService tagService;

    @BeforeEach
    void setUp() {
        this.repo = new TestTagRepository();
        tagService = new TagService(repo);
    }

    @Test
    void getTagById() {
        var testTag = getTestTag("test");
        tagService.save(testTag);
        assertEquals(testTag, tagService.getTagById(testTag.getId()));
    }

    @Test
    void getMissingTagById() {
        assertNull(tagService.getTagById(0));
    }

    @Test
    void findAll() {
        var testTag = getTestTag("test");
        tagService.save(testTag);
        var expected = new ArrayList<Tag>();
        expected.add(testTag);
        assertEquals(expected, tagService.findAll());
    }

    @Test
    void findAllEmpty() {
        assertEquals(new ArrayList<Tag>(), tagService.findAll());
    }

    @Test
    void save() {
        var testTag = getTestTag("test");
        tagService.save(testTag);
        assertEquals(testTag, tagService.getTagById(testTag.getId()));
    }

    @Test
    void count() {
        var testTag = getTestTag("test");
        tagService.save(testTag);
        var testTag1 = getTestTag("test");
        tagService.save(testTag);
        var testTag2 = getTestTag("test");
        tagService.save(testTag);
        assertEquals(3, tagService.count());
    }

    private static Tag getTestTag(String q) {
        return Tag.create(q, Color.create("#000000", "#FFFFFF"));
    }
}