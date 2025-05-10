package edu.vinu.springboot_websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vinu.springboot_websocket.entity.MessageEntity;
import edu.vinu.springboot_websocket.model.Message;
import edu.vinu.springboot_websocket.repository.MessageRepository;
import edu.vinu.springboot_websocket.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ObjectMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void saveAndSendMessage(Message message) {
        try {
            messageRepository.save(mapper.convertValue(message, MessageEntity.class));
            sendMessageToUser("/queue/messages-" + message.getRecipient(), message);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendMessageToUser(String destination, Message message) {
        try {
            messagingTemplate.convertAndSend(destination, message);
        } catch (MessagingException e) {
            throw new RuntimeException("Message sending failed", e);
        }
    }

    @Override
    public List<Message> getMessagesBetweenUsers(String user1, String user2) {
        return messageRepository.findMessagesBetweenUsers(user1, user2)
                .stream()
                .map(entity -> mapper.convertValue(entity, Message.class))
                .toList();
    }
}
