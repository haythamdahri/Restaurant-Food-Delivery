package org.restaurant.salado.mappers;

import org.mapstruct.Mapper;
import org.restaurant.salado.dtos.ShippingDTO;
import org.restaurant.salado.entities.Shipping;

/**
 * @author Haytham DAHRI
 */
@Mapper(componentModel = "spring")
public interface ShippingMapper extends GenericMapper<Shipping, ShippingDTO> {
}
