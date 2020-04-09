package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Shipping;
import org.restaurant.salado.repositories.ShippingRepository;
import org.restaurant.salado.services.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    @Override
    public Shipping saveShipping(Shipping shipping) {
        return this.shippingRepository.save(shipping);
    }

    @Override
    public boolean deleteShipping(Long id) {
        this.shippingRepository.deleteById(id);
        return true;
    }

    @Override
    public Shipping getShipping(Long id) {
        return this.shippingRepository.findById(id).orElse(null);
    }

    @Override
    public List<Shipping> getShippingList() {
        return this.shippingRepository.findAll();
    }


}
