package org.restaurant.salado.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Haytham DAHRI
 */
@Controller
@RequestMapping(value = "/")
@CrossOrigin(value = "*")
public class RestaurantController {

    /**
     * @return String
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public String home() {
        return "Welcome To Our Restaurant";
    }

}
