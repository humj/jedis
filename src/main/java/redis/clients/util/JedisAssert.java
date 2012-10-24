/**
 * 
 */
package redis.clients.util;

import org.junit.Assert;

/**
 * @author joey
 *
 */
public class JedisAssert extends Assert {
	
	public JedisAssert() {
		super();
	}
	
	public static void assertNotNulls(Object... objs ) {
		for (Object obj : objs) {
			assertNotNull(obj);
		}
	}
	
}
