package org.restaurant.salado;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.NotNull;
import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertFalse;
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
     * @throws Exception
     */
    @WithMockUser(value = "haytham.dahri@gmail.com")
    @Test
    public void toggleProductFromUserPreferences_shouldSucceedWith200AndExpectResults() throws Exception {
        // FINAL URIs
        String USERS_URI = "/api/v1/users/";
        String USER_PREFERENCES_URI = "/api/v1/users/preferences";
        String MEALS_URI = "/api/v1/meals/test";
        // Build expected data
        boolean preferred = false;
        String addedMessage = "Meal has been added to your preferences successfully";
        String removedMessage = "Meal has been removed from your preferences successfully";
        JSONObject data = new JSONObject();
        data.put("status", true);
        // Retrieve users and assert not empty list
        String response = this.mockMvc.perform(get(USERS_URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
        List<User> users = new ObjectMapper().readValue(response, new TypeReference<List<User>>() {
        });
        assertFalse(users.isEmpty());
        // Retrieve a product
        response = this.mockMvc.perform(get(MEALS_URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
        List<Meal> meals = new ObjectMapper().readValue(response, new TypeReference<List<Meal>>() {
        });
        Meal meal = meals.get(0);
        // POST to preferences endpoint, put expected response and assert response
        data.put("preferred", true);
        data.put("message", addedMessage);
        this.mockMvc.perform(post(USER_PREFERENCES_URI + "?id=" + meal.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.OK.value())).andExpect(content().json(data.toString()));
        // POST to preferences endpoint, put expected response and assert response
        data.put("preferred", false);
        data.put("message", removedMessage);
        this.mockMvc.perform(post(USER_PREFERENCES_URI + "?id=" + meal.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.OK.value())).andExpect(content().json(data.toString()));
    }

}
