package org.restaurant.salado.services;

import org.restaurant.salado.entities.Order;

import java.util.List;

public interface OrderService {

    Order saveOrder(Order order);

    boolean deleteOrder(Long id);

    List<Order> getUserOrders(Long id);

    Order getLastActiveOrder(Long id);

    List<Order> getOrders();

}
