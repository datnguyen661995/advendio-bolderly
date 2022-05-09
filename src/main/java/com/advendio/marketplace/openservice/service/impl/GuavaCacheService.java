/* (C)2022 */
package com.advendio.marketplace.openservice.service.impl;

import com.advendio.marketplace.openservice.properties.CacheProperties;
import com.advendio.marketplace.openservice.service.CacheService;
import com.google.common.cache.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GuavaCacheService implements CacheService {
    private TimeUnit expireAfterWriteUnit = TimeUnit.MINUTES;

    @Autowired private CacheProperties cacheProperties;

    private LoadingCache<String, Object> globalDataCache = null;

    @PostConstruct
    public void init() {
        setupCache();
    }

    @PreDestroy
    public void preDestroy() {
        cleanCache();
    }

    /** Clean cache when app destroy */
    public void cleanCache() {
        if (globalDataCache != null) {
            globalDataCache.invalidateAll();
        }
    }

    /** Setup cache when app start */
    public void setupCache() {
        try {
            globalDataCache =
                    loadCache(
                            new CacheLoader<String, Object>() {
                                public Object load(String key) throws Exception {
                                    // This method is mainly used to handle the processing logic
                                    // when the cache key
                                    // does not have a cache value.
                                    log.debug(
                                            "########[CACHE-MANAGEMENT]-- Guava Cache cache value does not exist, initialize null value, key name: {}",
                                            key);
                                    return null;
                                }
                            });
        } catch (Exception e) {
            log.error("########[CACHE-MANAGEMENT]-- Initial Guava Cache error", e);
        }
    }

    /**
     * buildCache
     *
     * <p>expireAfterWrite is to remove the key when the specified item has not been created /
     * overwritten within a certain time, and it will be taken from loading the next time it is
     * taken expireAfterAccess is that the specified item has not been read or written within a
     * certain time, the key will be removed, and it will be taken from loading the next time it is
     * taken refreshAfterWrite is not created / overwritten within the specified time, then after
     * the specified time, when you access again, it will refresh the cache, and always return the
     * old value before the new value arrives The difference with expire is that after the specified
     * time, expire is the remove key, and the next visit is to synchronize to get the new value;
     * Refresh is that after the specified time, the key will not be removed. The refresh will be
     * triggered at the next visit, and the old value will be returned when the new value does not
     * come back.
     *
     * <p>When setting, you can let expireAfterWrite> refreshAfterWrite, so that refreshReferWrite
     * time every interval, when there is access, refresh, If there is no access beyond
     * expireAfterWrite, the cache is invalidated, so that the refresh mechanism and expiration
     * mechanism of guava cache can be used at the same time
     *
     * @param expireAfterWrite Set the expiration time after write cache, one request is loaded,
     *     other requests are blocked
     * @param refreshAfterWrite Set the refresh time after write cache, one request to refresh,
     *     other requests return the old value
     * @param concurrencyLevel Allow simultaneous simultaneous update of operands. Refers to the
     *     amount of concurrency when updating data in a cache. After setting this parameter, the
     *     maximum amount of concurrency allowed may not strictly abide by this parameter. Because
     *     the data is stored in different blocks, and the data is not evenly distributed. In the
     *     code implementation, the cache will create the corresponding ConcurrentMap according to
     *     this parameter, and each ConcurrentMap is called a block. The data will be stored on each
     *     ConcurrentMap separately, there will be another data structure to maintain the location
     *     of all cached data. Therefore, if this parameter is set too large, it will cause more
     *     time and space overhead (more blocks are allocated, and the information of these blocks
     *     needs to be maintained additionally); If the setting is too small, it will cause a large
     *     number of threads to block during the update operation (update the same ConcurrentMap
     *     needs to wait for the lock)
     * @param initialCapacity Specify the minimum total size of the hash table used for caching.
     *     -For example, the initialCapacity is set to 60, and the concurrencyLevel (see description
     *     below) is also set to 8. The storage space will be divided into 8 blocks, each block has
     *     a hash table structure, and the initial size of each hash table is 8. If the cache space
     *     is limited, Need to estimate the initialization space large enough to slow down, to avoid
     *     expensive expansion operations when the data grows (the expansion space will cause deep
     *     COPY)
     * @param maximumSize Allow the maximum number of cache entries
     * @param cacheLoader Cache loading logic
     * @param <K>
     * @param <V>
     * @return
     */
    private <K, V> LoadingCache<K, V> loadCache(CacheLoader<K, V> cacheLoader) throws Exception {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(
                        cacheProperties.getMins().getExpireAfterWrite(), expireAfterWriteUnit)
                .initialCapacity(cacheProperties.getInitialCapacity())
                .maximumSize(cacheProperties.getMaximumSize())
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .recordStats()
                .removalListener(
                        new RemovalListener<K, V>() {
                            public void onRemoval(RemovalNotification<K, V> rn) {
                                log.debug(
                                        "########[CACHE-MANAGEMENT]---- Guava Cache cache reclaimed successfully, key: {}, value: {}",
                                        rn.getKey(),
                                        rn.getValue());
                            }
                        })
                .recordStats()
                .build(cacheLoader);
    }

    /** Add key and value to cache */
    @Override
    public void put(String key, Object value) {
        try {
            if (!cacheProperties.getEnabled()) {
                return;
            }
            globalDataCache.put(key, value);
        } catch (Exception e) {
            log.error("########[CACHE-MANAGEMENT]-- Set cache value error", e);
        }
    }

    /** Get the value for the given key from cache */
    @Override
    public Object get(String key) {
        Object obj = null;
        try {
            if (!cacheProperties.getEnabled()) {
                return null;
            }
            obj = globalDataCache.getIfPresent(key);
        } catch (Exception e) {
            log.error("########[CACHE-MANAGEMENT]-- Get cache value error", e);
        }
        return obj;
    }
}
