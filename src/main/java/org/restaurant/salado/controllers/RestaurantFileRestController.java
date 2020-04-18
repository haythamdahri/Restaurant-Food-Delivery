package org.restaurant.salado.controllers;

import org.restaurant.salado.dtos.RestaurantFileDTO;
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

import java.io.IOException;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/restaurantfiles")
public class RestaurantFileRestController {

    private RestaurantFileService restaurantFileService;

    @Autowired
    public void setRestaurantFileService(RestaurantFileService restaurantFileService) {
        this.restaurantFileService = restaurantFileService;
    }

    /**
     * Retrieve paged RestaurantFile
     *
     * @param page: Request Page
     * @param size: Request Page Size
     * @return ResponseEntity<Page < RestaurantFile>>
     */
    @GetMapping(value = "/")
    public ResponseEntity<Page<RestaurantFile>> fetchRestaurantFilesEndPoint(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default_size}") int size, @RequestParam(value = "search", required = false) String search) {
        // Check if search exists
        if (search == null) {
            return ResponseEntity.ok(this.restaurantFileService.getRestaurantFiles(page, size));
        }
        // Return response with RestaurantFile based on search
        return ResponseEntity.ok(this.restaurantFileService.searchRestaurantFiles(page, size, search));
    }

    /**
     * Retrieve RestaurantFile using Identifier
     *
     * @param id: RestaurntFile Identifier
     * @return RestaurantFile
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<RestaurantFile> fetchRestaurantFileWithNameEndPoint(@PathVariable(value = "id") Long id) {
        // Retrieve RestaurantFile using service tier
        RestaurantFile restaurantFile = this.restaurantFileService.getRestaurantFile(id);
        // Check RestaurantFile existence
        if (restaurantFile != null) {
            return ResponseEntity.ok(restaurantFile);
        }
        // Return 4040 not found
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieve Blob file only
     *
     * @param id: RestaurantFile Identifier
     * @return ResponseEntity<byte [ ]>
     */
    @GetMapping(value = "/file/{id}")
    public ResponseEntity<byte[]> showProductImage(@PathVariable(value = "id") Long id) {
        // Retrieve RestaurantFile
        RestaurantFile restaurantFile = this.restaurantFileService.getRestaurantFile(id);
        // Check file existence
        if (restaurantFile != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(restaurantFile.getMediaType()));
            return new ResponseEntity<>(restaurantFile.getFile(), headers, HttpStatus.OK);
        }
        // Return 404 not found
        return ResponseEntity.notFound().build();
    }

    /**
     * Upload restaurant file
     *
     * @param file: MultipartFile to upload
     * @return ResponseEntity<RestaurantFile>
     * @throws IOException: Thrown when having error in file
     */
    @PostMapping(path = "/")
    public ResponseEntity<RestaurantFile> uploadRestaurantFile(@RequestParam(value = "restaurantFile") MultipartFile file) throws IOException {
        // Save Restaurant file
        RestaurantFile restaurantFile = this.restaurantFileService.saveRestaurantFile(file);
        // Create response and return it
        return ResponseEntity.ok(restaurantFile);
    }

    /**
     * Save restaurant file (Insert Or Update)
     *
     * @param restaurantFileDTO: RestaurantFileDTO Data Transfer  Object
     * @return ResponseEntity<RestaurantFile>
     */
    @PutMapping(path = "/")
    public ResponseEntity<RestaurantFile> updateRestaurantFile(@RequestBody RestaurantFileDTO restaurantFileDTO) {
        // Save RestaurantFile And Create response then return it
        return ResponseEntity.ok(this.restaurantFileService.saveRestaurantFile(restaurantFileDTO));
    }

    /**
     * Delete RestaurantFile endpoint
     *
     * @param id: RestaurantFile Identifier
     * @return ResponseEntity<Object>
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteRestaurantFileEndpoint(@PathVariable(value = "id") Long id) {
        this.restaurantFileService.deleteRestaurantFile(id);
        return ResponseEntity.ok().build();
    }

}
