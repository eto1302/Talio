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

    @MessageMapping("/{dest}")
    @SendTo("topic/{dest}")
    public Message send(@DestinationVariable("dest") String dest, Message msg){
        System.out.println("/board/{dest} forwarded");
        System.out.println("{Dest}:" + dest);
        return msg;
    }

//    @MessageMapping("app/topic/{dest}")
//    @SendTo("/topic/{dest}")
//    public Message sendTopic(@DestinationVariable("dest") String dest, Message msg){
//        System.out.println("/topic/{dest} forwarded");
//        System.out.println("{Dest}:" + dest);
//        return msg;
//    }

//    @MessageMapping("/topic")
//    @SendTo("/topic")
//    public Message sendAll(Message msg){
//        System.out.println("/topic forwarded");
//        return msg;
//    }
//    @MessageMapping("/")
//    public void test(Message msg){
//        System.out.println("hahaha");
//    }

    @MessageMapping("/server")
    @SendToUser("/queue")
    public Message sendToServer(Message msg){
        System.out.println("/topic/server forwarded");
        msg.consume();
        return new SuccessMessage(true);
    }
}
