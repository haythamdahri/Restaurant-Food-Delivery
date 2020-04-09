package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.repositories.MealOrderRepository;
import org.restaurant.salado.services.MealOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
public class MealOrderServiceImpl implements MealOrderService {

    @Autowired
    private MealOrderRepository mealOrderRepository;

    @Override
    public MealOrder saveMealOrder(MealOrder mealOrder) {
        return this.mealOrderRepository.save(mealOrder);
    }

    @Override
    public boolean deleteMealOrder(Long id) throws Exception {
        MealOrder mealOrder = this.mealOrderRepository.findById(id).orElse(null);
        // Check if mealOrder exists
        if (mealOrder != null) {
            // Delete mealOrder from order
            mealOrder.deleteMealOrderFromOrder(mealOrder);
            // Delete current mealOrder
            this.mealOrderRepository.deleteById(id);
            return this.mealOrderRepository.findById(id).orElse(null) == null;
        }
        // throw exception if mealOrder does not exist
        throw new Exception();
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
    public List<MealOrder> getMealOrders() {
        return this.mealOrderRepository.findAll();
    }
}
