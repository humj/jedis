/**
 * 
 */
package redis.clients.jedis.wrapper.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.wrapper.JedisPoolGenerator;

/**
 * @author joey
 *
 */
public class JedisPoolGeneratorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		ShardedJedisPool pool = JedisPoolGenerator.singletonPool;
		System.out.println(pool);
	}

}
