package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.ChatMessage;
import org.restaurant.salado.entities.ChatMessageType;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.helpers.RestaurantHelper;
import org.restaurant.salado.models.ChatMessageRequest;
import org.restaurant.salado.providers.KafkaConstants;
import org.restaurant.salado.services.ChatMessageService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/chat")
public class ChatRestController {

    private KafkaTemplate<String, ChatMessage> kafkaTemplate;
    private RestaurantHelper restaurantHelper;
    private ChatMessageService chatMessageService;
    private UserService userService;
    private IAuthenticationFacade authenticationFacade;

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

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationFacade(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Retrieve Room Chat Messages between two users
     * UserId may be Sender Or Receiver
     *
     * @param senderId:   User Sender IDENTIFIER
     * @param receiverId: User Receiver IDENTIFIER
     * @return ResponseEntity<List < ChatMessage>>
     */
    @GetMapping(path = "/messages")
    public ResponseEntity<List<ChatMessage>> listRoomChatMessages(@RequestParam(name = "senderId") Long senderId, @RequestParam(name = "receiverId") Long receiverId) {
        return ResponseEntity.ok(this.chatMessageService.getRoomChatMessages(senderId, receiverId));
    }

    /**
     * List Users to chat with and exclude current authenticated user
     *
     * @return ResponseEntity<List < User>>
     */
    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> listCurrentUserChatHistory() {
        return ResponseEntity.ok(this.userService.getUsers(this.authenticationFacade.getAuthentication().getName()));
    }

    /*-------------------- Group (Public) chat--------------------*/

    /**
     * Notify users in Queue about a user joining or leave
     *
     * @param chatMessageRequest: ChatMessageRequest Object
     */
    @MessageMapping(value = {"/addUser", "/removeUser"})
    @SendTo("/topic/public")
    public void addUser(@Payload ChatMessageRequest chatMessageRequest) {
        // Go Online or Offline with user ChatMessageType is JOINED or LEAVE
        ChatMessageType chatMessageType = chatMessageRequest.getMessageType();
        if (chatMessageType.equals(ChatMessageType.JOINED) || chatMessageType.equals(ChatMessageType.LEAVE)) {
            this.userService.updateUserOnlineStatus(chatMessageRequest.getSenderId(), chatMessageType.equals(ChatMessageType.JOINED));
        }
        // Put message in the Queue to intercept in KafkaListener
        this.kafkaTemplate.send(KafkaConstants.KAFKA_USER_TOPIC, this.restaurantHelper.createChatMessage(chatMessageRequest));
    }


    /*--------------------Private chat--------------------*/

    /**
     * Send Private Message Handler
     *
     * @param chatMessageRequest: ChatMessageRequest Object
     */
    @MessageMapping("/sendPrivateMessage")
    //@SendTo("/queue/reply")
    public void sendPrivateMessage(@Payload ChatMessageRequest chatMessageRequest) {
        ChatMessage chatMessage = this.restaurantHelper.createChatMessage(chatMessageRequest);
        // Save ChatMessage in database if MessageType is MESSAGE
        if (chatMessage.getMessageType().equals(ChatMessageType.MESSAGE) && chatMessage.getContent().length() > 0) {
            chatMessage = this.chatMessageService.saveMessageService(chatMessage);
        }
        // Put message in the Queue to intercept in KafkaListener
        this.kafkaTemplate.send(KafkaConstants.KAFKA_PRIVATE_TOPIC, chatMessage);
    }
}