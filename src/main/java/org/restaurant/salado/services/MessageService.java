package org.restaurant.salado.services;

import org.restaurant.salado.entities.Message;

import java.util.Collection;
import java.util.List;

public interface MessageService {

    Message saveMessage(Message message);

    boolean deleteMessage(Long id);

    Message getMessage(Long id);

    List<Message> getMessages();

}
