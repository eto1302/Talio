package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestTag;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    Tag testTag;
    @BeforeEach
    void setUp() {
        testTag = new Tag(1, "testTag", "#000000");
    }

    @Test
    void getId() {
        assertEquals(1, testTag.getId());
    }

    @Test
    void setId() {
        testTag.setId(2);
        assertEquals(2, testTag.getId());
    }

    @Test
    void getName() {
        assertEquals("testTag", testTag.getName());
    }

    @Test
    void setName() {
        testTag.setName("newTag");
        assertEquals("newTag", testTag.getName());
    }

    @Test
    void getColor() {
        assertEquals("#000000", testTag.getColor());
    }

    @Test
    void setColor() {
        testTag.setColor("#ffffff");
        assertEquals("#ffffff", testTag.getColor());
    }

    @Test
    void testEquals() {
        Tag newTag = new Tag(1, "testTag", "#000000");
        assertEquals(newTag, testTag);
    }

    @Test
    void testNotEquals() {
        Tag newTag = new Tag(2, "testTag", "#000000");
        assertNotEquals(newTag, testTag);
    }

    @Test
    void testHashCode() {
        Tag newTag = new Tag(1, "testTag", "#000000");
        assertEquals(newTag.hashCode(), testTag.hashCode());
    }

    @Test
    void testWrongHashCode() {
        Tag newTag = new Tag(2, "testTag", "#000000");
        assertNotEquals(newTag.hashCode(), testTag.hashCode());
    }

    @Test
    void testToString() {
        String stringRepresentation = "Tag{" +
                "id=" + 1 +
                ", name='" + "testTag" + '\'' +
                ", color='" + "#000000" + '\'' +
                '}';
        assertEquals(stringRepresentation, testTag.toString());
    }
}