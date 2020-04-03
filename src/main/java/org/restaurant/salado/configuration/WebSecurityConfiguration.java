package org.restaurant.salado.configuration;

import org.springframework.beans.factory.annotation.Autowired;
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

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        /* configure AuthenticationManager so that it knows from where to load user for matching credentials
        Use BCryptPasswordEncoder */
        auth.userDetailsService(userDetailsService).passwordEncoder(this.bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/error", "/css/**", "/js/**", "/images/**", "/less/**", "/metadata/**", "/scss/**", "/sprites/**", "/svgs/**", "/webfonts/**", "/uploads/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // We don't need CSRF for this example
        http.csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests().antMatchers(
                "/api/roles/**",
                "/auth/**",
                "/api/meals",
                "/api/messages",
                "/api/users/search/existsByEmail",
                "/api/v1/users/**"
        ).permitAll().
                antMatchers(HttpMethod.OPTIONS, "/**").permitAll().
                // all other requests need to be authenticated
                        anyRequest().authenticated().and().
                /**
                 * make sure we use stateless session; session won't be used to store user's state.
                 */
                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
