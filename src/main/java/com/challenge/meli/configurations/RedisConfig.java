package com.challenge.meli.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import redis.clients.jedis.JedisPoolConfig;


@Configuration
@EnableRedisRepositories
public class RedisConfig {
	
	
	@Bean
	RedisConnectionFactory jedisConnectionFactory() {
		//RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("redisdb.wmbjqy.ng.0001.use2.cache.amazonaws.com", 6379);
        //return new JedisConnectionFactory(redisStandaloneConfiguration);
		JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMaxWaitMillis(10*1000);
        return new JedisConnectionFactory(poolConfig);
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(){
		final RedisTemplate<String , Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

}
