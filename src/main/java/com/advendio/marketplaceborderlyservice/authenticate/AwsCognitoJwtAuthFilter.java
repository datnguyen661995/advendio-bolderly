/* (C)2022 */
package com.advendio.marketplaceborderlyservice.authenticate;

import com.advendio.marketplaceborderlyservice.exception.CognitoException;
import com.advendio.marketplaceborderlyservice.properties.BolderlyProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@AllArgsConstructor
@Slf4j
public class AwsCognitoJwtAuthFilter extends OncePerRequestFilter {
    private final AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;
    private final BolderlyProperties bolderlyProperties;
    private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        Authentication authentication;
        try {
            authentication = this.awsCognitoIdTokenProcessor.authenticate(request);
            if (null != authentication) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                doFilter(request, response, filterChain);
                return;
            }
        } catch (Exception e) {
            log.error("[Bolderly] Cognito Id Token processing error, {}", e.getMessage());
            resolver.resolveException(
                    request,
                    response,
                    null,
                    new CognitoException(HttpStatus.UNAUTHORIZED, e.getMessage()));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        for (AntPathRequestMatcher matcher : bolderlyProperties.getIgnoringMatchers()) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
