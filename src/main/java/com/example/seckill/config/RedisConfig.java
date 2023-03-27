package com.example.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisCollectionFactory){
        RedisTemplate<String,Object>redisTemplate=new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisCollectionFactory);
        return redisTemplate;
    }

    @Bean
    public DefaultRedisScript<Boolean>script(){
        DefaultRedisScript<Boolean> defaultRedisScript=new DefaultRedisScript<>();
        defaultRedisScript.setLocation(new ClassPathResource("script.lua"));
        defaultRedisScript.setResultType(Boolean.class);
        return defaultRedisScript;
    }
}
