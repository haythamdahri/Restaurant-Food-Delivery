package org.restaurant.salado.services;

import org.restaurant.salado.entities.Order;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface OrderService {

    Order saveOrder(Order order);

    boolean deleteOrder(Long id);

    List<Order> getUserOrders(Long id);

    List<Order> getUserOrders(String email);

    Order getLastActiveOrder(Long userId);

    Order getOrder(Long id);

    List<Order> getOrders();

}
