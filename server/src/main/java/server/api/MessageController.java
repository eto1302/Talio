package server.api;

import commons.messaging.Messages.Message;
import commons.messaging.Messages.SuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    SimpMessagingTemplate template;

    @MessageMapping("/{dest}")
    public void send(@DestinationVariable("dest") String dest, Message msg){
        System.out.println("Message to app/"+dest+" forwarded to /topic/" + dest);
        String url = "/topic/"+dest;
        template.convertAndSend(url, msg);
    }


    @MessageMapping("/server")
    @SendToUser("/queue")
    public Message sendToServer(Message msg){
        System.out.println("/topic/server forwarded");
        msg.consume();
        return new SuccessMessage(true);
    }
}
