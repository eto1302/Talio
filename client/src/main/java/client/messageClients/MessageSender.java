package client.messageClients;

import com.google.inject.Inject;
import commons.messaging.Messages.Message;
import org.springframework.messaging.simp.stomp.StompSession;

public class MessageSender {

    @Inject
    StompSession session;

    public void send(String queue, Message msg) {
        var bruh = session.send(queue, msg);
        bruh.addReceiptTask(new Receipt());
        bruh.addReceiptTask(new NoReceipt());
    }
}
