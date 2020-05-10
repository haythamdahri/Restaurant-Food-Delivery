package org.restaurant.salado.controllers;

import org.restaurant.salado.helpers.RestaurantHelper;
import org.restaurant.salado.entities.ChatMessage;
import org.restaurant.salado.entities.ChatMessageType;
import org.restaurant.salado.models.ChatMessageRequest;
import org.restaurant.salado.providers.KafkaConstants;
import org.restaurant.salado.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/chat")
public class ChatRestController {

    private KafkaTemplate<String, ChatMessage> kafkaTemplate;
    private RestaurantHelper restaurantHelper;
    private ChatMessageService chatMessageService;

    @Autowired
    public void setRestaurantHelper(RestaurantHelper restaurantHelper) {
        this.restaurantHelper = restaurantHelper;
    }

    @Autowired
    public void setChatMessageService(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, ChatMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Retrieve Room Chat Messages between two users
     * UserId may be Sender Or Receiver
     * @param senderId: User Sender IDENTIFIER
     * @param receiverId: User Receiver IDENTIFIER
     * @return ResponseEntity<List<ChatMessage>>
     */
    @GetMapping(path = "/messages")
    public ResponseEntity<List<ChatMessage>> listRoomChatMessages(@RequestParam(name = "senderId") Long senderId, @RequestParam(name = "receiverId") Long receiverId) {
        return ResponseEntity.ok(this.chatMessageService.getRoomChatMessages(senderId, receiverId));
    }

    /*-------------------- Group (Public) chat--------------------*/
    /**
     * Notify users in Queue about a user joining or leave
     * @param chatMessageRequest: ChatMessageRequest Object
     */
    @MessageMapping(value = {"/addUser", "/removeUser"})
    @SendTo("/topic/public")
    public void addUser(@Payload ChatMessageRequest chatMessageRequest) {
        // Put message in the Queue to intercept in KafkaListener
        this.kafkaTemplate.send(KafkaConstants.KAFKA_USER_TOPIC, this.restaurantHelper.createChatMessage(chatMessageRequest));
    }


    /*--------------------Private chat--------------------*/

    /**
     * Send Private Message Handler
     * @param chatMessageRequest: ChatMessageRequest Object
     */
    @MessageMapping("/sendPrivateMessage")
    //@SendTo("/queue/reply")
    public void sendPrivateMessage(@Payload ChatMessageRequest chatMessageRequest) {
        ChatMessage chatMessage = this.restaurantHelper.createChatMessage(chatMessageRequest);
        // Save ChatMessage in database if MessageType is MESSAGE
        if( chatMessage.getMessageType().equals(ChatMessageType.MESSAGE) ) {
            chatMessage = this.chatMessageService.saveMessageService(chatMessage);
        }
        // Put message in the Queue to intercept in KafkaListener
        this.kafkaTemplate.send(KafkaConstants.KAFKA_PRIVATE_TOPIC, chatMessage);
    }
}