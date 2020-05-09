package org.restaurant.salado.configuration;

import org.restaurant.salado.entities.ChatMessage;
import org.restaurant.salado.entities.ChatMessageType;
import org.restaurant.salado.providers.KafkaConstants;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

/**
 * @author Haytham DAHRI
 */
@Component
public class ChatMessageListener {

    private SimpMessagingTemplate template;
    private UserService userService;

    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listen(ChatMessage chatMessage) {
        System.out.println("sending via kafka listener..");
        template.convertAndSend("/topic/public", chatMessage);
    }

    /**
     * Handle Events
     * @param event: SessionDisconnectEvent
     */
    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("user");
        String privateUserId = (String) headerAccessor.getSessionAttributes().get("private-user");
        if (userId != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessageType(ChatMessageType.LEAVE);
            chatMessage.setSender(this.userService.getUser(Long.parseLong(userId)));
            template.convertAndSend("/topic/public", chatMessage);
        }

        if (privateUserId != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessageType(ChatMessageType.LEAVE);
            chatMessage.setSender(this.userService.getUser(Long.parseLong(privateUserId)));
            template.convertAndSend("/queue/reply", chatMessage);
        }
    }

}
