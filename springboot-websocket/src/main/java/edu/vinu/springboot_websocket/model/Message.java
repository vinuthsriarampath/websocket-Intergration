package edu.vinu.springboot_websocket.model;

import edu.vinu.springboot_websocket.util.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Long id;
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime timestamp;
    private MessageStatus status;
}
