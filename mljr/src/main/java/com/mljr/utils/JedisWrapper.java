package com.mljr.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * ClassName:JedisWrapper <br/>
 * Function: Redis客户端 <br/>
 * Date:     2016年4月5日 上午11:25:01 <br/>
 * @author   tongzheng.zhang
 * @version  1.0
 * @since    JDK 1.8
 */
public class JedisWrapper {
	
	private static Logger logger = LoggerFactory.getLogger(com.mljr.yourcredit.installment.common.JedisWrapper.class);

	/**
	 * redis服务器地址 
	 */
	private String redisServerHost;
	
	private int redisServerPort;
	
	private String redisServerPassword;
	
	private int redisServerTimeout;
	/**
	 * 最大连接数
	 */
	public static final int CONFIG_MAX_ACTIVE = 500;
	
	/**
	 * 最大空闲连接数，-1 表示无限制
	 */
	public static final int CONFIG_MAX_IDLE = -1;
	
	/**
	 * 取一个连接的最长阻塞时间, milliseconds. 此处设置为10秒
	 */
	public static final int CONFIG_MAX_WAITE_MILLISECOND = 1000; // 10 seconds.
	
	/**
	 * 连接池达到最大连接数后取连接的行为:此处为阻塞等待，直到有连接返回，或达到最大阻塞时间返回失败
	 */
	public static final byte CONFIG_EXHAUSTED_ACTION = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
	
	private static JedisPoolConfig config;
	
	private static JedisPool pool;
	
	
	/**
	 * 私有构造器，初始化连接池
	 *  
	 */
	public JedisWrapper() {
		config = new JedisPoolConfig();
		config.setMaxTotal(CONFIG_MAX_ACTIVE);
		config.setMaxIdle(CONFIG_MAX_IDLE);
		config.setMaxWaitMillis(CONFIG_MAX_WAITE_MILLISECOND);
		config.setBlockWhenExhausted(false);
		if(redisServerHost != null && redisServerPassword != null) {
			pool = new JedisPool(config, redisServerHost,redisServerPort,redisServerTimeout, redisServerPassword);
		}
	}
	
	/**
	 * 从连接池中获取Jedis,不对外开放
	 * @return
	 * 		Jedis实例
	 */
	private Jedis getJedis() {
		if(pool == null) {
			pool = new JedisPool(config, redisServerHost,redisServerPort,redisServerTimeout, redisServerPassword);
		}
		
		Jedis jedis = pool.getResource();
		
		return jedis;
	}
	
