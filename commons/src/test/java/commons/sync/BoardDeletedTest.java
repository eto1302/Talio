package commons.sync;

import commons.mocks.IServerUtils;
import commons.models.IdResponseModel;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardDeletedTest {
    private BoardDeleted boardDeleted;
    private IServerUtils mockServerUtils = mock(IServerUtils.class);

    @BeforeEach
    private void setup(){
        this.boardDeleted = new BoardDeleted();
    }

    @Test
    public void emptyConstructorTest(){
        boardDeleted = new BoardDeleted();
        assertNotNull(boardDeleted);
    }

    @Test
    public void sendToServerTest(){
        when(mockServerUtils.deleteBoard(any(Integer.class)))
                .thenReturn(new IdResponseModel(1, null));
        IdResponseModel response = this.boardDeleted.sendToServer(mockServerUtils);
        verify(mockServerUtils, times(1)).deleteBoard(any(Integer.class));

        assertTrue(response.getId() != -1);
        assertNull(response.getErrorMessage());
    }
    @Test
    public void getSendQueueTest(){
        assertEquals("/app/admin", this.boardDeleted.getSendQueue());
    }
}
