package server.api;

import commons.messaging.Messages.Message;
import commons.messaging.Messages.SuccessMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/board/{dest}")
    @SendTo("/board/{dest}")
    public Message sendBoard(@DestinationVariable("dest") String dest, Message msg){
        return msg;
    }

    @MessageMapping("/topic/{dest}")
    @SendTo("/topic/{dest}")
    public Message sendTopic(@DestinationVariable("dest") String dest, Message msg){
        return msg;
    }

    @MessageMapping("/topic")
    @SendTo("/topic")
    public Message sendAll(Message msg){
        return msg;
    }

    @MessageMapping("/topic/server")
    @SendToUser("/queue")
    public Message sendToServer(Message msg){
        msg.consume();
        return new SuccessMessage(true);
    }
}
