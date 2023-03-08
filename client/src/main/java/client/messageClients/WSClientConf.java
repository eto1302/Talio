package client.messageClients;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class WSClientConf {

    public String wsUrl = "ws://localhost:8080/ws";

    @Bean
    public WebSocketClient webSocketClient(){
        return new StandardWebSocketClient();
    }

    @Bean
    public StompSessionHandler stompSessionHandler(){
        return new MessageSessionHandler();
    }

    @Bean
    public WebSocketStompClient webSocketStompClient(WebSocketClient client) {
        WebSocketStompClient wsStompClient = new WebSocketStompClient(client);
        wsStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        return wsStompClient;
    }

    @Bean
    public StompSession stompSession(WebSocketStompClient stompClient, StompSessionHandler handler) throws ExecutionException, InterruptedException {
        StompSession ss = stompClient.connect(wsUrl, handler).get();
        return ss;
    }

}
