package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Message;
import org.restaurant.salado.repositories.MessageRepository;
import org.restaurant.salado.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message saveMessage(Message message) {
        return this.messageRepository.save(message);
    }

    @Override
    public boolean deleteMessage(Long id) {
        this.messageRepository.deleteById(id);
        return true;
    }

    @Override
    public Message getMessage(Long id) {
        return this.messageRepository.findById(id).orElse(null);
    }

    @Override
    public List<Message> getMessages() {
        return this.messageRepository.findAll();
    }
}
