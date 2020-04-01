package org.restaurant.salado.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
@CrossOrigin(value = "*")
public class RestaurantController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public String home() {
        return "Welcome To Our Restaurant";
    }

}
