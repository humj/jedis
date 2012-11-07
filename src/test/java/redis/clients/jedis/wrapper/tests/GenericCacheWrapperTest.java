/**
 * 
 */
package redis.clients.jedis.wrapper.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Tuple;
import redis.clients.jedis.wrapper.GenericCacheWrapper;

/**
 * Test case for {@link GenericCacheWrapper}
 * 
 * @author joey
 * 
 */
public class GenericCacheWrapperTest extends Assert {

	private GenericCacheWrapper wrapper;

	// Test key value
	private String key1 = "key1";

	/**
	 * Initialize a GenericCacheWrapper instance
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		wrapper = new GenericCacheWrapper();
	}

	/**
	 * Return jedis resource
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		try {
			this.wrapper.delKey(key1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				this.wrapper.exitWrapper();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertEquals(new Boolean(false),
				this.wrapper.unwrapperJedis().exists(key1)); // True
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#setExpiredIn(java.lang.String, int)}
	 * .
	 */
	@Test
	public void testSetExpiredIn() {
		try {
			this.wrapper.setString(key1, "value1"); // {key1, value1}
			this.wrapper.setExpiredIn(key1, 5);	// expired in 5s
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(new Long(-1L),
				new Long(this.wrapper.unwrapperJedis().ttl(key1))); // True
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getString(java.lang.String)}
	 * . With unit test for:
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#setStringWithExpiredIn(String, String, int)}
	 * 
	 */
	@Test
	public void testStringOpr() {
		try {
			// Set a new key and set the expire time
			this.wrapper.setStringWithExpiredIn(key1, "testGetString", 5);
			assertEquals("testGetString", this.wrapper.getString(key1)); // True

			// Thread.sleep(5s)
			Thread.sleep(6000);

			// now, the value of key1 is null
			assertNull(this.wrapper.getString(key1)); // True

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getBean(java.lang.String, java.lang.String)}
	 * . With unit tests for:
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#setBean(String, String, String)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#setBeanWithExpiredIn(String, String, String, int)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#setBean(String, Map)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getBean(String, String...)}
	 * 
	 */
	@Test
	public void testBeanOpt() {
		try {
			// Test for wrapper.setBean(String, String, String)
			this.wrapper.setBean(key1, "field1", "value1");	// {key1, [{field1, value1}]}
			assertEquals("value1", this.wrapper.getBean(key1, "field1")); // True

			// Test for wrapper.setBean(String, String, String, int)
			this.wrapper.setBeanWithExpiredIn(key1, "field2", "value2", 5); // key1 expired in 5s

			// sleep 6s
			Thread.sleep(6000); // {key1, [{}]}
			// later set with expired will override the former settings on the
			// same key
			assertEquals(new HashMap<String, String>(),
					this.wrapper.getBean(key1)); // True

			// Test for wrapper.setBean(String, Map<String, String>)
			Map<String, String> bean = new HashMap<String, String>();
			bean.put("field1", "value1");
			this.wrapper.setBean(key1, bean); // {key1, [{field1, value1}]}
			assertEquals(bean, this.wrapper.getBean(key1));

			// Test for wrapper.getBean(String, List...)
			this.wrapper.setBean(key1, "field2", "value2"); // {key1,[{field1,value1},{field2,value2}]}
			List<String> res = new ArrayList<String>();
			res.add("value1");
			res.add("value2");
			List<String> valueList = this.wrapper.getBean(key1, "field1",
					"field2");
			assertEquals(res, valueList); // True, valueList = [value1, value2]

			// Test for wrapper.getBean(String, List...) == ArrayList(null, null)
			this.wrapper.delKey(key1);
			valueList = this.wrapper.getBean(key1, "field1", "field2");
			assertNull(this.wrapper.getBean(key1, "field1")); // True

			List<String> expeted = new ArrayList<String>();
			expeted.add(null);
			expeted.add(null);
			assertEquals(expeted, valueList); // True, key not exists, return [null, null]

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#leftPushList(java.lang.String, java.lang.String[])}
	 * .With unit tests for:
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#leftPopList(String)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#rightPopList(String)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#rightPushAndSetListExpiredIn(String, int, String...)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#trimList(String, long, long)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getRangeList(String, long, long)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#removeAllEqualElementsInList(String, String)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#removeL2REqualElementInList(String, String, long)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#removeR2LEqualElementInList(String, String, long)}
	 * 
	 */
	@Test
	public void testListOpr() {
		try {
			// Test for wrapper.leftPushList(String, String)
			this.wrapper.leftPushList(key1, "value1");
			this.wrapper.leftPushList(key1, "value2");// ["value2"->"value1"]
			assertEquals("value1", this.wrapper.rightPopList(key1)); // True, ["value2"]
			assertEquals("value2", this.wrapper.rightPopList(key1)); // True, []

			// Test for wrapper.rightPushAndSetListExpiredIn(String, int, String...)
			this.wrapper.rightPushAndSetListExpiredIn(key1, 5, "value1",
					"value2"); // ["value1"->"value2"]
			// sleep > 5s
			Thread.sleep(6000);
			assertNull(this.wrapper.leftPopList(key1));
			assertNull(this.wrapper.leftPopList(key1));

			// Test for wrapper.trimList(String, long, long)
			this.wrapper.leftPushList(key1, "1", "2", "3", "4"); // ["4"->"3"->"2"->"1"]
			this.wrapper.trimList(key1, 0, 2); // ["4"->"3"->"2"]
			assertEquals(new Long(3L),
					new Long(this.wrapper.getListLength(key1)));

			// Test for wrapper.getRangeList(String, long, long)
			List<String> expected = new ArrayList<String>();
			expected.add("3");
			expected.add("2");
			assertEquals(expected, this.wrapper.getRangeList(key1, 1, 2)); // True, return ["3"->"2"]

			// Test for wrapper.removeAllEqualElementsInList()
			this.wrapper.rightPushList(key1, "1", "3"); // ["4"->"3"->"2"->"1"->"3"]
			this.wrapper.removeAllEqualElementsInList(key1, "3"); // ["4"->"2"->"1"]
			expected.clear();
			expected.add("4"); expected.add("2"); expected.add("1");
			assertEquals(expected, this.wrapper.getRangeList(key1, 0, -1));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#appendZSet(String, double, String)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#appendZSet(String, Map)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getZSetLength(String)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getCountZSetByScores(String, double, double)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getIndexZSet(String, String)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getRangeZSet(String, long, long)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getRangeZSetByScoreWithScores(String, double, double)}
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper
	 * 
	 * 
	 */
	@Test
	public void testZSetOpr() {
		try {
			// Test for wrapper.appendZSet(key, score, value)
			wrapper.appendZSet(key1, 15.0, "im 15");
			// Test for wrapper.appendZSet(key, map<score, value>)
			Map<Double, String> scoreMembersMap = new HashMap<Double, String>();
			scoreMembersMap.put(10.0, "im 10");
			scoreMembersMap.put(20.0, "im 20");
			wrapper.appendZSet(key1, scoreMembersMap);
			// Test for wrapper.getZSetLength()
			assertEquals(Long.valueOf(3), wrapper.getZSetLength(key1));
			// Test for wrapper.getCountZSetByScores(key, min, max)
			assertEquals(Long.valueOf(2), wrapper.getCountZSetByScores(key1, 15.0, 20.0));
			// Test for wrapper.getIndexZSet(key, member)
			assertEquals(Long.valueOf(1), wrapper.getIndexZSet(key1, "im 15"));
			assertNull(wrapper.getIndexZSet(key1, "im null"));
			// Test for wrapper.getRangeZSet(key, start, stop)
			Set<String> respSet = new HashSet<String>();
			respSet.add("im 10"); respSet.add("im 15"); respSet.add("im 20");
			assertEquals(respSet, wrapper.getRangeZSet(key1, 0, -1));
			// Test for wrapper.getRangeZSetByScoreWithScores(key, minScore, maxScore)
			Set<Tuple> tupleSet = new HashSet<Tuple>();
			tupleSet.add(new Tuple("im 15", 15.0));
			tupleSet.add(new Tuple("im 20", 20.0));
			assertEquals(tupleSet, wrapper.getRangeZSetByScoreWithScores(key1, 15.0, 20.0));
			// Test for wrapper.removeElementsZSet(key, member)
			System.out.println(wrapper.removeElementsZSet("key2", "members"));
			assertEquals(Long.valueOf(0), wrapper.removeElementsZSet("key2", "members"));
			// Test for wrapper.
			wrapper.removeElementsZSet(key1, "im 15");
			assertEquals(Long.valueOf(2), wrapper.getZSetLength(key1));
			
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#appendZSet(java.lang.String, double, java.lang.String)}
	 * .
	 */
	@Test
	public void testAppendZSetStringDoubleString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#appendZSet(java.lang.String, java.util.Map)}
	 * .
	 */
	@Test
	public void testAppendZSetStringMapOfDoubleString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#appendZSetAndZSetExpiredIn(java.lang.String, double, java.lang.String, int)}
	 * .
	 */
	@Test
	public void testAppendZSetAndZSetExpiredInStringDoubleStringInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#appendZSetAndZSetExpiredIn(java.lang.String, java.util.Map, int)}
	 * .
	 */
	@Test
	public void testAppendZSetAndZSetExpiredInStringMapOfDoubleStringInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#removeElementsZSet(java.lang.String, java.lang.String[])}
	 * .
	 */
	@Test
	public void testRemoveElementsZSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#removeElementsZSetByScore(java.lang.String, double, double)}
	 * .
	 */
	@Test
	public void testRemoveElementsZSetByScore() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#removeElementsZSetByIndex(java.lang.String, long, long)}
	 * .
	 */
	@Test
	public void testRemoveElementsZSetByIndex() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getRangeZSet(java.lang.String, long, long)}
	 * .
	 */
	@Test
	public void testGetRangeZSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getRangeZSetWithScores(java.lang.String, long, long)}
	 * .
	 */
	@Test
	public void testGetRangeZSetWithScores() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getRangeZSetByScore(java.lang.String, double, double)}
	 * .
	 */
	@Test
	public void testGetRangeZSetByScore() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getRangeZSetByScoreWithScores(java.lang.String, double, double)}
	 * .
	 */
	@Test
	public void testGetRangeZSetByScoreWithScores() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getIndexZSet(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetIndexZSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getLengthZSet(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetLengthZSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getCountZSetByScores(java.lang.String, double, double)}
	 * .
	 */
	@Test
	public void testGetCountZSetByScores() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getScoreOfMemberZSet(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetScoreOfMemberZSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#exitWrapper()}.
	 */
	@Test
	public void testExitWrapper() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#unwrapperJedis()}.
	 */
	@Test
	public void testUnwrapperJedis() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link redis.clients.jedis.wrapper.GenericCacheWrapper#getVersion()}.
	 */
	@Test
	public void testGetVersion() {
		fail("Not yet implemented");
	}

}
