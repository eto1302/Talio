package client.messageClients;

import org.springframework.messaging.simp.stomp.StompSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom MessageAdmin class
 * Currently only used to subscribe to new queues
 * to subscribe to a queue use
 *  messageAdmin.subscribe(queue)
 *      queue -> whatever queue you want to subscribe to (see WSServerConf javadoc)
 */
public class MessageAdmin {

    /**
     * Handler that receives and consumes messages
     */
    private MessageSessionHandler handler;

    /**
     * StompSession, used for all interactions with stomp
     * If you are debugging anything client side for messaging, pay attention to the StompSession
     */
    private StompSession session;

    private Map<String, StompSession.Subscription> subs = new HashMap<>();

    public MessageAdmin(StompSession session) {
        this.session = session;
        this.handler = new MessageSessionHandler();
    }

    /**
     * Subscribes to a queue using our Handler to process messages from said queue
     *  You need to subscribe to a queue that has a simpleMessageBroker configured (
     *  "/topic/{something}" will be right in 95% of use cases
     * @param queue
     */
    public void subscribe(String queue){
        StompSession.Subscription sub = session.subscribe(queue, handler);
        subs.put(queue, sub);
    }

    public void unsubscribe(String queue){
        if(!subs.containsKey(queue)) return;
        subs.get(queue).unsubscribe();
    }

    public void disconnect(){
        for(Map.Entry e : subs.entrySet()){
            StompSession.Subscription sub = (StompSession.Subscription) e.getValue();
            sub.unsubscribe();
        }
        session.disconnect();
    }

    public boolean isSubscribed(String q){
        return subs.containsKey(q);
    }
}
