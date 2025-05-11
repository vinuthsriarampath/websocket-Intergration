package edu.vinu.springboot_websocket.requestDto;

import edu.vinu.springboot_websocket.util.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageStatusUpdate {
    private Long messageId;
    private MessageStatus status;
}
