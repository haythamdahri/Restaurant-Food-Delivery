package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.repositories.OrderRepository;
import org.restaurant.salado.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Haytam DAHRI
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

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
    public Order getLastActiveOrder(Long userId) {
        return this.orderRepository.findByUserIdAndCancelledFalseAndDeliveredFalse(userId).orElse(null);
    }

    @Override
    public Order getLastActiveOrder(String userEmail) {
        return this.orderRepository.findByUserEmailAndCancelledFalseAndDeliveredFalse(userEmail).orElse(null);
    }

    @Override
    public Order getOrder(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> getOrders() {
        return this.orderRepository.findAll();
    }


    @Override
    public List<Order> getUserOrders(Long id) {
        return this.orderRepository.findByUserId(id);
    }

    @Override
    public List<Order> getUserOrders(String email) {
        return this.orderRepository.findByUserEmail(email);
    }

}
