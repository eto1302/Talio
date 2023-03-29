package client.messageClients;

import commons.messaging.Messages.Message;
import javafx.application.Platform;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

/**
 * Custom class for handling and consuming messages and errors with sending them
 */
public class MessageSessionHandler extends StompSessionHandlerAdapter {

    /**
     * Setup code to execute after a session has connected to the server
     * Currently just subscribed
     * @param session the client STOMP session
     * @param headers the STOMP CONNECTED frame headers
     */
    @Override
    public void afterConnected(StompSession session, StompHeaders headers){
        session.subscribe("/topic/all", this);
        session.subscribe("/queue", this);
        session.subscribe("/topic/test", this);
        session.subscribe("/topic", this);
    }

    /**
     * returns type for the payload (message) received
     * In our case this will alwasy be a subtype of Message
     * Please don't change this, nothing works otherwise
     * @param headers the headers of a message
     * @return
     */
    @Override
    public Type getPayloadType(StompHeaders headers){
        return Message.class;
    }

    /**
     * Handles all recieved Messages
     * Currently just calls consume() on the message
     * @param headers the headers of the frame
     * @param payload the payload, or {@code null} if there was no payload
     */
    @Override
    public void handleFrame(StompHeaders headers, Object payload){
        Message msg = (Message) payload;
        System.out.println("Received message: "+ msg);
        Platform.runLater(() -> msg.consume());
    }

    /**
     * Handles any transport errors, f.x dropped messages
     * @param session the client STOMP session
     * @param exception the exception that occurred
     */
    @Override
    public void handleTransportError(StompSession session, Throwable exception){
        System.out.println("Client transport error: error "+ exception.getMessage());
        throw new RuntimeException(exception.getMessage());
    }
}
