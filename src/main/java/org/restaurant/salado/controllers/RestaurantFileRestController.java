package org.restaurant.salado.controllers;

import com.sun.mail.iap.Response;
import org.restaurant.salado.entities.RestaurantFile;
import org.restaurant.salado.services.RestaurantFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * @return ResponseEntity<byte[]>
     * @throws IOException
     */
    @RequestMapping(value = "/file/{id}")
    public ResponseEntity<byte[]> showProductImage(@PathVariable(value = "id", required = true) Long id) throws IOException {
        // Retrieve RestaurantFile
        RestaurantFile restaurantFile = this.restaurantFileService.getRestaurantFile(id);
        // Check file existence
        if( restaurantFile != null ) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(restaurantFile.getMediaType()));
            return new ResponseEntity<>(restaurantFile.getFile(), headers, HttpStatus.OK);
        }
        // Return 404 not found
        return ResponseEntity.notFound().build();
    }

    /**
     * Upload restaurant file
     * @param file
     * @return ResponseEntity<RestaurantFile>
     * @throws IOException
     */
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<RestaurantFile> uploadRestaurantFile(@RequestParam(value = "restaurantfile") MultipartFile file) throws IOException {
        // Save Restaurant file
        RestaurantFile restaurantFile = this.restaurantFileService.saveRestaurantFile(file);
        // Create response and return it
        return ResponseEntity.ok(restaurantFile);
    }

    /**
     * Update restaurant file
     * @param restaurantFile
     * @return ResponseEntity<RestaurantFile>
     * @throws IOException
     */
    @RequestMapping(path = "/", method = RequestMethod.PUT)
    public ResponseEntity<RestaurantFile> updateRestaurantFile(@RequestBody RestaurantFile restaurantFile) throws IOException {
        // Save Restaurant file
        restaurantFile = this.restaurantFileService.saveRestaurantFile(restaurantFile);
        // Create response and return it
        return ResponseEntity.ok(restaurantFile);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRestaurantFileEndpoint(@PathVariable(value = "id") Long id) {
        this.restaurantFileService.deleteRestaurantFile(id);
        return ResponseEntity.ok().build();
    }

}
