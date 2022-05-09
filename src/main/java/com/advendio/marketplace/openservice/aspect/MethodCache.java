/* (C)2022 */
package com.advendio.marketplace.openservice.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Support Cached in method with annotation */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodCache {

    String value() default "";

    String cacheName() default "";
}
