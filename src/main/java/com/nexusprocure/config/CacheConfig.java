package com.nexusprocure.config;

import com.nexusprocure.common.cache.NexusProcureCacheErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CacheConfig implements CachingConfigurer {
    private final NexusProcureCacheErrorHandler nexusProcureCacheErrorHandler;
    @Override
    public CacheErrorHandler errorHandler(){
        return nexusProcureCacheErrorHandler;

    }
}
