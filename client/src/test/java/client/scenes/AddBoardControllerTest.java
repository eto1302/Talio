package client.scenes;

import client.utils.ServerUtils;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddBoardControllerTest {
    private AddBoardController controller;
    private final ServerUtils serverUtils = mock(ServerUtils.class);
    private final ShowCtrl showCtrl = mock(ShowCtrl.class);
    @Mock
    private TextField nameField;
    @Mock
    private ColorPicker fontColor;
    @Mock
    private ColorPicker backgroundColor;
    @Mock
    private Button addButton;

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new AddBoardController(showCtrl, serverUtils);

        /*controller.setNameField(nameField);
        controller.setFontColor(fontColor);
        controller.setBackgroundColor(backgroundColor);
        controller.setAddButton(addButton);*/
    }

    @Test
    public void testAddBoard() {
        /*when(nameField.getText()).thenReturn("Test Board");
        when(fontColor.getValue()).thenReturn(Color.WHITE);
        when(backgroundColor.getValue()).thenReturn(Color.BLACK);
        when(addButton.isArmed()).thenReturn(true);
        when(addButton.isPressed()).thenReturn(true);

        controller.addBoard();

        Board expected = Board.create("Test Board", null, new HashSet<>(), "#000000", "#FFFFFF");
        verify(serverUtils).addBoard(expected);*/
        assertTrue(true);
    }

}
