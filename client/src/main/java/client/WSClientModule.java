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

/**
 * Guice module for all Messaging related dependancies
 */
public class WSClientModule implements Module {

    /**
     * Url currently being used to connect to websockets
     * "host/ws"
     */
    public String wsUrl = "ws://localhost:8080/ws";

    @Override
    public void configure(Binder binder) {

        /**
         * Here we create a WebSocketStompClient
         * message converter -> MappingJackson2MessageConverter: converts json to our classes
         * task scheduler -> ThreadPoolTaskScehduler: needed for receipts
         */
        WebSocketStompClient wsStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        wsStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.afterPropertiesSet();
        wsStompClient.setTaskScheduler(scheduler);
        binder.bind(WebSocketStompClient.class).toInstance(wsStompClient);

        /**
         * Binding our custom handler
         * Also bound to StompSessionHandlerAdapter which is the default interface for session handlers
         */
        MessageSessionHandler handler = new MessageSessionHandler();
        binder.bind(StompSessionHandlerAdapter.class).toInstance(handler);
        binder.bind(MessageSessionHandler.class).toInstance(handler);

        /**
         * Here we:
         *  Connect to the server and asign our custom handler to the connection
         *  get the session from the connection (this is the only way to get the session)
         *  Add receipts to our session
         *  bind our session instance to StompSession
         */
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
