package org.restaurant.salado;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Haytham DAHRI
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantRestIntegrationTest {

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
    @WithMockUser(value = "spring")
    @Test

    public void givenAuthRequestOnRestaurantRestService_shouldSucceedWith200AndExpectSameContent() throws Exception {
        // Build expected data
        JSONObject data = new JSONObject();
        data.put("name", "HAYTHAM DAHRI");
        data.put("age", 22);
        // Perform test
        this.mockMvc.perform(get("/api/v1/").contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
                .andExpect(content().json(data.toString()));
    }

}
