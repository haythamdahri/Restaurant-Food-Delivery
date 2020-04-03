package org.restaurant.salado.services.implementations;

import org.restaurant.salado.entities.Order;
import org.restaurant.salado.repositories.OrderRepository;
import org.restaurant.salado.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Order> getOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public List<Order> getUserOrders(Long id) {
        return this.orderRepository.findByUserId(id);
    }
}
