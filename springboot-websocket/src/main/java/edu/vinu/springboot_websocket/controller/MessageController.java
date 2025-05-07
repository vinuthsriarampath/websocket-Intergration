package edu.vinu.springboot_websocket.controller;

import edu.vinu.springboot_websocket.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    public void sendMessage(Message message) {
        String destination = "/queue/messages-" + message.getTo();
        messagingTemplate.convertAndSend(destination, message);
    }
}
