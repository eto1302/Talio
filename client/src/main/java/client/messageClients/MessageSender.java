package client.messageClients;

import commons.messaging.Messages.Message;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * Custom class for sending Messages
 * to send a message call messageSender.send(queue, Message)
 *  queue -> whatever queue you want to send the message to (see WSServerConf javadoc)
 *  Message -> your custom implementation of Message (see Message javadoc)
 */
public class MessageSender {

    /**
     * StompSession, used for all interactions with stomp
     * Injected by guice
     * If you are debugging anything client side for messaging, pay attention to the StompSession
     */
    private StompSession session;

    public MessageSender(StompSession session) {
        this.session = session;
    }

    /**
     * Send your Message to a queue
     * You can send a message to:
     *  A simple message broker: "/topic/{something}"
     *  MessageController: "app/{something}"
     * The message will be consumed by any clients that have subscribed to the queue
     * We also add custom Classes to handle recieipts from the server
     * @param queue
     * @param msg
     */
    public void send(String queue, Message msg) {
        var receiptable = session.send(queue, msg);
        receiptable.addReceiptTask(new Receipt());
        //receiptable.addReceiptLostTask(new NoReceipt());
    }
}
