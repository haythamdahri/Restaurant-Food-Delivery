package org.restaurant.salado.controllers;

import org.restaurant.salado.builders.RestaurantBuilder;
import org.restaurant.salado.entities.ChatMessage;
import org.restaurant.salado.entities.ChatMessageType;
import org.restaurant.salado.models.ChatMessageRequest;
import org.restaurant.salado.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    private SimpMessagingTemplate template;
    private RestaurantBuilder restaurantBuilder;
    private ChatMessageService chatMessageService;

    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Autowired
    public void setRestaurantBuilder(RestaurantBuilder restaurantBuilder) {
        this.restaurantBuilder = restaurantBuilder;
    }

    @Autowired
    public void setChatMessageService(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
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
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessageRequest chatMessageRequest) {
        ChatMessage chatMessage = this.restaurantBuilder.buildChatMessage(chatMessageRequest);
        // Save ChatMessage in database if MessageType is MESSAGE
        if( chatMessage.getMessageType().equals(ChatMessageType.MESSAGE) ) {
            chatMessage = this.chatMessageService.saveMessageService(chatMessage);
        }
        return chatMessage;
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessageRequest chatMessageRequest, SimpMessageHeaderAccessor headerAccessor) {
        // Add user in web socket session
        ChatMessage chatMessage = this.restaurantBuilder.buildChatMessage(chatMessageRequest);
        // Save ChatMessage in database if MessageType is MESSAGE
        if( chatMessage.getMessageType().equals(ChatMessageType.MESSAGE) ) {
            chatMessage = this.chatMessageService.saveMessageService(chatMessage);
        }
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("user", chatMessage.getSender().getId().toString());
        return chatMessage;
    }


    /*--------------------Private chat--------------------*/
    @MessageMapping("/sendPrivateMessage")
    //@SendTo("/queue/reply")
    public void sendPrivateMessage(@Payload ChatMessageRequest chatMessageRequest) {
        ChatMessage chatMessage = this.restaurantBuilder.buildChatMessage(chatMessageRequest);
        // Save ChatMessage in database if MessageType is MESSAGE
        if( chatMessage.getMessageType().equals(ChatMessageType.MESSAGE) ) {
            chatMessage = this.chatMessageService.saveMessageService(chatMessage);
        }
        this.template.convertAndSendToUser(
                chatMessage.getReceiver().getId().toString(), "/reply", chatMessage);
    }

    @MessageMapping("/addPrivateUser")
    @SendTo("/queue/reply")
    public ChatMessage addPrivateUser(@Payload ChatMessageRequest chatMessageRequest, SimpMessageHeaderAccessor headerAccessor) {
        ChatMessage chatMessage = this.restaurantBuilder.buildChatMessage(chatMessageRequest);
        // Save ChatMessage in database if MessageType is MESSAGE
        if( chatMessage.getMessageType().equals(ChatMessageType.MESSAGE) ) {
            chatMessage = this.chatMessageService.saveMessageService(chatMessage);
        }
        // Add user in web socket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("private-user", chatMessage.getSender().getId().toString());
        return chatMessage;
    }
}