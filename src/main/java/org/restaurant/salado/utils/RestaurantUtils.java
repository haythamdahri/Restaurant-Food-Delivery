package org.restaurant.salado.utils;

import org.apache.commons.io.FilenameUtils;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class RestaurantUtils {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserService userService;

    /*
     * @Retrieve file extension from a file name
     * */
    public String getExtensionByApacheCommonLib(String filename) {
        return FilenameUtils.getExtension(filename);
    }


}
