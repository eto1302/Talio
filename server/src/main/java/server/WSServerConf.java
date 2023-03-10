package server;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Websocket Server configuration
 * Turns our server into a Websocket message broker
 * And sets everything up
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableWebSocketMessageBroker
public class WSServerConf implements WebSocketMessageBrokerConfigurer {

    /**
     * Adds simple brokers to "/topic" and "/queue",
     * messages sent here will be forwarded straight to their subscribers
     *  Example:
     *  We send a message to "/topic/wow",
     *  this sends the message to all users subscribed to "/topic/wow"
     * Adds "/app" as a application destination prefix,
     * messages sent here will go to our MessageController
     *  Example:
     *  We send a message to "/app/crazy",
     *  the message goes to MessageController, is handled by the method with the
     *  @MessageMapping that matches the path, and
     *  then forwarded to some queue (in this case "/topic/crazy")
     * @param config gotten from spring
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers endpoint used to start the websocket connection
     * in our case we use "http://localhost:8080/ws"
     * @param registry wired by spring
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws");
        registry.addEndpoint("/ws").withSockJS();
    }
}
