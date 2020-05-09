package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.ChatMessage;
import org.restaurant.salado.repositories.ChatMessageRepository;
import org.restaurant.salado.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private ChatMessageRepository chatMessageRepository;

    @Autowired
    public void setChatMessageRepository(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatMessage saveMessageService(ChatMessage chatMessage) {
        return this.chatMessageRepository.save(chatMessage);
    }

    @Override
    public boolean deleteChatMessage(Long id) {
        this.chatMessageRepository.deleteById(id);
        return true;
    }

    @Override
    public ChatMessage getChatMessage(Long id) {
        return this.chatMessageRepository.findById(id).orElse(null);
    }

    @Override
    public List<ChatMessage> getChatMessages() {
        return this.chatMessageRepository.findAll();
    }

    @Override
    public List<ChatMessage> getRoomChatMessages(Long senderId, Long receiverId) {
        return this.chatMessageRepository.searchRoomChatMessages(senderId, receiverId);
    }
}
