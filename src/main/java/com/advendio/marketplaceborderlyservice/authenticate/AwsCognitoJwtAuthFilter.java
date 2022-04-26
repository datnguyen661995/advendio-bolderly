package com.advendio.marketplaceborderlyservice.authenticate;

import com.advendio.marketplaceborderlyservice.exception.CognitoException;
import com.advendio.marketplaceborderlyservice.properties.BolderlyProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@Component
@Slf4j
public class AwsCognitoJwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;

    @Autowired
    private BolderlyProperties bolderlyProperties;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
//
//    public AwsCognitoJwtAuthFilter(AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor, BolderlyProperties bolderlyProperties) {
//        this.awsCognitoIdTokenProcessor = awsCognitoIdTokenProcessor;
//        this.bolderlyProperties = bolderlyProperties;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication;
        try {
            authentication = this.awsCognitoIdTokenProcessor.authenticate((HttpServletRequest) request);
            if (null != authentication) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                doFilter(request, response, filterChain);
            }
        } catch (ParseException | BadJOSEException | JOSEException e) {
            log.error("[Bolderly] Cognito Id Token processing error");
            resolver.resolveException(request, response, null, new CognitoException(HttpStatus.UNAUTHORIZED, CognitoException.INVALID_TOKEN));
        }
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