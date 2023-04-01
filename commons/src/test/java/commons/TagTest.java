package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Tag testTag;
    private Color color;
    @BeforeEach
    void setUp() {
        color = Color.create("#000000", "#FFFFFF");
        testTag = Tag.create("testTag", color);
    }

    @Test
    void createTest(){
        Tag expected = new Tag();
        expected.setId(0);
        expected.setName("testTag");
        expected.setColor(color);
        assertEquals(expected, testTag);
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
        assertEquals(color, testTag.getColor());
    }

    @Test
    void setColor() {
        testTag.setColor(Color.create("#FFFFFF", "#111111"));
        assertEquals(Color.create("#FFFFFF", "#111111"), testTag.getColor());
    }

    @Test
    void testEquals() {
        Tag newTag = Tag.create("testTag", color);
        assertEquals(newTag, testTag);
    }

    @Test
    void testNotEquals() {
        Tag newTag = Tag.create("testTag1", color);
        assertNotEquals(newTag, testTag);
    }

    @Test
    void testHashCode() {
        assertEquals(testTag.hashCode(), testTag.hashCode());
    }

    @Test
    void testWrongHashCode() {
        Tag newTag = Tag.create("testTag1", color);
        assertNotEquals(newTag.hashCode(), testTag.hashCode());
    }

    @Test
    void testToString() {
        String stringRepresentation = "Tag{id=0, name='testTag', " +
                "color='Color{id=0, fontColor='#000000', backgroundColor='#FFFFFF'}'}";
        assertEquals(stringRepresentation, testTag.toString());
    }
}