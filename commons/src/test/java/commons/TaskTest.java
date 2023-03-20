package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task1;
    private Task task2;
    @BeforeEach
    void setUp() {
        task1 = new Task();
        task1.setId(1);
        task1.setTitle("Title 1");
        task1.setDescription("Description 1");

        task2 = new Task();
        task2.setId(2);
        task2.setTitle("Title 2");
        task2.setDescription("Description 2");
    }

    @Test
    void create() {
        Task task = Task.create("Description", "Title");
        assertNotNull(task);
        assertEquals(0, task.getId());
        assertEquals("Description", task.getDescription());
        assertEquals("Title", task.getTitle());
    }

    @Test
    void setId() {
        task1.setId(3);
        assertEquals(3, task1.getId());
    }

    @Test
    void setDescription() {
        task1.setDescription("New Description");
        assertEquals("New Description", task1.getDescription());
    }

    @Test
    void setTitle() {
        task1.setTitle("New Title");
        assertEquals("New Title", task1.getTitle());
    }

    @Test
    void setList() {
        List list = new List();
        task1.setList(list);
        assertEquals(list, task1.getList());
    }

    @Test
    void setTag() {
        Tag tag = new Tag();
        task1.setTag(tag);
        assertEquals(tag, task1.getTag());
    }

    @Test
    void testEquals() {
        assertTrue(task1.equals(task1));
        assertFalse(task1.equals(null));
        assertFalse(task1.equals(new Object()));
        assertFalse(task1.equals(task2));
        assertFalse(task1.equals(Task.create("Description 1", "Title 2")));
        assertFalse(task1.equals(Task.create("Description 2", "Title 1")));
    }

    @Test
    void testHashCode() {
        assertNotEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void testToString() {
        String expected = new String("Task{id=1, description='Description 1', title='Title 1'}");
        assertEquals(expected, task1.toString());
    }
}