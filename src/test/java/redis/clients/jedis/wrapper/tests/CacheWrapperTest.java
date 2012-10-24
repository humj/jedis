package redis.clients.jedis.wrapper.tests;

import org.junit.Assert;
import org.junit.Test;

import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.wrapper.JedisPoolGenerator;

public class CacheWrapperTest extends Assert {

	@Test
	public void shardPoolGeneratorTest() {
		ShardedJedisPool pool = JedisPoolGenerator.singletonPool;
		assertNotNull(pool);
	}

}
