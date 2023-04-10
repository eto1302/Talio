package client.Services;

import client.messageClients.MessageAdmin;
import client.messageClients.MessageSender;
import client.user.UserData;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.stomp.*;


import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                    public void afterConnected(StompSession session,
                                               StompHeaders connectedHeaders) {

                    }

                    @Override
                    public void handleException(StompSession session, StompCommand command,
                                                StompHeaders headers, byte[] payload,
                                                Throwable exception) {

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

    @Test
    void disconnectTest(){

        connectionService.disconnect();

        verify(mockUserData, times(1))
            .disconnect();
    }

    @Test
    void getServerUrlsTest(){
        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
            List<String> expectedUrls = Arrays.asList("http://example.com", "https://example2.com");

            filesMock.when(() -> Files.readAllLines(any(Path.class)))
                .thenReturn(expectedUrls);

            List<String> actualUrls = connectionService.getServerUrls();
            assertEquals(expectedUrls, actualUrls);

            filesMock.verify(() -> Files.readAllLines(any(Path.class)), times(1));
        }
    }

    @Test
    void getServerUrlsTestException(){
        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
            List<String> expectedUrls = Arrays.asList("http://example.com", "https://example2.com");

            filesMock.when(() -> Files.readAllLines(any(Path.class)))
                .thenThrow(new IOException());

            List<String> resp = connectionService.getServerUrls();

            assertTrue(resp.isEmpty());

            filesMock.verify(() -> Files.readAllLines(any(Path.class)), times(1));
        }
    }
}
