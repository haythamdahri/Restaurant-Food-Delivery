package org.restaurant.salado;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.models.ResponseData;
import org.restaurant.salado.providers.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Haytham DAHRI
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    /**
     * Test RestaurantRestController endpoint: /api/v1/
     *
     * @throws Exception: On thrown exception
     */
    @WithMockUser(value = "haytham.dahri@gmail.com")
    @Test
    public void toggleProductFromUserPreferences_shouldSucceedWith200AndExpectSameResults() throws Exception {
        // FINAL URIs
        String USERS_URI = "/api/v1/users/all";
        String USER_PREFERENCES_URI = "/api/v1/users/preferences";
        String MEALS_URI = "/api/v1/meals/all";
        // Mapper
        ObjectMapper objectMapper = new ObjectMapper();
        // Build expected data
        String STATUS = "status";
        String MESSAGE = "message";
        String PREFERRED = "preferred";
        Map<String, Object> data = new HashMap<>();
        data.put(STATUS, true);
        // Retrieve users and assert not empty list
        String response = this.mockMvc.perform(get(USERS_URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
        // Assert response not null
        assertNotNull(response);
        assertTrue(response.toString().length() > 0);
        // Map response to List<User>
        List<User> users = new ObjectMapper().readValue(response, new TypeReference<List<User>>() {
        });
        assertFalse(users.isEmpty());
        // Retrieve a product
        response = this.mockMvc.perform(get(MEALS_URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
        List<Meal> meals = new ObjectMapper().readValue(response, new TypeReference<List<Meal>>() {
        });
        assertNotNull(meals);
        Meal meal = meals.get(0);
        // POST to preferences endpoint, put expected response and assert response
        response = this.mockMvc.perform(post(USER_PREFERENCES_URI).param("id", meal.getId().toString()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.OK.value())).andReturn().getResponse().getContentAsString();
        data = objectMapper.readValue(response, Map.class);
        // POST to preferences endpoint, put expected response and assert the whole response data
        boolean expectedPreferred = !(boolean)data.get(PREFERRED);
        String expectedMessage = data.get(MESSAGE).toString().equalsIgnoreCase(Constants.MEAL_REMOVED_FROM_PREFERENCES) ? Constants.MEAL_ADDED_TO_PREFERENCES : Constants.MEAL_REMOVED_FROM_PREFERENCES;
        data.put(MESSAGE, expectedMessage);
        data.put(PREFERRED, expectedPreferred);
        this.mockMvc.perform(post(USER_PREFERENCES_URI).param("id", meal.getId().toString()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.OK.value())).andExpect(content().string(objectMapper.writeValueAsString(data)));
    }

//    public void registerUserAndEnableAccountAndLogin__shouldSucceedWith200AndExpectSameResults() throws Exception {
//        // Construct user to sign up with
//        File file = new File("uploads/users/images/default.png");
//        RestaurantFile restaurantFile = new RestaurantFile(null, file.getName(), RestaurantUtils.getExtensionByApacheCommonLib(file.getName()), MediaType.IMAGE_PNG.toString(), IOUtils.toByteArray(new FileInputStream(file)), null);
//        restaurantFile = this.restaurantFileService.saveRestaurantFile(restaurantFile);
//        User user = new User(null, "marakechatlas@gmail.com", "toortoor", "intg user", false, )
//    }

}
