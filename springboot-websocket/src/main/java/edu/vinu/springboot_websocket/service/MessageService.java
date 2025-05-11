package edu.vinu.springboot_websocket.service;

import edu.vinu.springboot_websocket.model.Message;
import edu.vinu.springboot_websocket.requestDto.MessageStatusUpdate;
import edu.vinu.springboot_websocket.util.MessageStatus;

import java.util.List;

public interface MessageService {
    void saveAndSendMessage(Message message);
    void sendMessageToUser(String destination, Message message);
    List<Message> getMessagesBetweenUsers(String user1, String user2);

    void updateMessageStatus(Long messageId, MessageStatus status);
    void broadcastStatusUpdate(MessageStatusUpdate statusUpdate);
}
