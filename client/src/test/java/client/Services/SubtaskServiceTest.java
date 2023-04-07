package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SubtaskServiceTest {
    @MockBean
    private UserData mockUserData = mock(UserData.class);
    @MockBean
    private ServerUtils mockServerUtils = mock(ServerUtils.class);
    private SubtaskService subtaskService;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        subtaskService = new SubtaskService(mockUserData, mockServerUtils);
    }
    @Test
    public void getSubtasksOrderedTest(){
        Subtask[] expected = new Subtask[1];
        when(mockServerUtils.getSubtasksOrdered(any(Integer.class)))
                .thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));
        List<Subtask> response = this.subtaskService.getSubtasksOrdered(1);
        verify(mockServerUtils, times(1))
                .getSubtasksOrdered(any(Integer.class));
        assertEquals(Arrays.asList(expected), response);
    }
}