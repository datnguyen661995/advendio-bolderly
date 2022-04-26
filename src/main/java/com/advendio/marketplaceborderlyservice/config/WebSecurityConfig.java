package com.advendio.marketplaceborderlyservice.config;

import com.advendio.marketplaceborderlyservice.authenticate.AwsCognitoJwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] WHITE_LIST = {"/actuator/**", "/", "/v2/api-docs/**"};
    private final AwsCognitoJwtAuthFilter awsCognitoJwtAuthFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors()
                .and().authorizeRequests().antMatchers(WHITE_LIST).permitAll()
                .and().headers().contentSecurityPolicy("script-src 'self'");
        httpSecurity.addFilterBefore(awsCognitoJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
