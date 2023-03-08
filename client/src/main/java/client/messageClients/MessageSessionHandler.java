package client.messageClients;

import commons.messaging.Messages.Message;
import commons.messaging.Messages.TestMessage;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class MessageSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders headers){
        session.subscribe("/topic/all", this);
        session.subscribe("/queue", this);
        session.send("ws/topic/server" , new TestMessage("connecting"));

    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload){
        Message msg = (Message) payload;
        msg.consume();
    }
}
