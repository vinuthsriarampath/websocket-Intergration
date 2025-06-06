package edu.vinu.springboot_websocket.controller;

import edu.vinu.springboot_websocket.model.Message;
import edu.vinu.springboot_websocket.requestDto.MessageStatusUpdate;
import edu.vinu.springboot_websocket.response.ApiResponse;
import edu.vinu.springboot_websocket.service.MessageService;
import edu.vinu.springboot_websocket.util.MessageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/send")
    public void sendMessage(@RequestBody Message message) {
        messageService.saveAndSendMessage(message);
    }

    // Handle delivery acknowledgment
    @MessageMapping("/delivered")
    public void handleDelivered(@RequestBody MessageStatusUpdate statusUpdate) {
        messageService.updateMessageStatus(statusUpdate.getMessageId(), MessageStatus.DELIVERED);
        messageService.broadcastStatusUpdate(statusUpdate);
    }

    // Handle read acknowledgment
    @MessageMapping("/read")
    public void handleRead(@RequestBody MessageStatusUpdate statusUpdate) {
        messageService.updateMessageStatus(statusUpdate.getMessageId(), MessageStatus.READ);
        messageService.broadcastStatusUpdate(statusUpdate);
    }

    @GetMapping("/history/{user1}/{user2}")
    public ResponseEntity<ApiResponse> getChatHistory(@PathVariable String user1, @PathVariable String user2) {
        try {
            List<Message> messages = messageService.getMessagesBetweenUsers(user1, user2);
            if (!messages.isEmpty()) {
                return ResponseEntity.status(200).body(new ApiResponse("Messages Found", messages));
            } else {
                return ResponseEntity.status(200).body(new ApiResponse("No Messages Found", messages));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error Occurred", null));
        }
    }
}
