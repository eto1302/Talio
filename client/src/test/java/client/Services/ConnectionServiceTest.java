package client.Services;

import client.messageClients.MessageAdmin;
import client.messageClients.MessageSender;
import client.user.UserData;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.stomp.*;


import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

public class ConnectionServiceTest {
    @MockBean
    private UserData mockUserData = mock(UserData.class);
    @MockBean
    private ServerUtils mockServerUtils = mock(ServerUtils.class);
    private ConnectionService connectionService;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        connectionService = new ConnectionService(mockUserData, mockServerUtils);
    }

    @Test
    public void setupTest(){
        this.connectionService.setUrl("testing");
        verify(mockServerUtils, times(1)).setUrl(any(String.class));
    }

    @Test
    public void setWsConfigTest(){
        this.connectionService.setWsConfig(
                new DefaultStompSession(new StompSessionHandler() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

                    }

                    @Override
                    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

                    }

                    @Override
                    public void handleTransportError(StompSession session, Throwable exception) {

                    }

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return null;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {

                    }
                }, new StompHeaders()));
        verify(mockUserData, times(1))
                .setWsConfig(any(MessageAdmin.class), any(MessageSender.class));
    }
}
