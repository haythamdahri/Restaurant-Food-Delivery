package org.restaurant.salado;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.RestaurantFile;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Haytham DAHRI
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantFileRestIntegrationTest {

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
     * Test RestaurantFileRestController endpoint: /api/v1/restaurantfiles
     *
     * @throws Exception
     */
    @WithMockUser(value = "haytham.dahri@gmail.com")
    @Test
    public void uploadRestaurantFileAndDeleteIt__shouldSucceedWith200AndExpectSameResults() throws Exception {
        // URIs
        String RESTAURANT_FILES_URI = "/api/v1/restaurantfiles/";
        String RESTAURANT_FILES_DELETE_URI = "/api/v1/restaurantfiles/{id}";
        String RESTAURANT_FILES_FILE_URI = "/api/v1/restaurantfiles/file/{id}";
        // Retrieve file
        File file = new File("");
        Path path = Paths.get("uploads/users/images/default.png");
        String originalFileName = "default.png";
        String name = "default";
        String contentType = MediaType.IMAGE_PNG_VALUE;
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile multipartFile = new MockMultipartFile("restaurantfile", originalFileName, contentType, content);
        // Create Expected RestaurantFile
        RestaurantFile expectedRestaurantFile = new RestaurantFile(null, originalFileName, RestaurantUtils.getExtensionByApacheCommonLib(originalFileName), contentType, multipartFile.getBytes(), null);
        // Upload multipart file
        String response = this.mockMvc.perform(multipart(RESTAURANT_FILES_URI).file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA_VALUE).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
        // Assert response not null
        assertNotNull(response);
        // Cast response to RestaurantFile
        RestaurantFile returnedRestaurantFile = new ObjectMapper().readValue(response, RestaurantFile.class);
        /**
         * ============================= Assertions =============================
         */
        // Bytes
        response = this.mockMvc.perform(get(RESTAURANT_FILES_FILE_URI, returnedRestaurantFile.getId().toString()).accept(MediaType.IMAGE_PNG)).andReturn().getResponse().getContentAsString();
        byte[] bytes = response.getBytes();
        assertTrue(bytes.length > 0);
        // Name
        assertTrue(name.equalsIgnoreCase(returnedRestaurantFile.getName()));
        // Extension
        assertTrue(expectedRestaurantFile.getExtension().equalsIgnoreCase(returnedRestaurantFile.getExtension()));
        // Media Type
        assertTrue(expectedRestaurantFile.getMediaType().equalsIgnoreCase(returnedRestaurantFile.getMediaType()));
        // Set ID and TIMESTAMP Then Compare the two objects
        expectedRestaurantFile.setId(returnedRestaurantFile.getId());
        expectedRestaurantFile.setTimestamp(returnedRestaurantFile.getTimestamp());
        expectedRestaurantFile.setName(name);
        expectedRestaurantFile.setFile(null);
        returnedRestaurantFile.setFile(null);
        assertEquals(expectedRestaurantFile, returnedRestaurantFile);
        /**
         * Delete Restaurant file
         */
        this.mockMvc.perform(delete(RESTAURANT_FILES_DELETE_URI, returnedRestaurantFile.getId())).andExpect(status().is2xxSuccessful());
        /**
         * Check that restaurant file is deleted
         */
        this.mockMvc.perform(get(RESTAURANT_FILES_FILE_URI, returnedRestaurantFile.getId().toString())).andExpect(status().is4xxClientError());
    }

}
