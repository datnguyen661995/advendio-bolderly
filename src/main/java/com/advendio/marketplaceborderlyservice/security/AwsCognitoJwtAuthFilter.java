package com.advendio.marketplaceborderlyservice.security;

import com.advendio.marketplaceborderlyservice.exception.CustomException;
import com.advendio.marketplaceborderlyservice.properties.BolderlyProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AwsCognitoJwtAuthFilter extends OncePerRequestFilter {
    private final AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;
    private final BolderlyProperties bolderlyProperties;

    public AwsCognitoJwtAuthFilter(AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor, BolderlyProperties bolderlyProperties) {
        this.awsCognitoIdTokenProcessor = awsCognitoIdTokenProcessor;
        this.bolderlyProperties = bolderlyProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication;
        try {
            authentication = this.awsCognitoIdTokenProcessor.authenticate((HttpServletRequest) request);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                doFilter(request, response, filterChain);
                return;
            }
        } catch (Exception e) {
            log.error("[Bolderly] Cognito Id Token processing error, {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }
        throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid Token");

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        for (AntPathRequestMatcher matcher : bolderlyProperties.getIgnoringMatchers()) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }
}


//@Component
//@Slf4j
//public class AwsCognitoJwtAuthFilter extends GenericFilter {
//    private AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;
//
//    public AwsCognitoJwtAuthFilter(AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor) {
//        this.awsCognitoIdTokenProcessor = awsCognitoIdTokenProcessor;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        Authentication authentication;
//        try {
//            authentication = this.awsCognitoIdTokenProcessor.authenticate((HttpServletRequest) request);
//            if (null != authentication) {
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (Exception e) {
//            log.error("[Bolderly] Cognito Id Token processing error, {}", e);
//            SecurityContextHolder.clearContext();
//        }
//        filterChain.doFilter(request,response);
//    }
//}