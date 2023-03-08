package client;

import client.messageClients.MessageSessionHandler;
import com.google.inject.Binder;
import com.google.inject.Module;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

public class WSClientModule implements Module {

    public String wsUrl = "ws://localhost:8080/ws";

    @Override
    public void configure(Binder binder) {

        //Bind websocket stomp client and set message converter
        WebSocketStompClient wsStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        wsStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        binder.bind(WebSocketStompClient.class).toInstance(wsStompClient);

        //Bind
        try {
            StompSession ss = wsStompClient.connect(wsUrl, new MessageSessionHandler()).get();
            binder.bind(StompSession.class).toInstance(ss);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
