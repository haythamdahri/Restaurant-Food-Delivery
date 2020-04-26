package org.restaurant.salado.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Haytham DAHRI
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder passwordEncoder;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private UserDetailsService userDetailsService;

    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtAuthenticationEntryPoint(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Autowired
    public void setUserDetailsService(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtRequestFilter(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        /* configure AuthenticationManager so that it knows from where to load user for matching credentials
        Use BCryptPasswordEncoder */
        auth.userDetailsService(userDetailsService).passwordEncoder(this.passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**", "/uploads/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // We don't need CSRF for this example
        http.csrf().disable()
                .authorizeRequests()
                // Allow POST for authentication
                .antMatchers(HttpMethod.POST,
                        "/auth/**",
                        "/api/v1/users/"
                ).permitAll()
                // Deny retrieving all users only for employees and admins
                .antMatchers(HttpMethod.GET, "/api/v1/users/").authenticated()
                // Dont authenticate this particular endpoints for GET request only
                .antMatchers(HttpMethod.GET,
                "/api/v1/restaurantfiles/**",
                "/api/roles/**",
                "/api/meals",
                "/api/v1/meals/**",
                "/api/v1/reviews/**",
                "/api/contactmessages",
                "/api/users/search/existsByEmail",
                "/api/v1/users/**").permitAll()
                // Allow POST request for password request
                .antMatchers(HttpMethod.POST, "/api/v1/users/passwordreset").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // all other requests need to be authenticated
                .anyRequest().authenticated().and().
                // make sure we use stateless session; session won't be used to store user's state.
                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
