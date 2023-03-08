package client.messageClients;

import commons.messaging.Messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    StompSession session;

    public void send(String queue, Message msg) {
        session.send(queue, msg);
    }
}
