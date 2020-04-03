package org.restaurant.salado.services;

import org.restaurant.salado.entities.Shipping;

import java.util.List;

public interface ShippingService {

    Shipping saveShipping(Shipping shipping);

    boolean deleteShipping(Long id);

    Shipping getShipping(Long id);

    List<Shipping> getShippingList();
}
