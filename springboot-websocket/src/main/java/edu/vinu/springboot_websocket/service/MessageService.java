package edu.vinu.springboot_websocket.service;

import edu.vinu.springboot_websocket.model.Message;

import java.util.List;

public interface MessageService {
    void saveAndSendMessage(Message message);
    void sendMessageToUser(String destination, Message message);
    List<Message> getMessagesBetweenUsers(String user1, String user2);
}
