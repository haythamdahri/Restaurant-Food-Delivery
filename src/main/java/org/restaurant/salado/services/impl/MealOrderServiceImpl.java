package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.exceptions.BusinessException;
import org.restaurant.salado.models.MealOrderRequest;
import org.restaurant.salado.providers.Constants;
import org.restaurant.salado.repositories.MealOrderRepository;
import org.restaurant.salado.services.MealOrderService;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Haytam DAHRI
 */
@Service
public class MealOrderServiceImpl implements MealOrderService {

    private MealOrderRepository mealOrderRepository;

    private UserService userService;
    private OrderService orderService;
    private MealService mealService;

    @Autowired
    public void setMealOrderRepository(MealOrderRepository mealOrderRepository) {
        this.mealOrderRepository = mealOrderRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setMealService(MealService mealService) {
        this.mealService = mealService;
    }

    @Override
    public MealOrder saveMealOrder(MealOrder mealOrder) {
        return this.mealOrderRepository.save(mealOrder);
    }

    @Override
    public MealOrder updateMealOrderQuantity(Long id, int quantity) {
        // Retrieve mealOrder
        MealOrder mealOrder = this.mealOrderRepository.findById(id).orElse(null);
        // Check if meal order exists
        if (mealOrder != null) {
            // Check meal available quantity
            if (mealOrder.getMeal().getStock() >= quantity) {
                // Update mealOrder quantity and save it
                mealOrder.setQuantity(quantity);
                mealOrder = this.mealOrderRepository.save(mealOrder);
            } else {
                // Throw exception of unavailable quantity, replace AVAILABLE_STOCK with current mealOrder stock in text
                throw new BusinessException(Constants.UNAVAILABLE_PRODUCT_STOCK.replace("AVAILABLE_STOCK", mealOrder.getMeal().getStock().toString()));
            }
        } else {
            throw new BusinessException("An error occurred while updating meal order quantity, please try again!");
        }
        // Return mealOrder for successful operation
        return mealOrder;
    }

    @Override
    public boolean deleteMealOrder(Long id) {
        MealOrder mealOrder = this.mealOrderRepository.findById(id).orElse(null);
        // Check if mealOrder exists
        if (mealOrder != null) {
            // Get Order to set it as cancelled if no other mealOrder exists
            Order order = mealOrder.getOrder();
            // Delete mealOrder from order
            mealOrder.deleteMealOrderFromOrder(mealOrder);
            // Delete current mealOrder
            this.mealOrderRepository.deleteById(id);
            // Set order as cancelled if not remaining mealOrder object
            if (order.getMealOrders().isEmpty()) {
                this.orderService.getOrder(order.getId()).setCancelled(true);
                this.orderService.saveOrder(order);
            }
            // Return true
            return true;
        }
        // Return false in case MealOrder not found
        return false;
    }

    @Override
    public MealOrder getMealOrder(Long mealOrderId) {
        return this.mealOrderRepository.findById(mealOrderId).orElse(null);
    }

    @Override
    public List<MealOrder> getMealOrders(Long orderId) {
        return this.mealOrderRepository.findByOrderId(orderId);
    }


    @Override
    public MealOrder createOrUpdateUserCurrentOrder(String email, MealOrderRequest mealOrderRequest) {
        // Create MealOrder object
        MealOrder mealOrder = new MealOrder(null, null, this.mealService.getMeal(mealOrderRequest.getMealId()), mealOrderRequest.getQuantity(), BigDecimal.ZERO);
        // Fetch connected user from database
        User user = this.userService.getUser(email);
        // Get last active order or create a new one of not exists
        Order userOrder = Optional.ofNullable(this.orderService.getLastActiveOrder(email)).orElse(new Order(null, user, null, BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), "", new Date(), false, false, null));
        // Check if meal already exists in cart or meal stock is empty
        if (userOrder.getMealOrders() != null) {
            if (userOrder.checkMealOrder(mealOrder.getMeal().getId())) {
                // Throw new Business RuntimeException
                throw new BusinessException(Constants.MEAL_EXISTS_IN_CART);
            } else if (mealOrder.getMeal().getStock() == 0) {
                // Check if meal stock is available
                // Throw new Business RuntimeException
                throw new BusinessException(Constants.NO_PRODUCT_STOCK_AVAILABLE);
            }
        }
        // Save mealOrder
        mealOrder.setOrder(userOrder);
        this.mealOrderRepository.save(mealOrder);
        // Save order in case everything is ok
        userOrder.setCancelled(false);
        userOrder.setDelivered(false);
        this.orderService.saveOrder(userOrder);
        // Return User Order
        return userOrder.getMealOrders().stream().filter(ml -> ml.getId().equals(mealOrder.getId())).findFirst().orElseThrow(() -> new BusinessException(Constants.ERROR));
    }

    @Override
    public List<MealOrder> getMealOrders() {
        return this.mealOrderRepository.findAll();
    }
}
