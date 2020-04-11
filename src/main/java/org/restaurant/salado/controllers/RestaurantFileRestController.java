package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.RestaurantFile;
import org.restaurant.salado.services.RestaurantFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/restaurantfiles")
public class RestaurantFileRestController {

    @Autowired
    private RestaurantFileService restaurantFileService;

    /**
     * Retrieve paged RestaurantFile
     *
     * @param page
     * @param size
     * @return ResponseEntity<Page<RestaurantFile>>
     * @throws InterruptedException
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Page<RestaurantFile>> fetchRestaurantFilesEndPoint(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default_size}") int size, @RequestParam(value = "search", required = false) String search) throws InterruptedException {
        // Check if search exists
        if( search == null ) {
            return ResponseEntity.ok(this.restaurantFileService.getRestaurantFiles(page, size));
        }
        // Return response with RestaurantFile based on search
        return ResponseEntity.ok(this.restaurantFileService.searchRestaurantFiles(page, size, search));
    }


    /**
     * Retrieve RestaurantFile using Identifier
     * @param id
     * @return RestaurantFile
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestaurantFile> fetchRestaurantFileWithNameEndPoint(@PathVariable(value = "id") Long id) {
        // Retrieve RestaurantFile using service tier
        RestaurantFile restaurantFile = this.restaurantFileService.getRestaurantFile(id);
        // Check RestaurantFile existence
        if( restaurantFile != null ) {
            return ResponseEntity.ok(restaurantFile);
        }
        // Return 4040 not found
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieve Blob file only
     * @param id
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/file/{id}")
    public ResponseEntity<byte[]> showProductImage(@PathVariable(value = "id", required = true) Long id) throws IOException {
        // Retrieve RestaurantFile
        RestaurantFile restaurantFile = this.restaurantFileService.getRestaurantFile(id);
        // Check file existence
        if( restaurantFile != null ) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(restaurantFile.getMediaType());
            return new ResponseEntity<>(restaurantFile.getFile(), headers, HttpStatus.OK);
        }
        // Return 404 not found
        return ResponseEntity.notFound().build();
    }


}
