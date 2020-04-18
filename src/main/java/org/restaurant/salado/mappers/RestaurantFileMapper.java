package org.restaurant.salado.mappers;

import org.mapstruct.Mapper;
import org.restaurant.salado.dtos.RestaurantFileDTO;
import org.restaurant.salado.entities.RestaurantFile;

/**
 * @author Haytham DAHRI
 */
@Mapper(componentModel = "spring")
public interface RestaurantFileMapper extends GenericMapper<RestaurantFile, RestaurantFileDTO> {
}
