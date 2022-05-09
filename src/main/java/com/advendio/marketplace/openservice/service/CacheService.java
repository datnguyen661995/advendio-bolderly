/* (C)2022 */
package com.advendio.marketplace.openservice.service;
/** Interface implemented for cache */
public interface CacheService {
    void put(String key, Object value);

    Object get(String key);
}
