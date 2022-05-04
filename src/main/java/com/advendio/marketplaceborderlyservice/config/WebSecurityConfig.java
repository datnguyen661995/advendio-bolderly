/* (C)2022 */
package com.advendio.marketplaceborderlyservice.config;

import com.advendio.marketplaceborderlyservice.authenticate.AwsCognitoJwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] WHITE_LIST = {
        "/actuator/**", "/", "/v2/api-docs/**", "/bolderly/oauth/token"
    };
    private final AwsCognitoJwtAuthFilter awsCognitoJwtAuthFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(WHITE_LIST)
                .permitAll()
                .and()
                .headers()
                .contentSecurityPolicy("script-src 'self'");
    }
}
