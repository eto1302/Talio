package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Tag testTag;
    private Color color;
    @BeforeEach
    void setUp() {
        color = new Color("#000000", "#FFFFFF");
        testTag = new Tag("testTag", color);
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
        testTag.setColor(new Color("#FFFFFF", "#111111"));
        assertEquals(new Color("#FFFFFF", "#111111"), testTag.getColor());
    }


    @Test
    public void testGetBoard() {
        testTag.setBoard(new Board());
        assertEquals(new Board(), testTag.getBoard());
    }

    @Test
    public void testGetBoardId() {
        testTag.setBoardId(1);
        assertEquals(1, testTag.getBoardId());
    }

    @Test
    void testEquals() {
        Tag newTag = new Tag("testTag", color);
        assertEquals(newTag, testTag);
    }

    @Test
    void testNotEquals() {
        Tag newTag = new Tag("testTag1", color);
        assertNotEquals(newTag, testTag);
    }

    @Test
    void testHashCode() {
        assertEquals(testTag.hashCode(), testTag.hashCode());
    }

    @Test
    void testWrongHashCode() {
        Tag newTag = new Tag("testTag1", color);
        assertNotEquals(newTag.hashCode(), testTag.hashCode());
    }

    @Test
    void testToString() {
        String stringRepresentation = "Tag{id=0, name='testTag', " +
                "color='Color{id=0, fontColor='#000000', backgroundColor='#FFFFFF'}'}";
        assertEquals(stringRepresentation, testTag.toString());
    }
}