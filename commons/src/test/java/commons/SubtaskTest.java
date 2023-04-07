package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private Subtask subtask1, subtask2;

    @BeforeEach
    public void setup(){
        subtask1 = new Subtask("Subtask1", true, 1);
        subtask1.setIndex(0);
        subtask2 = new Subtask("Subtask2", false, 2);
        subtask2.setIndex(0);
    }

    @Test
    void create() {
        Subtask subtask = new Subtask("New Subtask", false, 1);
        assertNotNull(subtask);
        assertEquals("New Subtask", subtask.getDescription());
        assertFalse(subtask.isChecked());
        assertEquals(1, subtask.getTaskID());
    }

    @Test
    void getId() {
        assertEquals(0,subtask1.getId());
    }

    @Test
    void getDescription() {
        assertEquals("Subtask2", subtask2.getDescription());
    }

    @Test
    void isChecked() {
        assertTrue(subtask1.isChecked());
    }

    @Test
    void getTaskID() {
        assertEquals(1, subtask1.getTaskID());
    }

    @Test
    void getIndex(){
        assertEquals(0, subtask1.getIndex());
    }

    @Test
    void getTask() {
        Task task = new Task();
        subtask2.setTask(task);
        assertEquals(task, subtask2.getTask());
    }

    @Test
    void setId() {
        subtask2.setId(10);
        assertEquals(10, subtask2.getId());
    }

    @Test
    void setDescription() {
        subtask2.setDescription("Hello world");
        assertEquals("Hello world", subtask2.getDescription());
    }

    @Test
    void setChecked() {
        subtask1.setChecked(false);
        assertFalse(subtask1.isChecked());
    }

    @Test
    void setTaskID() {
        subtask1.setTaskID(2);
        assertEquals(2, subtask1.getTaskID());
    }

    @Test
    void setIndex(){
        subtask1.setIndex(12);
        assertEquals(12, subtask1.getIndex());
    }

    @Test
    void setTask() {
        Task task = new Task();
        subtask1.setTask(task);
        assertEquals(task, subtask1.getTask());
    }

    @Test
    void testEquals() {
        assertNotEquals(subtask1, subtask2);
        Task task = new Task();
        subtask1.setTask(task);
        subtask2.setTask(task);
        subtask1.setChecked(false);
        subtask1.setDescription("Subtask2");
        subtask1.setTaskID(2);

        assertEquals(subtask1,subtask2);
    }

    @Test
    void testHashCode() {
        Subtask subtask = new Subtask();
        subtask.setDescription("Subtask1");
        subtask.setChecked(true);
        subtask.setTaskID(1);
        subtask.setIndex(0);

        assertEquals(subtask.hashCode(), subtask1.hashCode());
        assertNotEquals(subtask1.hashCode(), subtask2.hashCode());
    }

    @Test
    void testToString() {
        String expected = "Subtask{id=0index=0, description='Subtask1', checked=true, taskID=1}";
        assertEquals(expected, subtask1.toString());
    }
}