package org.restaurant.salado.services;

import org.restaurant.salado.entities.MealOrder;

import java.util.List;

public interface MealOrderService {

    MealOrder saveMealOrder(MealOrder mealOrder);

    boolean deleteMealOrder(Long id);

    List<MealOrder> getMealOrders(Long orderId);

    List<MealOrder> getMealOrders();

}
