package com.nexusprocure.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NexusProcureCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key){
        log.error("Cache GET failed. Cache: {}, Key: {}", cache.getName(),key,exception);
    }
    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value){
        log.error("Cache PUT failed. Cache: {}, Key: {}", cache.getName(),key, exception);
    }

    @Override
    public void handleCacheEvictError(
            RuntimeException exception,
            Cache cache,
            Object key
    ) {

        log.error(
                "Cache EVICT failed. Cache: {}, Key: {}",
                cache.getName(),
                key,
                exception
        );
    }
    @Override
    public void handleCacheClearError(
            RuntimeException exception,
            Cache cache
    ) {

        log.error(
                "Cache CLEAR failed. Cache: {}",
                cache.getName(),
                exception
        );

    }


}
