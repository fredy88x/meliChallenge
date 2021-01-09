package com.challenge.meli.configurations;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import redis.clients.jedis.Protocol;


@Configuration
@EnableRedisRepositories
public class RedisConfig {
	
	
	@SuppressWarnings("deprecation")
	@Bean
	RedisConnectionFactory jedisConnectionFactory() {
		String redistogoUrl = System.getenv("REDISTOGO_URL");
		URI redistogoUri;
		try {
			redistogoUri = new URI(redistogoUrl);
			JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory();

			jedisConnFactory.setUsePool(true);
			jedisConnFactory.setHostName(redistogoUri.getHost());
			jedisConnFactory.setPort(redistogoUri.getPort());
			jedisConnFactory.setTimeout(Protocol.DEFAULT_TIMEOUT);
			jedisConnFactory.setPassword(redistogoUri.getUserInfo().split(":", 2)[1]);

			return jedisConnFactory;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}

		
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(){
		final RedisTemplate<String , Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

}
