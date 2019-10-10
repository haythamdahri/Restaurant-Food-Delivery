package org.restaurant.salado.services;

import org.restaurant.salado.entities.MealOrder;

import java.util.Collection;

public interface MealOrderService {

    public MealOrder saveMealOrder(MealOrder mealOrder);

    public boolean deleteMealOrder(Long id);

    public Collection<MealOrder> getMealOrders(Long orderId);

    public Collection<MealOrder> getMealOrders();

}
