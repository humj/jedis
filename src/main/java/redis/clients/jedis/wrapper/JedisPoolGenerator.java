/**
 * 
 */
package redis.clients.jedis.wrapper;

import java.util.List;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Global ShardedJedisPool singleton instance provider
 * 
 * @author joey
 * 
 */
public class JedisPoolGenerator {

	public static ShardedJedisPool singletonPool = new JedisPoolGenerator()
			.generateShardedJedisPoolFromProperties();

	protected ShardedJedisPool generateShardedJedisPoolFromProperties() {
		ShardedJedisPool _pool = new ShardedJedisPool(loadPoolConfig(), loadShardInfo());
		return _pool;
	}

	protected List<JedisShardInfo> loadShardInfo() {
		return null;
	}

	protected JedisPoolConfig loadPoolConfig() {
		return null;
	}

}
