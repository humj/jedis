/**
 * 
 */
package redis.clients.jedis.wrapper;

import java.util.Map;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.JedisAssert;

/**
 * 
 * An generic cache wrapper for easy-use
 * 
 * @author joey
 * 
 */
public class GenericCacheWrapper implements CacheWrapper {

	protected static ShardedJedisPool pool = JedisPoolGenerator.singletonPool;

	protected ShardedJedis jedis;

	public GenericCacheWrapper() {
		JedisAssert.assertNotNulls(pool);
		this.jedis = pool.getResource();
	}

	/**
	 * Delete the key from redis
	 * 
	 * @see {@link redis.clients.jedis.Jedis#del(String...)}
	 * @param key
	 * @return Integer reply, specifically: an integer greater than 0 if one or
	 *         more keys were removed 0 if none of the specified key existed
	 */
	public Long delKey(String key) {
		JedisAssert.assertNotNulls(key);
		return this.jedis.del(key);
	}

	/**
	 * Set the <key, value> pair, never expire
	 * 
	 * @param key
	 * 
	 * @param value
	 * @return The set command will never failed, so set void
	 * 
	 */
	public void setString(String key, String value) {
		JedisAssert.assertNotNulls(key, value);
		this.jedis.set(key, value);
	}

	/**
	 * Set the <key, value> pair, and it will expire in <i>seconds</i> seconds
	 * 
	 * @param key
	 * 
	 * @param value
	 * 
	 * @param seconds
	 * 
	 */
	public void setStringExpiredIn(String key, String value, int seconds) {
		JedisAssert.assertNotNulls(key, value, seconds);
		this.setString(key, value);
		this.setExpireIn(key, seconds);
	}

	/**
	 * Set the expired time of the <i>key</i> at <i>milliseconds</i> time
	 * 
	 * @param key
	 * 
	 * @param milliseconds
	 * 
	 */
	public void setExpiredAt(String key, long milliseconds) {
		JedisAssert.assertNotNulls(key, milliseconds);
		this.jedis.expireAt(key, milliseconds);
	}

	/**
	 * Get the value of the key
	 * 
	 * @param key
	 * 
	 * @return The value of the key, return null when the key does not exists
	 */
	public String getString(String key) {
		JedisAssert.assertNotNulls(key);
		return this.jedis.get(key);
	}

	/**
	 * Set the expired time of the <i>key</i> at specified seconds then
	 * 
	 * @param key
	 * @param seconds
	 * @return Integer reply, specifically: 1: the timeout was set. 0: the
	 *         timeout was not set since the key already has an associated
	 *         timeout (this may happen only in Redis versions < 2.1.3, Redis >=
	 *         2.1.3 will happily update the timeout), or the key does not
	 *         exist.
	 */
	public Long setExpireIn(String key, int seconds) {
		JedisAssert.assertNotNulls(key, seconds);
		return this.jedis.expire(key, seconds);
	}

	/**
	 * Use HASHES to set a key with <field, value> pair
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return If the field already exists, and the HSET just produced an update
	 *         of the value, 0 is returned, otherwise if a new field is created
	 *         1 is returned.
	 */
	public Long setBean(String key, String field, String value) {
		JedisAssert.assertNotNulls(key, field, value);
		return this.jedis.hset(key, field, value);
	}

	public void setBean(String key, Map<String, String> hashes) {
		JedisAssert.assertNotNulls(key, hashes);
		this.jedis.hmset(key, hashes);
	}

	public void exitWrapper() {
		pool.returnResource(jedis);
	}

	public String getVersion() {
		return "1.0";
	}

}
