package edu.vinu.springboot_websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vinu.springboot_websocket.entity.MessageEntity;
import edu.vinu.springboot_websocket.model.Message;
import edu.vinu.springboot_websocket.repository.MessageRepository;
import edu.vinu.springboot_websocket.requestDto.MessageStatusUpdate;
import edu.vinu.springboot_websocket.service.MessageService;
import edu.vinu.springboot_websocket.util.MessageStatus;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            message.setStatus(MessageStatus.SENT);
            MessageEntity entity = messageRepository.save(mapper.convertValue(message, MessageEntity.class));
            // Convert back to Message for sending
            Message savedMessage = mapper.convertValue(entity, Message.class);
            sendMessageToUser("/queue/messages-" + message.getSender(), savedMessage);
            sendMessageToUser("/queue/messages-" + message.getRecipient(), savedMessage);
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

    @Override
    @Transactional
    public void updateMessageStatus(Long messageId, MessageStatus status) {
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoResultException("Message not found"));
        message.setStatus(status);
        messageRepository.save(message);
    }

    @Override
    public void broadcastStatusUpdate(MessageStatusUpdate statusUpdate) {
        MessageEntity message = messageRepository.findById(statusUpdate.getMessageId())
                .orElseThrow(() -> new NoResultException("Message not found"));
        messagingTemplate.convertAndSend("/queue/status-" + message.getSender(),statusUpdate);
    }
}
