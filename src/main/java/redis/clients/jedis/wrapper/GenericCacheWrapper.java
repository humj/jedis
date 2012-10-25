/**
 * 
 */
package redis.clients.jedis.wrapper;

import java.util.List;
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
/**
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
	 * Set the expired time of the <i>key</i> at <i>milliseconds</i> time
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#expireAt(String, long)}
	 * @param key
	 * @param milliseconds
	 * 
	 */
	public void setExpiredAt(String key, long milliseconds) throws Exception {
		JedisAssert.assertNotNulls(key, milliseconds);
		this.jedis.expireAt(key, milliseconds);
	}

	/**
	 * Set the <i>key</i> will be expired in <i>seconds</i> later
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#expire(String, int)}
	 * @param key
	 * @param seconds
	 */
	public void setExpiredIn(String key, int seconds) throws Exception {
		JedisAssert.assertNotNulls(key, seconds);
		this.jedis.expire(key, seconds);
	}

	/**
	 * Delete the key from redis
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#del(String...)}
	 * @param key
	 * @return Integer reply, specifically: an integer greater than 0 if one or
	 *         more keys were removed 0 if none of the specified key existed
	 */
	public Long delKey(String key) throws Exception {
		JedisAssert.assertNotNulls(key);
		return this.jedis.del(key);
	}

	/**
	 * Set the <key, value> pair, never expire
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#set(String, String)}
	 * @param key
	 * @param value
	 * @return The set command will never failed, so set void
	 * 
	 */
	public void setString(String key, String value) throws Exception {
		JedisAssert.assertNotNulls(key, value);
		this.jedis.set(key, value);
	}

	/**
	 * Set the <key, value> pair, and it will expire in <i>seconds</i> seconds
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#setex(String, int, String)}
	 * @param key
	 * @param value
	 * @param seconds
	 * 
	 */
	public void setStringExpiredIn(String key, String value, int seconds)
			throws Exception {
		JedisAssert.assertNotNulls(key, value, seconds);
		this.jedis.setex(key, seconds, value);
	}

	/**
	 * Get the value of the key
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#get(String)}
	 * @param key
	 * @return The value of the key, return null when the key does not exists
	 */
	public String getString(String key) throws Exception {
		JedisAssert.assertNotNulls(key);
		return this.jedis.get(key);
	}

	/**
	 * Use HASHES to save a field of a bean, with no expired limition
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#hset(String, String, String)}
	 * @param key
	 * @param field
	 * @param value
	 * @return If the field already exists, and the HSET just produced an update
	 *         of the value, 0 is returned, otherwise if a new field is created
	 *         1 is returned.
	 */
	public Long setBean(String key, String field, String value)
			throws Exception {
		JedisAssert.assertNotNulls(key, field, value);
		return this.jedis.hset(key, field, value);
	}

	/**
	 * Use HASHES to save some fields of a bean, with no expired limition
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#hmset(String, Map)}
	 * @param key
	 * @param hashes
	 */
	public String setBean(String key, Map<String, String> hashes)
			throws Exception {
		JedisAssert.assertNotNulls(key, hashes);
		return this.jedis.hmset(key, hashes);
	}

	/**
	 * Use HASHES to save a field of a bean, will be expired <br>
	 * in <i>seconds</i> later
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @param seconds
	 * @return
	 */
	public Long setBeanExpiredIn(String key, String field, String value,
			int seconds) throws Exception {
		JedisAssert.assertNotNulls(key, field, value, seconds);
		Long resp = this.setBean(key, field, value);
		this.setExpiredIn(key, seconds);
		return resp;
	}

	/**
	 * Use HASHES to save a field of a bean, will be expired <br>
	 * in <i>seconds</i> later
	 * 
	 * @param key
	 * @param hashes
	 * @param seconds
	 * @return If success, return "OK"; or throw an exception
	 * @throws Exception
	 */
	public String setBeanExpiredIn(String key, Map<String, String> hashes,
			int seconds) throws Exception {
		JedisAssert.assertNotNulls(key, hashes, seconds);
		String resp = this.setBean(key, hashes);
		this.setExpiredIn(key, seconds);
		return resp;
	}

	/**
	 * Get the field value of a bean with the key
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#hget(String, String)}
	 * @param key
	 * @param field
	 * @return Bulk reply
	 */
	public String getBean(String key, String field) throws Exception {
		JedisAssert.assertNotNulls(key, field);
		return this.jedis.hget(key, field);
	}

	/**
	 * Get the values of specified fields of a bean with the key, in the same
	 * order of the request
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#hmget(String, String...)}
	 * @param key
	 * @param fields
	 * @return Multi Bulk Reply specifically a list of all the values associated
	 *         with the specified fields
	 * @throws Exception
	 */
	public List<String> getBean(String key, String... fields) throws Exception {
		JedisAssert.assertNotNulls(key, fields);
		return this.jedis.hmget(key, fields);
	}

	/**
	 * Get all the <field, value> pairs of a bean with the key
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#hgetAll(String)}
	 * @param key
	 * @return	All the fields and values contained into a hash.
	 * @throws Exception
	 */
	public Map<String, String> getBean(String key) throws Exception {
		JedisAssert.assertNotNulls(key);
		return this.jedis.hgetAll(key);
	}
	
	/**
	 * Return the ShardedJedis instance back to pool
	 */
	public void exitWrapper() throws Exception {
		pool.returnResource(jedis);
	}

	/**
	 * Only for advanced usage who already knows how to use the Jedis library
	 * 
	 * @return	The ShardedJedis instance
	 */
	public ShardedJedis getJedisInstance() {
		return this.jedis;
	}
	
	public String getVersion() {
		return "1.0";
	}

}
