package client.messageClients;

import commons.messaging.Messages.Message;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MessageSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders headers){
        session.subscribe("/topic/all", this);
//        session.subscribe("/topic/test/res", this);
        session.subscribe("/queue", this);
        session.subscribe("/topic/test", this);
//        session.subscribe("/topic/{dest}", this);
        session.subscribe("/topic", this);
//        session.send("ws/topic/server" , new TestMessage("connecting"));
    }

    @Override
    public Type getPayloadType(StompHeaders headers){
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload){
        Message msg = (Message) payload;
        msg.consume();
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception){
        System.out.println("Client transport error: error "+ exception.getMessage());
        throw new RuntimeException(exception.getMessage());
    }
}
