package org.restaurant.salado.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(value = "/api/v1")
@CrossOrigin(value = "*")
public class RestaurantRestController {

    /**
     * Home Endpoint
     *
     * @return Map<String, String>
     */
    @GetMapping(value = "/")
    public Map<String, Object> home() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "HAYTHAM DAHRI");
        data.put("age", 22);
        return data;
    }

}
