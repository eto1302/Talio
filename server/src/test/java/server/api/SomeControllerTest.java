package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SomeControllerTest {
    private SomeController controller;

    @BeforeEach
    public void setup() {
        controller = new SomeController();
    }

    @Test
    public void CheckOutput() {
        var actual = controller.index();
        assertEquals("Hello world!", actual);
    }
}
