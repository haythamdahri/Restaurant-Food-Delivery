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

    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Handle private Messages
     * @param chatMessage: ChatMessage Object
     */
    @KafkaListener(
            topics = KafkaConstants.KAFKA_PRIVATE_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listenToPrivateMessages(ChatMessage chatMessage) {
        System.out.println("sending via kafka listener | Private Message ...");
        this.template.convertAndSendToUser(chatMessage.getReceiver().getId().toString(), "/reply", chatMessage);
    }

    /**
     * Listen to users joining and leave
     * @param chatMessage: ChatMessage Object
     */
    @KafkaListener(
            topics = KafkaConstants.KAFKA_USER_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listenToUsersJoiningAndLeave(ChatMessage chatMessage) {
        System.out.println("sending via kafka listener | Joined or Left User ...");
        this.template.convertAndSend("/topic/public", chatMessage);
    }

}
