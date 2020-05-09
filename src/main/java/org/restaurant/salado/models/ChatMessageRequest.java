package org.restaurant.salado.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.restaurant.salado.entities.ChatMessageType;

import java.time.LocalDate;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDate timestamp;
    private ChatMessageType messageType;
    private boolean read;

}
