package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Tag testTag;
    @BeforeEach
    void setUp() {
        testTag = Tag.create("testTag", "#000000");
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
        Tag newTag = Tag.create("testTag", "#000000");
        assertEquals(newTag, testTag);
    }

    @Test
    void testNotEquals() {
        Tag newTag = Tag.create("testTag1", "#000000");
        assertNotEquals(newTag, testTag);
    }

    @Test
    void testHashCode() {
        assertEquals(testTag.hashCode(), testTag.hashCode());
    }

    @Test
    void testWrongHashCode() {
        Tag newTag = Tag.create("testTag1", "#000000");
        assertNotEquals(newTag.hashCode(), testTag.hashCode());
    }

    @Test
    void testToString() {
        String stringRepresentation = "Tag{" +
                "id=" + testTag.getId() +
                ", name='" + "testTag" + '\'' +
                ", color='" + "#000000" + '\'' +
                '}';
        assertEquals(stringRepresentation, testTag.toString());
    }
}