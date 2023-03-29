package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {
    private List list;

    @BeforeEach
    void setUp() {
        list = List.create("Test List", "#000000",
                "#FFFFFF", 1, Arrays.asList(new Task(), new Task()));
    }

    @Test
    void setTasks() {
        java.util.List<Task> newTasks = Arrays.asList(new Task(), new Task(), new Task());
        list.setTasks(newTasks);
        assertEquals(newTasks, list.getTasks());
    }

    @Test
    void create() {
        List newList = List.create("Test List", "#000000",
                "#FFFFFF",1,  Arrays.asList(new Task()));
        assertNotNull(newList);
        assertEquals("Test List", newList.getName());
        assertEquals(0, newList.getId());
        assertEquals(Arrays.asList(new Task()), newList.getTasks());
    }

    @Test
    void getName() {
        assertEquals("Test List", list.getName());
    }

    @Test
    void getId() {
        assertEquals(0, list.getId());
    }

    @Test
    void getBoard() {
        assertNull(list.getBoard());
    }

    @Test
    void setName() {
        list.setName("New Name");
        assertEquals("New Name", list.getName());
    }

    @Test
    void setBoard() {
        Board board = new Board();
        list.setBoard(board);
        assertEquals(board, list.getBoard());
    }

    @Test
    void testEquals() {
        List newList = List.create("Test List", "#000000",
                "#FFFFFF",1,  Arrays.asList(new Task(), new Task()));
        assertEquals(list, newList);
        assertEquals(list.hashCode(), newList.hashCode());
    }

    @Test
    void testHashCode() {
        List newList = List.create("Test List", "#000000",
                "#FFFFFF",1, Arrays.asList(new Task(), new Task()));
        assertEquals(list.hashCode(), newList.hashCode());
    }

    @Test
    void testToString() {
        String expected = "List{id=0, name='Test List', backgroundColor='#000000'," +
                " fontColor='#FFFFFF', boardId=1";
        assertEquals(expected, list.toString());
    }
}