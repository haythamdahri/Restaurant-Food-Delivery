package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.ContactMessage;
import org.restaurant.salado.repositories.ContactMessageRepository;
import org.restaurant.salado.services.ContactMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
public class ContactMessageServiceImpl implements ContactMessageService {

    private ContactMessageRepository messageRepository;

    @Autowired
    public void setMessageRepository(ContactMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public ContactMessage saveContactMessage(ContactMessage message) {
        return this.messageRepository.save(message);
    }

    @Override
    public boolean deleteContactMessage(Long id) {
        this.messageRepository.deleteById(id);
        return true;
    }

    @Override
    public ContactMessage getContactMessage(Long id) {
        return this.messageRepository.findById(id).orElse(null);
    }

    @Override
    public List<ContactMessage> getContactMessages() {
        return this.messageRepository.findAll();
    }
}
