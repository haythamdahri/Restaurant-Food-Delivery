package org.restaurant.salado.services;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.repositories.MealOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MealOrderServiceImpl implements MealOrderService {

    @Autowired
    private MealOrderRepository mealOrderRepository;

    @Override
    public MealOrder saveMealOrder(MealOrder mealOrder) {
        return this.mealOrderRepository.save(mealOrder);
    }

    @Override
    public boolean deleteMealOrder(Long id) {
        this.mealOrderRepository.deleteById(id);
        return true;
    }

    @Override
    public Collection<MealOrder> getMealOrders(Long orderId) {
        return this.mealOrderRepository.findByOrderId(orderId);
    }

    @Override
    public Collection<MealOrder> getMealOrders() {
        return this.mealOrderRepository.findAll();
    }
}
