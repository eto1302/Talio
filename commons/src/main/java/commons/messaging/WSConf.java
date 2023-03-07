package commons.messaging;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.net.URI;
import java.util.concurrent.ExecutionException;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableWebSocketMessageBroker
public class WSConf implements WebSocketMessageBrokerConfigurer {

    public String wsUrl = "ws://localhost:8080/ws";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/board", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws");
        registry.addEndpoint("/ws").withSockJS();
    }

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
