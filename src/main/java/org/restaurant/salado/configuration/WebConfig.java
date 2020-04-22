package org.restaurant.salado.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Haytham DAHRI
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurationSupport {

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

}
