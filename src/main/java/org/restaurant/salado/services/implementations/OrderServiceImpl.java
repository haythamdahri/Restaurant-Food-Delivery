package org.restaurant.salado.services.implementations;

import org.restaurant.salado.entities.Order;
import org.restaurant.salado.providers.ValuesProvider;
import org.restaurant.salado.repositories.OrderRepository;
import org.restaurant.salado.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return this.orderRepository.findByUserIdAndCancelledFalseAndDeliveredFalse(id)
                .map(order -> {
                    order.postLoad();
                    order.setShippingFees(ValuesProvider.SHIPPING_FEES);
                    return order;
                }).orElse(null);
    }

    @Override
    public Order getLastActiveOrderOutOfShippingFees(Long id) {
        return this.orderRepository.findByUserIdAndCancelledFalseAndDeliveredFalse(id)
                .map(order -> {
                    order.postLoad();
                    return order;
                }).orElse(null);
    }


    @Override
    public Order getOrder(Long id) {
        return this.orderRepository.findById(id)
                .map(order -> {
                    order.setShippingFees(ValuesProvider.SHIPPING_FEES);
                    return order;
                }).orElse(null);
    }

    @Override
    public Order getOrderOutOfShippingFees(Long id) {
        return this.orderRepository.findById(id)
                .map(order -> {
                    order.postLoad();
                    return order;
                }).orElse(null);
    }

    @Override
    public List<Order> getOrders() {
        return this.postLoadExecuter(this.orderRepository.findAll(), true);
    }

    @Override
    public List<Order> getOrdersOutOfShippingFees() {
        return this.postLoadExecuter(this.orderRepository.findAll(), false);
    }

    @Override
    public List<Order> getUserOrders(Long id) {
        return this.orderRepository.findByUserId(id);
    }

    @Override
    public List<Order> getUserOrdersOutOfShippingFees(String email) {
        return this.postLoadExecuter(this.orderRepository.findByUserEmail(email), false);
    }

    @Override
    public List<Order> getUserOrders(String email) {
        return this.postLoadExecuter(this.orderRepository.findByUserEmail(email), true);
    }

    private List<Order> postLoadExecuter(List<Order> orders, Boolean isFeeesIncluded) {
        return isFeeesIncluded ?
                orders.stream()
                        .peek(Order::postLoad)
                        .peek(order -> order.setShippingFees(ValuesProvider.SHIPPING_FEES))
                        .collect(Collectors.toList())
                :
                orders.stream()
                        .peek(Order::postLoad)
                        .collect(Collectors.toList());

    }
}
