package commons.messaging;

import commons.messaging.Messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Service
public class MessageAdmin {

    @Autowired
    StompSession session;

    Map<String, String> queues = new HashMap<>();


    public void subscribe(String queue){
        String id  = session.subscribe(queue, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return null;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message msg = (Message) payload;
                msg.consume();
            }
        }).getSubscriptionId();

        queues.put(queue, id);
    }
}
