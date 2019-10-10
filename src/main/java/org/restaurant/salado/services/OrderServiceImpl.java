package org.restaurant.salado.services;

import org.restaurant.salado.entities.Order;
import org.restaurant.salado.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    public boolean deleteOrder(Long id) {
        this.orderRepository.deleteById(id);
        return true;
    }

    @Override
    public Order getLastActiveOrder(Long id) {
        return this.orderRepository.findByUserIdAndCancelledFalseAndDeliveredFalse(id);
    }

    @Override
    public Collection<Order> getOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public Collection<Order> getUserOrders(Long id) {
        return this.orderRepository.findByUserId(id);
    }
}
