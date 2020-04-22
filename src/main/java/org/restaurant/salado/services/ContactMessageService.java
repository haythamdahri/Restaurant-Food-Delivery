package org.restaurant.salado.services;

import org.restaurant.salado.entities.ContactMessage;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface ContactMessageService {

    ContactMessage saveContactMessage(ContactMessage message);

    boolean deleteContactMessage(Long id);

    ContactMessage getContactMessage(Long id);

    List<ContactMessage> getContactMessages();

}
