package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColorTest {
    private Board board;
    private Color boardColor;

    @BeforeEach
    void setUp() {
        boardColor = Color.create("#000000", "#FFFFFF");

        board = Board.create("Board1", "password123", new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
    }
    @Test
    public void getBoard(){
        this.boardColor.setBoard(board);
        assertEquals(board, boardColor.getBoard());
    }
}
