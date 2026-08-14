package com.nexusprocure.config;
import com.nexusprocure.common.cache.CacheNames;
import com.nexusprocure.common.cache.NexusProcureCacheErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
//Springboot creates RedisManger, RedisConnectionFactory, RedisTemplate,Redis CacheManager
// sometimes company need to customize serialization, TTl, etc.so we use RedisConfig

@Configuration//hey spring this class contains bean definitions
@EnableCaching//This will activate @Cacheable, @Cacheput, @Cacheevict
@RequiredArgsConstructor
public class RedisConfig {
   private final NexusProcureCacheErrorHandler nexusProcureCacheErrorHandler;
   private final RedisConnectionFactory redisConnectionFactory;

   @Bean // Create RedisSerializer bean so that redis infrastructure will use it
   //Here RedisSerializer is interface, GenericJackson2JsonRedisSerializer is an implementation
    public RedisSerializer<Object> redisSerializer(){
       return new GenericJackson2JsonRedisSerializer();
   }
   @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisSerializer<Object>serializer) {
       RedisCacheConfiguration defaultConfig = RedisCacheConfiguration
               .defaultCacheConfig().
               serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
               .entryTtl(Duration.ofMinutes(30));
       RedisCacheConfiguration productConfig = defaultConfig.entryTtl(Duration.ofMinutes(30));
       RedisCacheConfiguration inventoryConfig = defaultConfig.entryTtl(Duration.ofMinutes(10));

       Map<String, RedisCacheConfiguration> cacheConfigurations = Map.of(CacheNames.PRODUCTS, productConfig, CacheNames.INVENTORY, inventoryConfig);
       return RedisCacheManager.builder(redisConnectionFactory)
               .cacheDefaults(defaultConfig)
               .withInitialCacheConfigurations(cacheConfigurations)
               .build();
   }

}
