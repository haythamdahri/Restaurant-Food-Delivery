package org.restaurant.salado.services;

import org.restaurant.salado.entities.ContactMessage;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface ContactMessageService {

    ContactMessage saveContactMessage(ContactMessage message);

    ContactMessage respondContactMessage(Long id, String response);

    boolean deleteContactMessage(Long id);

    ContactMessage getContactMessage(Long id);

    Page<ContactMessage> getContactMessages(String search, int page, int size);

    List<ContactMessage> getContactMessages();

}
