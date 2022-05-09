/* (C)2022 */
package com.advendio.marketplace.openservice.aspect;

import com.advendio.marketplace.openservice.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodCacheAspect {

    @Autowired private CacheService cacheService;

    // Call before and after function with annotation @MethodCache
    @Around("@annotation(com.advendio.marketplace.openservice.aspect.MethodCache)")
    public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return methodCacheHold(joinPoint);
    }

    public Object methodCacheHold(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get all the method params
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        log.debug(
                "########[CACHE-MANAGEMENT]-- targetName::{} - methodName::{}",
                targetName,
                methodName);
        // Use the method name and params to create a key
        String key = getCacheKey(methodName, arguments);
        log.debug("########[CACHE-MANAGEMENT]---- getCacheKey--{}", key);
        // call cache service to get the value for the given key if not execute method
        // to get the return object to be cached
        Object returnObject = cacheService.get(key);
        if (returnObject != null) {
            log.debug(
                    "########[CACHE-MANAGEMENT]---- Load data exist from cached with key--{}", key);
            return returnObject;
        }
        log.debug("########[CACHE-MANAGEMENT]---- Create new cached with key--{}", key);
        // execute method to get the return object
        returnObject = joinPoint.proceed(arguments);

        // cache the method return object to cache with the key generated
        cacheService.put(key, returnObject);

        return returnObject;
    }

    /**
     * Build key compare in cached
     *
     * @param methodName
     * @param arguments
     * @return
     */
    public String getCacheKey(String methodName, Object[] arguments) {
        StringBuilder sbKey = new StringBuilder();
        sbKey.append(methodName);
        if (arguments != null && arguments.length != 0) {
            for (int i = 0; i < arguments.length; i++) {
                sbKey.append(".").append(arguments[i]);
            }
        }
        return sbKey.toString().hashCode() + "";
    }
}
