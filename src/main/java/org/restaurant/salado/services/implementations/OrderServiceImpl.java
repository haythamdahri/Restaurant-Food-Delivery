package org.restaurant.salado.services.implementations;

import org.restaurant.salado.entities.Order;
import org.restaurant.salado.providers.ValuesProvider;
import org.restaurant.salado.repositories.OrderRepository;
import org.restaurant.salado.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
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
        return this.orderRepository.findByUserIdAndCancelledFalseAndDeliveredFalse(id).map(order -> {
            order.setShippingFees(ValuesProvider.SHIPPING_FEES);
            return order;
        }).orElse(null);
    }

    @Override
    public Order getLastActiveOrderOutOfShippingFees(Long id) {
        return this.orderRepository.findByUserIdAndCancelledFalseAndDeliveredFalse(id).orElse(null);
    }


    @Override
    public Order getOrder(Long id) {
        return this.orderRepository.findById(id).map(order -> {
            order.setShippingFees(ValuesProvider.SHIPPING_FEES);
            return order;
        }).orElse(null);
    }

    @Override
    public Order getOrderOutOfShippingFees(Long id) {
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
}
