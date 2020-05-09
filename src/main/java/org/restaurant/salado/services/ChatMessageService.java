package org.restaurant.salado.services;

import org.restaurant.salado.entities.ChatMessage;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface ChatMessageService {

    ChatMessage saveMessageService(ChatMessage chatMessage);

    boolean deleteChatMessage(Long id);

    ChatMessage getChatMessage(Long id);

    List<ChatMessage> getChatMessages();

    List<ChatMessage> getRoomChatMessages(Long senderId, Long receiverId);

}
