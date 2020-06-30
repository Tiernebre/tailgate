package com.tiernebre.zone_blitz.web;

import com.tiernebre.zone_blitz.authentication.JwtAuthorizationFilter;
import com.tiernebre.zone_blitz.token.access.AccessTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AccessTokenProvider tokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), tokenProvider))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
