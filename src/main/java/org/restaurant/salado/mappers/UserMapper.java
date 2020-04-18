package org.restaurant.salado.mappers;

import org.mapstruct.Mapper;
import org.restaurant.salado.dtos.UserDTO;
import org.restaurant.salado.entities.User;

/**
 * @author Haytham DAHRI
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<User, UserDTO> {
}
