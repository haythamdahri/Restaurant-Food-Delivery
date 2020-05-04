package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.ContactMessage;
import org.restaurant.salado.exceptions.BusinessException;
import org.restaurant.salado.repositories.ContactMessageRepository;
import org.restaurant.salado.services.ContactMessageService;
import org.restaurant.salado.services.EmailService;
import org.restaurant.salado.services.MailContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
public class ContactMessageServiceImpl implements ContactMessageService {

    private ContactMessageRepository contactMessageRepository;
    private EmailService emailService;

    @Autowired
    public void setContactMessageRepository(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public ContactMessage saveContactMessage(ContactMessage message) {
        return this.contactMessageRepository.save(message);
    }

    @Override
    public ContactMessage respondContactMessage(Long id, String response) {
        // Retrieve ContactMessage
        ContactMessage contactMessage = this.contactMessageRepository.findById(id).orElseThrow(BusinessException::new);
        // Send Email to sender
        this.emailService.sendContactMessageEmail(contactMessage.getEmail(), "Contact Support", response);
        // Set ContactMessage as responded
        contactMessage.setResponded(true);
        return this.contactMessageRepository.save(contactMessage);
    }

    @Override
    public boolean deleteContactMessage(Long id) {
        this.contactMessageRepository.deleteById(id);
        return true;
    }

    @Override
    public ContactMessage getContactMessage(Long id) {
        return this.contactMessageRepository.findById(id).orElse(null);
    }

    @Override
    public Page<ContactMessage> getContactMessages(String search, int page, int size) {
        search = search.trim().toLowerCase();
        // Check if search is empty
        if (search.length() == 0) {
            // Return users with ROLE_USER without search
            return this.contactMessageRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC, "id"));
        }
        // Return by search
        return this.contactMessageRepository.searchContactMessages(PageRequest.of(page, size, Sort.Direction.DESC, "id"), search);
    }

    @Override
    public List<ContactMessage> getContactMessages() {
        return this.contactMessageRepository.findAll();
    }
}
