package org.restaurant.salado.configuration;

import lombok.extern.log4j.Log4j2;
import org.restaurant.salado.entities.ChatMessage;
import org.restaurant.salado.providers.Constants;
import org.restaurant.salado.providers.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Haytham DAHRI
 */
@Component
@Log4j2
public class ChatMessageListener {

    private SimpMessagingTemplate template;

    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Handle private Messages
     *
     * @param chatMessage: ChatMessage Object
     */
    @KafkaListener(
            topics = KafkaConstants.KAFKA_PRIVATE_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listenToPrivateMessages(ChatMessage chatMessage) {
        log.info("sending via kafka listener | Private Message ...");
        final String senderChannel = chatMessage.getSender().getId().toString() + Constants.CHANNEL_SEPARATOR + chatMessage.getReceiver().getId().toString();
        final String receiverChannel = chatMessage.getReceiver().getId().toString() + Constants.CHANNEL_SEPARATOR + chatMessage.getSender().getId().toString();
        this.template.convertAndSendToUser(senderChannel, "/reply", chatMessage);
        this.template.convertAndSendToUser(receiverChannel, "/reply", chatMessage);
    }

    /**
     * Listen to users joining and leave
     *
     * @param chatMessage: ChatMessage Object
     */
    @KafkaListener(
            topics = KafkaConstants.KAFKA_USER_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listenToUsersJoiningAndLeave(ChatMessage chatMessage) {
        log.info("sending via kafka listener | Joined or Left User ...");
        this.template.convertAndSend("/topic/public", chatMessage);
    }

}
