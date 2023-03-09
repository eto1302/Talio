package client;

import client.messageClients.MessageSessionHandler;
import com.google.inject.Binder;
import com.google.inject.Module;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
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
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.afterPropertiesSet();
        wsStompClient.setTaskScheduler(scheduler);
        binder.bind(WebSocketStompClient.class).toInstance(wsStompClient);

        MessageSessionHandler handler = new MessageSessionHandler();
        binder.bind(StompSessionHandlerAdapter.class).toInstance(handler);
        binder.bind(MessageSessionHandler.class).toInstance(handler);

        //Bind
        try {
            StompSession ss = wsStompClient.connect(wsUrl, handler).get();
            ss.setAutoReceipt(true);
            binder.bind(StompSession.class).toInstance(ss);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
