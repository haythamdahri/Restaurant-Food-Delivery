package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.*;
import org.restaurant.salado.models.PasswordReset;
import org.restaurant.salado.services.*;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v1")
@CrossOrigin(value = "*")
public class RestaurantRestController {

    /**
    * Home Endpoint
    */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<String, String> home() {
        Map<String, String> data = new HashMap<>();
        data.put("name", "HAYTHAM DAHRI");
        return data;
    }

}
