package org.restaurant.salado.services;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.entities.Message;
import org.restaurant.salado.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

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
    public Collection<Message> getMessages() {
        return this.messageRepository.findAll();
    }
}
