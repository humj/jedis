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
	 * @return All the fields and values contained into a hash.
	 * @throws Exception
	 */
	public Map<String, String> getBean(String key) throws Exception {
		JedisAssert.assertNotNulls(key);
		return this.jedis.hgetAll(key);
	}

	/**
	 * Push elements into the list with the key from left side
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#lpush(String, String...)}
	 * @param key
	 * @param strings
	 * @return Integer reply, specifically, the number of elements inside the
	 *         list after the push operation.
	 * @throws Exception
	 */
	public Long leftPushList(String key, String... strings) throws Exception {
		JedisAssert.assertNotNulls(key, strings);
		return this.jedis.lpush(key, strings);
	}

	/**
	 * Push elements into the list with the key from right side
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#rpush(String, String...)}
	 * @param key
	 * @param strings
	 * @return Integer reply, specifically, the number of elements inside the
	 *         list after the push operation.
	 * @throws Exception
	 */
	public Long rightPushList(String key, String... strings) throws Exception {
		JedisAssert.assertNotNulls(key, strings);
		return this.jedis.rpush(key, strings);
	}

	/**
	 * Pop the element in list from left side
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#lpop(String)}
	 * @param key
	 * @return If the key exists, return the last item; If the key does not
	 *         exist or the list is already empty, return null
	 * @throws Exception
	 */
	public String leftPopList(String key) throws Exception {
		JedisAssert.assertNotNulls(key);
		String resp = this.jedis.lpop(key);
		return resp.equals("nil") ? null : resp;
	}

	/**
	 * Pop the element in list from right side
	 * 
	 * @see {@link redis.clients.jedis.ShardedJedis#rpop(String)}
	 * @param key
	 * @return If the key exists, return the last item; If the key does not
	 *         exist or the list is already empty, return null
	 * @throws Exception
	 */
	public String rightPopList(String key) throws Exception {
		JedisAssert.assertNotNulls(key);
		String resp = this.jedis.rpop(key);
		return resp.equals("nil") ? null : resp;
	}

/**
	 * Trim an existing list so that it will contain only the specified range of
     * elements specified. Start and end are zero-based indexes. 0 is the first
     * element of the list (the list head), 1 the next element and so on.
     * <p>
     * For example LTRIM foobar 0 2 will modify the list stored at foobar key so
     * that only the first three elements of the list will remain.
     * <p>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * 
	 * @see {@link redis.clients.jedis.ShardedJedis#ltrim(String, long, long)
	 * @param key
	 * @param start
	 * @param end
	 * @return Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, AN EMPTY LIST is left as value.
	 * @throws Exception
	 */
	public String trimList(String key, long start, long end) throws Exception {
		JedisAssert.assertNotNulls(key, start, end);
		return this.jedis.ltrim(key, start, end);
	}

	/**
	 * Return the specified elements of the list stored at the specified key.
	 * Start and end are zero-based indexes. 0 is the first element of the list
	 * (the list head), 1 the next element and so on.
	 * <p>
	 * For example LRANGE foobar 0 2 will return the first three elements of the
	 * list.
	 * <p>
	 * start and end can also be negative numbers indicating offsets from the
	 * end of the list. For example -1 is the last element of the list, -2 the
	 * penultimate element and so on.
	 * <p>
	 * <b>Consistency with range functions in various programming languages</b>
	 * <p>
	 * Note that if you have a list of numbers from 0 to 100, LRANGE 0 10 will
	 * return 11 elements, that is, rightmost item is included.
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return Multi bulk reply, specifically a list of elements in the
	 *         specified range.Indexes out of range will not produce an error:
	 *         if start is over the end of the list, or start > end, an empty
	 *         list is returned. If end is over the end of the list Redis will
	 *         threat it just like the last element of the list.
	 * @throws Exception
	 */
	public List<String> getRangeList(String key, long start, long end)
			throws Exception {
		JedisAssert.assertNotNulls(key, start, end);
		return this.jedis.lrange(key, start, end);
	}

	/**
	 * Remove all the elements which equals to specified value
	 * 
	 * @param key
	 * @param value
	 * @return The number of removed elements successfully
	 * @throws Exception
	 */
	public Long removeAllElementEquealsToInList(String key, String value)
			throws Exception {
		JedisAssert.assertNotNulls(key, value);
		return this.removeElementsEqualsToInList(key, 0, value);
	}

	/**
	 * Remove specified count occurrences of the value from Left to Right in
	 * list
	 * 
	 * @param key
	 * @param value
	 * @param count
	 * @return The number of removed elements successfully
	 * @throws Exception
	 */
	public Long removeElementEqualsToL2RInList(String key, String value,
			long count) throws Exception {
		JedisAssert.assertNotNulls(key, value, count);
		return this.removeElementsEqualsToInList(key, Math.abs(count), value);
	}

	/**
	 * Remove specified count occurrences of the value from Right to Left in
	 * list
	 * 
	 * @param key
	 * @param value
	 * @param count
	 * @return The number of removed elements successfully
	 * @throws Exception
	 */
	public Long removeElementEqualsToR2LInList(String key, String value,
			long count) throws Exception {
		JedisAssert.assertNotNulls(key, value, count);
		return this.removeElementsEqualsToInList(key, 0 - Math.abs(count),
				value);
	}

	/**
	 * Remove the first count occurrences of the value element from the list. If
	 * count is zero all the elements are removed. If count is negative elements
	 * are removed from tail to head, instead to go from head to tail that is
	 * the normal behaviour.
	 * 
	 * @param key
	 * @param value
	 * @param count
	 * @return Integer Reply, specifically: The number of removed elements if
	 *         the operation succeeded
	 */
	private Long removeElementsEqualsToInList(String key, long count,
			String value) throws Exception {
		return this.jedis.lrem(key, count, value);
	}

	/**
	 * Return the length of the list stored at the specified key.
	 * 
	 * @param key
	 * @return If the key does not exist zero is returned; if the value stored
	 *         at key is not a list an error is returned.
	 * @throws Exception
	 */
	public Long getListLength(String key) throws Exception {
		return this.jedis.llen(key);
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
	 * @return The ShardedJedis instance
	 */
	public ShardedJedis getJedisInstance() {
		return this.jedis;
	}

	public String getVersion() {
		return "1.0";
	}

}
