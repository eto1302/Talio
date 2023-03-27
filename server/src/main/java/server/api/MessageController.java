package server.api;

import commons.messaging.Messages.Message;
import commons.messaging.Messages.SuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


/**
 * Controller that forwards Messages to clients
 */
@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * Forwards messages sent to "app/{dest}" to "topic/{dest}"
     * @param dest value inside {dest}
     * @param msg Message sent from client
     */
    @MessageMapping("/{dest}")
    public void send(@DestinationVariable("dest") String dest, Message msg){
        System.out.println("Message to app/"+dest+" forwarded to /topic/" + dest);
        String url = "/topic/"+dest;
        System.out.println("Message: "+msg);
        template.convertAndSend(url, msg);
    }


    /**
     * Mapping for sending messages to the server, still a WIP
     * Eventually should process a Message, and return a response Message
     * @param msg Message sent from client
     * @return msg
     */
    @MessageMapping("/server")
    @SendToUser("/queue")
    public Message sendToServer(Message msg){
        System.out.println("/topic/server forwarded");
        msg.consume();
        return new SuccessMessage(true);
    }
}
