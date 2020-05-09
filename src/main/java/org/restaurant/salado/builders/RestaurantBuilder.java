package org.restaurant.salado.builders;

import org.restaurant.salado.entities.ChatMessage;
import org.restaurant.salado.entities.ChatMessageType;
import org.restaurant.salado.models.ChatMessageRequest;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Haytham DAHRI
 */
@Component
public class RestaurantBuilder {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ChatMessage buildChatMessage(ChatMessageRequest chatMessageRequest) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(null);
        chatMessage.setSender(this.userService.getUser(chatMessageRequest.getSenderId()));
        chatMessage.setReceiver(this.userService.getUser(chatMessageRequest.getReceiverId()));
        chatMessage.setContent(chatMessageRequest.getContent());
        chatMessage.setMessageType(chatMessageRequest.getMessageType());
        return chatMessage;
    }

}
