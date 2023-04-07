package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private Color boardColor;
    private Color listColor;

    @BeforeEach
    void setUp() {
        java.util.List<List> lists = new LinkedList<>();
        List list1 = new List();
        lists.add(list1);
        boardColor = new Color("#000000", "#FFFFFF");
        listColor = new Color("#000000", "#111111");
        java.util.List<Color> colors = new ArrayList<>();
        colors.add(boardColor);
        colors.add(listColor);

        board = new Board("Board1", "password123", lists,
                colors, new ArrayList<>());
        board.setBoardColor(boardColor);
        board.setListColor(listColor);
    }

    @Test
    void setLists() {
        java.util.List<List> newLists = new LinkedList<>();
        List list1 = new List();
        newLists.add(list1);

        board.setLists(newLists);

        assertEquals(newLists, board.getLists());
    }

    @Test
    void create() {
        java.util.List<List> expectedLists = new LinkedList<>();
        List list1 = new List();
        List list2 = new List();
        expectedLists.add(list1);
        expectedLists.add(list2);

        Board expectedBoard = new Board();
        expectedBoard.setName("Board1");
        expectedBoard.setPassword("password123");
        expectedBoard.setLists(expectedLists);

        java.util.List<Color> colors = new ArrayList<>();
        colors.add(boardColor);
        colors.add(listColor);

        Board actualBoard = new Board("Board1", "password123", expectedLists,
                colors, new ArrayList<>());

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    void getId() {
        assertEquals(0, board.getId());
    }

    @Test
    void getName() {
        assertEquals("Board1", board.getName());
    }

    @Test
    void getPassword() {
        assertEquals("password123", board.getPassword());
    }

    @Test
    void getLists() {
        java.util.List<List> expectedLists = new LinkedList<>();
        List list1 = new List();
        expectedLists.add(list1);

        assertEquals(expectedLists, board.getLists());
    }

    @Test
    void setName() {
        board.setName("NewBoardName");
        assertEquals("NewBoardName", board.getName());
    }

    @Test
    void setPassword() {
        board.setPassword("newpassword");
        assertEquals("newpassword", board.getPassword());
    }
    @Test
    void testEquals() {
        Board board1 = new Board();
        board1.setName("Board1");
        board1.setPassword("password123");

        java.util.List<List> lists = new LinkedList<>();
        List list1 = new List();
        List list2 = new List();
        lists.add(list1);
        lists.add(list2);

        board1.setLists(lists);
        java.util.List<Color> colors = new ArrayList<>();
        colors.add(boardColor);
        colors.add(listColor);
        board1.setColors(colors);

        assertEquals(board, board1);

        board1.setName("DifferentBoardName");
        assertNotEquals(board, board1);
    }

    @Test
    void testNotEquals() {
        Board board2 = new Board("board2", "password2",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertNotEquals(board, board2);
    }

    @Test
    void testHashCode() {
        Board board1 = new Board();
        board1.setName("Board1");
        board1.setPassword("password123");

        java.util.List<List> lists = new LinkedList<>();
        List list1 = new List();
        List list2 = new List();
        lists.add(list1);
        lists.add(list2);

        java.util.List<Color> colors = new ArrayList<>();
        colors.add(boardColor);
        colors.add(listColor);

        board1.setLists(lists);
        board1.setColors(colors);

        assertEquals(board.hashCode(), board1.hashCode());

        board1.setName("DifferentBoardName");
        assertNotEquals(board.hashCode(), board1.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "Board{id=0, name='Board1', password='password123', " +
                "inviteKey='null'}";

        assertEquals(expectedString, board.toString());
    }
}