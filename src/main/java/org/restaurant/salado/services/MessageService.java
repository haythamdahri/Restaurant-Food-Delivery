package org.restaurant.salado.services;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.entities.Message;

import java.util.Collection;

public interface MessageService {

    public Message saveMessage(Message message);

    public boolean deleteMessage(Long id);

    public Collection<Message> getMessages();

}