	/**
	 * 
	 * execute:执行有返回值的redis操作，这是模板，只须实现逻辑即可. <br/>
	 *
	 * @author tongzheng.zhang
	 * @param action
	 * @return
	 * @since JDK 1.8
	 */
	public <T> T execute(JedisAction<T> action) {
		try(Jedis jedis = getJedis()) {
			return action.action(jedis);
		} catch (Exception e) {
			logger.error("error when execute redis command->{}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * execute:执行无返回值的redis操作，这是模板，只须实现逻辑即可. <br/>
	 *
	 * @author tongzheng.zhang
	 * @param action
	 * @since JDK 1.8
	 */
	public void execute(VoidJedisAction action) {
		try(Jedis jedis = getJedis()) {
			action.action(jedis);
		} catch (Exception e) {
			logger.error("error when execute redis command->{}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public Boolean pipelineEvict(String keyPattern) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				ScanParams scanParams = new ScanParams().count(100);
				scanParams.match(keyPattern);
				String cursor = ScanParams.SCAN_POINTER_START;
				boolean stop = false;
				List<String> collector = new ArrayList<>(50000);
				while (!stop) {
					ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
					List<String> keys = scanResult.getResult();
					collector.addAll(keys);
					cursor = scanResult.getStringCursor();
					stop = cursor.equals(ScanParams.SCAN_POINTER_START);
				}
				Pipeline pipeline = jedis.pipelined();
				collector.forEach(c -> {
					jedis.del(c);
				});
				pipeline.sync();
				return Boolean.TRUE;
			}
		});
		
	}
	
	public Boolean evict(String keyPattern) {
		if (StringUtils.isNotBlank(keyPattern) && !keyPattern.trim().startsWith("*")) {
			execute(new JedisAction<Boolean>() {

				@Override
				public Boolean action(Jedis jedis) {
					long s = System.currentTimeMillis();
					Set<String> keys = jedis.keys(keyPattern);
					if (CollectionUtils.isNotEmpty(keys)) {
						keys.stream().forEach(k -> jedis.del(k));
						logger.info("evict {}->size:{} takes time:{}", keyPattern, keys.size(), (System.currentTimeMillis() - s) / 1000);
					}
					
					return Boolean.TRUE;
				}
			});
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 
	 * set:set操作. <br/>
	 *
	 * @author tongzheng.zhang
	 * @param key
	 * @param value
	 * @since JDK 1.8
	 */
	public void set(String key, String value) {
		execute(new JedisAction<String>() {

			@Override
			public String action(Jedis jedis) {
				return jedis.set(key, value);
			}
		});
	}
	   /**
     * 
     * exists:(判断是否存在该key). <br/>
     *
     * @author jiangtao.liu
     * @param key
     * @return
     * @since JDK 1.8
     */
    public Boolean exists(String key) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }
    
    /**
     * 
     * sadd:(放入随机数组). <br/>
     *
     * @author jiangtao.liu
     * @param key
     * @param values
     * @return
     * @since JDK 1.8
     */
    public Long sadd(String key, String... values) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.sadd(key, values);
            }
        });
    }
    
    /**
     * 
     * spop:(随机删除一个元素). <br/>
     *
     * @author jiangtao.liu
     * @param key
     * @param values
     * @return
     * @since JDK 1.8
     */
	public String spop(String key) {
		return execute(new JedisAction<String>() {
			@Override
			public String action(Jedis jedis) {
				Transaction tx = jedis.multi();
				// Response<Set<String>> resp = tx.smembers(key);
				Response<String> res = tx.spop(key);
				tx.exec();
				// if(resp.get().size() > 0){
				// return res.get();
				// }else{
				// return null;
				// }
				if (res != null && res.get() != null) {
					return res.get();
				} else {
					return null;
				}
			}
		});
	}

    /**
     * 
     * smembers:(这里用一句话描述这个方法的作用). <br/>
     *
     * @author jiangtao.liu
     * @param key
     * @return
     * @since JDK 1.8
     */
    public Set<String> smembers(String key) {
        return execute(new JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.smembers(key);
            }
        });
    }
    
    /**
     * 
     * del:(这里用一句话描述这个方法的作用). <br/>
     *
     * @author jiangtao.liu
     * @param key
     * @return
     * @since JDK 1.8
     */
    public Long del(String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.del(key);
            }
        });
    }
	/**
	 * 
	 * get:根据key获取value. <br/>
	 *
	 * @author tongzheng.zhang
	 * @param key
	 * @return
	 * @since JDK 1.8
	 */
	public String get(String key) {
		return execute(new DefaultGetAction(key));
	}
	
	/**
	 * 
	 * del:删除. <br/>
	 *
	 * @author tongzheng.zhang
	 * @param keys
	 * @return
	 * @since JDK 1.8
	 */
	public Long del(String... keys) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.del(keys);
			}
		});
	}
	
	/**
	 * 
	 * ClassName:JedisAction <br/>
	 * Function: 需要返回值的Redis操作. <br/>
	 * Date:     2016年4月5日 上午11:30:51 <br/>
	 * @author   tongzheng.zhang
	 * @version  1.0
	 * @since    JDK 1.8
	 */
	public static interface JedisAction<T> {
		T action(Jedis jedis);
	}
	
	public static class DefaultGetAction implements JedisAction<String> {

		/**
		 * Creates a new instance of GetStringAction.
		 *
		 * @param key
		 */
		public DefaultGetAction(String key) {
			this.key = key;
		}

		String key;
		
		/**
		 * @see com.mljr.yourcredit.installment.common.JedisWrapper.JedisAction#action(Jedis)
		 */
		@Override
		public String action(Jedis jedis) {
			return jedis.get(key);
		}
		
	}
	
	/**
	 * 
	 * ClassName:VoidJedisAction <br/>
	 * Function: 无返回值的Redis操作. <br/>
	 * Date:     2016年4月5日 上午11:31:22 <br/>
	 * @author   tongzheng.zhang
	 * @version  1.0
	 * @since    JDK 1.8
	 */
	public static interface VoidJedisAction {
		void action(Jedis jedis);
	}

	public void setRedisServerHost(String redisServerHost) {
		this.redisServerHost = redisServerHost;
	}

	public void setRedisServerPort(int redisServerPort) {
		this.redisServerPort = redisServerPort;
	}

	public void setRedisServerPassword(String redisServerPassword) {
		this.redisServerPassword = redisServerPassword;
	}

	public void setRedisServerTimeout(int redisServerTimeout) {
		this.redisServerTimeout = redisServerTimeout;
	}

	/**
	 * 
	 * hash:hset操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @param field
	 * @param value
	 * @return Long
	 * @since JDK 1.8
	 */
	public Long hset(byte[] key, byte[] field, byte[] value) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		});
	}

	/**
	 * 
	 * hash:hget操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @param field
	 * @return byte[]
	 * @since JDK 1.8
	 */
	public byte[] hget(byte[] key, byte[] field) {
		return execute(new JedisAction<byte[]>() {

			@Override
			public byte[] action(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});
	}
	
	/**
	 * 
	 * hash:hdel操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @param field
	 * @return Long
	 * @since JDK 1.8
	 */
	public Long hdel(byte[] key, byte[] field) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.hdel(key, field);
			}
		});
	}
	
	/**
	 * 
	 * hash:hgetAll操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @return Map<byte[], byte[]>
	 * @since JDK 1.8
	 */
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return execute(new JedisAction<Map<byte[], byte[]>>() {

			@Override
			public Map<byte[], byte[]> action(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		});
	}
	
	/**
	 * 
	 * hash:hvals操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @return List<byte[]>
	 * @since JDK 1.8
	 */
	public List<byte[]> hvals(byte[] key) {
		return execute(new JedisAction<List<byte[]>>() {

			@Override
			public List<byte[]> action(Jedis jedis) {
				return jedis.hvals(key);
			}
		});
	}
	
	/**
	 * 
	 * hash:hkeys操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @return Set<byte[]>
	 * @since JDK 1.8
	 */
	public Set<byte[]> hkeys(byte[] key) {
		return execute(new JedisAction<Set<byte[]>>() {

			@Override
			public Set<byte[]> action(Jedis jedis) {
				return jedis.hkeys(key);
			}
		});
	}
	
	/**
	 * 
	 * expire操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @param seconds
	 * @return Long
	 * @since JDK 1.8
	 */
	public Long expire(byte[] key, int seconds) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
	}
	
	/**
	 * 
	 * expire操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @param seconds
	 * @return Long
	 * @since JDK 1.8
	 */
	public Long expire(String key, int seconds) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
	}
	
	/**
	 * 
	 * list:lpop操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @return byte[]
	 * @since JDK 1.8
	 */
	public byte[] lpop(byte[] key) {
		return execute(new JedisAction<byte[]>() {

			@Override
			public byte[] action(Jedis jedis) {
				return jedis.lpop(key);
			}
		});
	}

	/**
	 *
	 * list:blpop操作. <br/>
	 * 返回列表的头元素
	 *
	 * @author changjiu.he
	 * @param key
	 * @param timeout 超时时间(秒)
	 * @return List<String>
	 * @since JDK 1.8
	 */
	public List<String> blpop(int timeout, String... key) {
		return execute(new JedisAction<List<String>>() {
			@Override
			public List<String> action(Jedis jedis) {
				return jedis.blpop(timeout, key);
			}
		});
	}

	/**
	 *
	 * list:brpop操作. <br/>
	 * 返回列表的尾元素
	 *
	 * @author changjiu.he
	 * @param key
	 * @param timeout 超时时间(秒)
	 * @return List<String>
	 * @since JDK 1.8
	 */
	public List<String> brpop(int timeout, String... key) {
		return execute(new JedisAction<List<String>>() {
			@Override
			public List<String> action(Jedis jedis) {
				return jedis.brpop(timeout, key);
			}
		});
	}

	/**
	 * 
	 * list:lpush操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @param strings
	 * @return Long
	 * @since JDK 1.8
	 */
	public Long lpush(byte[] key, byte[] strings) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.lpush(key, strings);
			}
		});
	}
	
	public Long lpush(String key, String value) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.lpush(key, value);
			}
		});
	}
	
	public String rpop(String key) {
		return execute(new JedisAction<String>() {

			@Override
			public String action(Jedis jedis) {
				return jedis.rpop(key);
			}
		});
	}
	
	public boolean isMember(String key, String value) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.sismember(key, value);
			}
		});
	}
	
	public Long llen(String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.llen(key);
			}
		});
	}
	
	/**
	 * 
	 * list:rpop操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @return byte[]
	 * @since JDK 1.8
	 */
	public byte[] rpop(byte[] key) {
		return execute(new JedisAction<byte[]>() {

			@Override
			public byte[] action(Jedis jedis) {
				return jedis.rpop(key);
			}
		});
	}

	/**
	 * 
	 * list:rpush操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @param strings
	 * @return Long
	 * @since JDK 1.8
	 */
	public Long rpush(byte[] key, byte[] strings) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.rpush(key, strings);
			}
		});
	}
	
	/**
	 * 
	 * list:llen操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @return Long
	 * @since JDK 1.8
	 */
	public Long llen(byte[] key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.llen(key);
			}
		});
	}
	
	/**
	 * 
	 * list:ltrim操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @param start
	 * @param end
	 * @return String
	 * @since JDK 1.8
	 */
	public String ltrim(byte[] key,long start, long end) {
		return execute(new JedisAction<String>() {

			@Override
			public String action(Jedis jedis) {
				return jedis.ltrim(key, start, end);
			}
		});
	}
	
	/**
	 * 
	 * list:lrange操作. <br/>
	 *
	 * @author wei.guo
	 * @param key
	 * @param start
	 * @param end
	 * @return String
	 * @since JDK 1.8
	 */
	public List<byte[]> lrange(byte[] key,long start, long end) {
		return execute(new JedisAction<List<byte[]>>() {

			@Override
			public List<byte[]> action(Jedis jedis) {
				return jedis.lrange(key, start, end);
			}
		});
	}

    /**
     * setnx: set if not exists 用于分布式锁<br/>
     * @author tao.xiao
     * @param key
     * @param value
     * @return 1:key的值被设置（获得锁） 0:key的值未设置（未获得锁）
     */
    public Long setnx(String key,String value) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.setnx(key, value);
            }
        });
    }
}