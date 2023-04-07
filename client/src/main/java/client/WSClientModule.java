package client;

import client.messageClients.MessageSessionHandler;
import com.google.inject.AbstractModule;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

/**
 * Guice module for all Messaging related dependancies
 */
public class WSClientModule extends AbstractModule {

    /**
     * Url currently being used to connect to websockets
     * "host/ws"
     */
    private String wsUrl = "ws://localhost:8080/ws";
    private static WebSocketStompClient wsStompClient;

    @Override
    public void configure() {

        /**
         * Here we create a WebSocketStompClient
         * message converter -> MappingJackson2MessageConverter: converts json to our classes
         * task scheduler -> ThreadPoolTaskScehduler: needed for receipts
         */
        this.wsStompClient = new
                WebSocketStompClient(new StandardWebSocketClient());
        wsStompClient.setMessageConverter(new
                MappingJackson2MessageConverter());
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.afterPropertiesSet();
        wsStompClient.setTaskScheduler(scheduler);

        bind(WebSocketStompClient.class).toInstance(wsStompClient);
    }


    /**
     * Connects to the websocket server and returns a StompSession
     * @param url -> url to connect to
     * @return StompSession
     */
    public static StompSession connect(String url) {
        try {
            StompSession ss = wsStompClient.connect(url, new MessageSessionHandler()).get();
            ss.setAutoReceipt(true);
            return ss;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
