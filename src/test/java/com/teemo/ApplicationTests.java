package com.teemo;

import com.teemo.entity.User;
import com.teemo.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate ;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;



	@Autowired
	private UserService userService ;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testRedis(){
		stringRedisTemplate.opsForValue().set("test_key", "test_value");

		Assert.assertEquals("test_value",stringRedisTemplate.opsForValue().get("test_key"));
	}



	@Test
	public void testObjectRedis(){
		List<User> list = userService.getUsers();
		redisTemplate.opsForList().rightPushAll("user_list",list);
	}


//	@Test
//	public void testUserRedis(){
//		List<User> users = userService.getUsers();
//		final String key = "users";
//		redisTemplate.execute(new RedisCallback<Boolean>() {
//			@Override
//			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//				RedisSerializer<Object> keySerializer = redisTemplate.getKeySerializer();
//				RedisSerializer<Object> valueSerializer = redisTemplate.getValueSerializer();
//				connection.multi();
//				connection.del(keySerializer.serialize(key));
//
//				for (User user : users){
//					connection.rPush(keySerializer.serialize(key),valueSerializer.serialize(user));
//				}
//				connection.close();
//
//				return true;
//			}
//		});
//
//	}
}
