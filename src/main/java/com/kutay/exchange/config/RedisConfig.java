package com.kutay.exchange.config;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching // enable spring cache abstraction
@Configuration // marks class as a spring configuration class
public class RedisConfig {
    /*
     * RedisConenctionFactory --> interface for working with and retrieving active connections to redis.
     *
     * */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // later check serializers
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .prefixCacheNameWith("exchange::")
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // later maybe add transaction awareness
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("walletBalance",
                        defaultConfig.entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration("walletAvailable",
                        defaultConfig.entryTtl(Duration.ofMinutes(15)))
                .build();
    }
}
