package client.messageClients;

import com.google.inject.Inject;
import commons.messaging.Messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;

public class MessageSender {

    @Inject
    StompSession session;

    public void send(String queue, Message msg) {
        session.send(queue, msg);
    }
}
