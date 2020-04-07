package org.restaurant.salado.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<?, ?> home() throws Exception{
        Map<Object, Object> data = new HashMap<>();
        data.put("name", "HAYTHAM DAHRI");
        data.put("age", 22);
        return data;
    }

}
