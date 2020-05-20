package com.tiernebre.tailgate.web;

import com.tiernebre.tailgate.authentication.JwtAuthorizationFilter;
import com.tiernebre.tailgate.token.AccessTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/users")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/sessions")
                .permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), tokenProvider))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
