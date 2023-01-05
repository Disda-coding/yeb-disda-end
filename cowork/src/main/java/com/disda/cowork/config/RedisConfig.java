package com.disda.cowork.config;

import com.disda.cowork.utils.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 */



@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    // @Bean
    // public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
    //     RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    //
    //     //String类型key序列化
    //     redisTemplate.setKeySerializer(new StringRedisSerializer());
    //     //String类型value序列化
    //     redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    //     //Hash类型key序列化
    //     redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    //     //Hash类型value序列化
    //     redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    //
    //     redisTemplate.setConnectionFactory(connectionFactory);
    //     return redisTemplate;
    // }

}
