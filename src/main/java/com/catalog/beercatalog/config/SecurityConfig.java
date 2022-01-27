package com.catalog.beercatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {"/beers", "/beers/*", "/manufacturers", "manufacturers/*"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeRequests()
            .antMatchers(HttpMethod.GET, AUTH_WHITELIST).permitAll()
            .antMatchers(HttpMethod.POST, "/beers").hasAnyRole("ADMIN","MANUFACTURER")
            .antMatchers(HttpMethod.PUT, "/beers").hasAnyRole("ADMIN","MANUFACTURER")
            .antMatchers(HttpMethod.DELETE, "/beers/*").hasAnyRole("ADMIN","MANUFACTURER")
            .antMatchers(HttpMethod.POST, "/manufacturers").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/manufacturers").hasAnyRole("ADMIN","MANUFACTURER")
            .antMatchers(HttpMethod.DELETE, "/manufacturers/*").hasRole("ADMIN")
        .and().httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

