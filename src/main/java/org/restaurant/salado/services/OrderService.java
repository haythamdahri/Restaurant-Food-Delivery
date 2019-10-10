package org.restaurant.salado.services;

import org.restaurant.salado.entities.Order;

import java.util.Collection;

public interface OrderService {

    public Order saveOrder(Order order);

    public boolean deleteOrder(Long id);

    public Collection<Order> getUserOrders(Long id);

    public Order getLastActiveOrder(Long id);

    public Collection<Order> getOrders();

}
