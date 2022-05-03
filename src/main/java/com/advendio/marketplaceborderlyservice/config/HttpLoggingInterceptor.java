package com.advendio.marketplaceborderlyservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class HttpLoggingInterceptor implements HandlerInterceptor {
    private static final String REQUEST_URL = "Request URL: ";
    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("********** BolderlyHttpInterceptor.preHandle **********");
        log.info("{}{}", REQUEST_URL + request.getRequestURL(), (StringUtils.isBlank(request.getQueryString()) ? "" : "?" +request.getQueryString()));
        log.info("Start Time: {}", startTime);
        request.setAttribute(START_TIME, startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("********** BolderlyHttpInterceptor.postHandle **********");
        log.info("{}{}", REQUEST_URL + request.getRequestURL(), StringUtils.isBlank(request.getQueryString()) ? "" : "?" + request.getQueryString());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("********** BolderlyHttpInterceptor.afterCompletion ********** ");
        long startTime = (Long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        log.info("{}{}", REQUEST_URL + request.getRequestURL(), StringUtils.isBlank(request.getQueryString()) ? "" : "?" + request.getQueryString());
        log.info("Response Status: {}", response.getStatus());
        log.info("End Time: {}", endTime);
        log.info("Time Taken: {}", timeTaken);
    }

    private void printRequestInfo(HttpServletRequest req) {
        StringBuffer requestURL = req.getRequestURL();
        String queryString = req.getQueryString();
        if (queryString == null) {
            log.debug("######## url: {}", requestURL.toString());
        } else {
            log.debug("######## url: {}", requestURL.append('?').append(queryString).toString());
        }
        log.debug("######## getRemoteUser: {}", req.getRemoteUser());
        log.debug("######## method: {}", req.getMethod());
    }
}
