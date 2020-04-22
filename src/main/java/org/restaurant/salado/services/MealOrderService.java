package org.restaurant.salado.services;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.models.MealOrderRequest;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface MealOrderService {

    MealOrder saveMealOrder(MealOrder mealOrder);

    MealOrder updateMealOrderQuantity(Long id, int quantity);

    boolean deleteMealOrder(Long id);

    MealOrder getMealOrder(Long mealOrderId);

    List<MealOrder> getMealOrders(Long orderId);

    MealOrder createOrUpdateUserCurrentOrder(String email, MealOrderRequest mealOrderRequest);

    List<MealOrder> getMealOrders();

}
