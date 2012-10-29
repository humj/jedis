/**
 * 
 */
package redis.clients.jedis.wrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

	private static final String JEDIS_POOL_CONFIG = "/jedispool.conf";

	// configuration keys
	private static final String REDIS_SHARD_INFOS = "redis.shard.infos";
	private static final String REDIS_POOL_MAX_IDLE = "redis.pool.maxidle";
	private static final String REDIS_POOL_MIN_IDLE = "redis.pool.minidle";
	private static final String REDIS_POOL_MAX_ACTIVE = "redis.pool.maxactive";
	private static final String REDIS_POOL_MAX_WAIT = "redis.pool.maxwait";
	private static final String REDIS_POOL_WHEN_EXHAUSTED_ACTION = "redis.pool.whenexhaustedaction";
	private static final String REDIS_POOL_TEST_ON_RETURN = "redis.pool.testonreturn";
	private static final String REDIS_POOL_TEST_ON_BORROW = "redis.pool.testonborrow";
	private static final String REDIS_POOL_TEST_WHILE_IDLE = "redis.pool.testwhileidle";
	private static final String REDIS_POOL_NUM_TEST_PER_EVICTION_RUN = "redis.pool.numtestsperevictionrun";
	private static final String REDIS_POOL_TIME_BETWEEN_EVICTION_RUNS_MILLIS = "redis.pool.timebetweenevictionrunsmillis";
	private static final String REDIS_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS = "redis.pool.minevictableidletimemillis";
	private static final String REDIS_POOL_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = "redis.pool.softminevictableidletimemillis";

	private Properties properties = new Properties();

	public static ShardedJedisPool singletonPool = new JedisPoolGenerator()
			.generateShardedJedisPoolFromProperties();

	protected ShardedJedisPool generateShardedJedisPoolFromProperties() {
		ShardedJedisPool _pool = null;
		try {
			this.loadConfigFile();
			_pool = new ShardedJedisPool(loadPoolConfig(), loadShardInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return _pool;
	}

	private void loadConfigFile() throws IOException {
		this.properties.load(this.getClass().getResourceAsStream(
				JEDIS_POOL_CONFIG));
	}

	/**
	 * Load Redis shards configurations
	 * 
	 * @return An array of JedisShardInfo objects
	 */
	private List<JedisShardInfo> loadShardInfo() {
		List<JedisShardInfo> shardList = new ArrayList<JedisShardInfo>();
		String shardStr = this.properties.getProperty(REDIS_SHARD_INFOS).trim();
		String[] shards = shardStr.split(",");
		for (String shardItem : shards) {
			String[] hostAndPort = shardItem.split(":");
			shardList.add(new JedisShardInfo(hostAndPort[0], hostAndPort[1]));
		}

		return shardList;
	}

	/**
	 * Load Redis pool configurations
	 * 
	 * @return An initialized JedisPoolConfig instance
	 */
	private JedisPoolConfig loadPoolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();

		String maxActive = this.properties.getProperty(REDIS_POOL_MAX_ACTIVE);
		if (maxActive != null)
			poolConfig.setMaxActive(Integer.parseInt(maxActive));

		String maxIdle = this.properties.getProperty(REDIS_POOL_MAX_IDLE);
		if (maxIdle != null)
			poolConfig.setMaxIdle(Integer.parseInt(maxIdle));

		String maxWait = this.properties.getProperty(REDIS_POOL_MAX_WAIT);
		if (maxWait != null)
			poolConfig.setMaxWait(Integer.parseInt(maxWait));

		String minEvictableIdleTime = this.properties
				.getProperty(REDIS_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS);
		if (minEvictableIdleTime != null)
			poolConfig.setMinEvictableIdleTimeMillis(Integer
					.parseInt(minEvictableIdleTime));

		String minIdle = this.properties.getProperty(REDIS_POOL_MIN_IDLE);
		if (minIdle != null)
			poolConfig.setMinIdle(Integer.parseInt(minIdle));

		String numTestPerEvictionRun = this.properties
				.getProperty(REDIS_POOL_NUM_TEST_PER_EVICTION_RUN);
		if (numTestPerEvictionRun != null)
			poolConfig.setNumTestsPerEvictionRun(Integer
					.parseInt(numTestPerEvictionRun));

		String testOnBorrow = this.properties
				.getProperty(REDIS_POOL_TEST_ON_BORROW);
		if (testOnBorrow != null)
			poolConfig.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));

		String testOnReturn = this.properties
				.getProperty(REDIS_POOL_TEST_ON_RETURN);
		if (testOnReturn != null)
			poolConfig.setTestOnReturn(Boolean.parseBoolean(testOnReturn));

		String testWhileIdle = this.properties
				.getProperty(REDIS_POOL_TEST_WHILE_IDLE);
		if (testWhileIdle != null)
			poolConfig.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));

		String timeBetweenEvictionRunsMillis = this.properties
				.getProperty(REDIS_POOL_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
		if (timeBetweenEvictionRunsMillis != null)
			poolConfig.setTimeBetweenEvictionRunsMillis(Integer
					.parseInt(timeBetweenEvictionRunsMillis));

		String whenExhaustedAction = this.properties
				.getProperty(REDIS_POOL_WHEN_EXHAUSTED_ACTION);
		if (whenExhaustedAction != null)
			poolConfig.setWhenExhaustedAction(Byte
					.parseByte(whenExhaustedAction));

		String softMinEvictableIdleTimeMillis = this.properties
				.getProperty(REDIS_POOL_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
		if (softMinEvictableIdleTimeMillis != null)
			poolConfig.setSoftMinEvictableIdleTimeMillis(Long
					.parseLong(softMinEvictableIdleTimeMillis));

		return poolConfig;
	}

}
