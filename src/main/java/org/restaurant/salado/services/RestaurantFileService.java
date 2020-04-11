package org.restaurant.salado.services;

import org.restaurant.salado.entities.RestaurantFile;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface RestaurantFileService {

    RestaurantFile saveRestaurantFile(RestaurantFile file);

    RestaurantFile saveRestaurantFile(MultipartFile file) throws IOException;

    RestaurantFile saveRestaurantFile(MultipartFile file, RestaurantFile restaurantFile) throws IOException;

    Boolean deleteRestaurantFile(Long id);

    RestaurantFile getRestaurantFile(Long id);

    RestaurantFile getRestaurantFile(String name);

    Page<RestaurantFile> getRestaurantFiles(int page, int size);

    // TODO: Implement method on IMPL class
    Page<RestaurantFile> searchRestaurantFiles(int page, int size, String search);

    List<RestaurantFile> getRestaurantFiles();

}
