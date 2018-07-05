package com.racetime.xsad.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

/**
 * 
* 项目名称：ad-service   
* 类名称：RedisDao   
* 类描述：   操作字符串redis缓存方法，list中的操作全是按照right方式
* 创建人：skg  
* 创建时间：2017-7-12 下午5:24:20   
* @version    
*
 */
@Repository
public class RedisDao {
	/**
	 * 日志记录
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public StringRedisTemplate redisTemplate;
	
	@Autowired
	public StringRedisTemplate redisTemplate_db2;

	/**
     * 缓存value操作
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheValue(String k, String v, long time) {
        try {
            ValueOperations<String, String> valueOps =  redisTemplate.opsForValue();
            valueOps.set(k, v);
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存value操作
     * @param k
     * @param v
     * @return
     */
    public boolean cacheValue(String k, String v) {
        return cacheValue(k, v, -1);
    }

    /**
     * 判断缓存是否存在
     * @param k
     * @return
     */
    public boolean containsKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Throwable t) {
            logger.error("判断缓存存在失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 获取缓存
     * @param k
     * @return
     */
    public String getValue(String k) {
        try {
            ValueOperations<String, String> valueOps =  redisTemplate.opsForValue();
            return valueOps.get(k);
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }


    /**
     * 移除缓存
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }
    /**
     * 缓存set操作
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheSet(String k, String v, long time) {
        try {
            SetOperations<String, String> valueOps =  redisTemplate.opsForSet();
            valueOps.add(k, v);
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存set
     * @param k
     * @param v
     * @return
     */
    public boolean cacheSet(String k, String v) {
        return cacheSet(k, v, -1);
    }

    /**
     * 缓存set
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheSet(String k, Set<String> v, long time) {
        try {
            SetOperations<String, String> setOps =  redisTemplate.opsForSet();
            setOps.add(k, v.toArray(new String[v.size()]));
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存set
     * @param k
     * @param v
     * @return
     */
    public boolean cacheSet(String k, Set<String> v) {
        return cacheSet(k, v, -1);
    }

    /**
     * 获取缓存set数据
     * @param k
     * @return
     */
    public Set<String> getSet(String k) {
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            return setOps.members(k);
        } catch (Throwable t) {
            logger.error("获取set缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 查询set中是否包含某个元素
     * @param k
     * @param o
     * @return
     */
    public boolean isMemberSet(String k,Object o){
    	 try {
             SetOperations<String, String> setOps = redisTemplate.opsForSet();
             return setOps.isMember(k, o);
         } catch (Throwable t) {
             logger.error("查询set中元素失败key[" + k + ", error[" + t + "]");
         }
         return false;
    }
    
    /**
     * list缓存
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheList(String k, String v, long time) {
        try {
            ListOperations<String, String> listOps =  redisTemplate.opsForList();
            listOps.rightPush(k, v);
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存list
     * @param k
     * @param v
     * @return
     */
    public boolean cacheList(String k, String v) {
        return cacheList(k, v, -1);
    }

    /**
     * 缓存list
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheList(String k, List<String> v, long time) {
        try {
            ListOperations<String, String> listOps =  redisTemplate.opsForList();
            long l = listOps.rightPushAll(k, v);
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存list
     * @param k
     * @param v
     * @return
     */
    public boolean cacheList(String k, List<String> v) {
       return cacheList(k, v, -1);
    }

    /**
     * 获取list缓存
     * @param k
     * @param start
     * @param end
     * @return
     */
    public List<String> getList(String k, long start, long end) {
        try {
            ListOperations<String, String> listOps =  redisTemplate.opsForList();
            return listOps.range(k, start, end);
        } catch (Throwable t) {
            logger.error("获取list缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }
    /**
     * 获取list缓存
     * @param k
     */
    public Object getPopList(String k){
    	try{
    		ListOperations<String, String> listOps =  redisTemplate.opsForList();
    		return listOps.leftPop(k);
    	}catch(Throwable t){
    		logger.error("获取list缓存失败key[" + k + ", error[" + t + "]");
    	}
    	return null;
    }
    
    /**
     * 获取总条数, 可用于分页
     * @param k
     * @return
     */
    public long getListSize(String k) {
        try {
            ListOperations<String, String> listOps =  redisTemplate.opsForList();
            return listOps.size(k);
        } catch (Throwable t) {
            logger.error("获取list长度失败key[" + k + "], error[" + t + "]");
        }
        return 0;
    }

    /**
     * 获取总条数, 可用于分页
     * @param listOps
     * @param k
     * @return
     */
    public long getListSize(ListOperations<String, String> listOps, String k) {
        try {
            return listOps.size(k);
        } catch (Throwable t) {
            logger.error("获取list长度失败key[" + k + "], error[" + t + "]");
        }
        return 0;
    }

    /**
     * 移除list缓存
     * @param k
     * @return
     */
    public boolean removeOneOfList(String k) {
        try {
            ListOperations<String, String> listOps =  redisTemplate.opsForList();
            listOps.rightPop(k);
            return true;
        } catch (Throwable t) {
            logger.error("移除list缓存失败key[" + k + ", error[" + t + "]");
        }
        return false;
    }
    
    /**
     * 缓存hash
     * @param hk
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheHash(String hk, String k, Object v, long time) {
        try {
            HashOperations<String, String, Object> hashOps =  redisTemplate.opsForHash();
            hashOps.put(hk, k, v);
            if (time > 0) redisTemplate.expire(hk, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }
    
    /**
     * 缓存hash
     * @param hk
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheHash(String hk, String k, Object v) {
    	return cacheHash(hk, k, v, -1);
    }
    
    /**
     * 获取map缓存
     * @param k
     * @param start
     * @param end
     * @return
     */
    public Object getHash(String hk, String k) {
        try {
            HashOperations<String, String, Object> hashOps =  redisTemplate.opsForHash();
            return hashOps.get(hk, k);
        } catch (Throwable t) {
            logger.error("获取hash缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }
    
    /**
     * 获取map缓存
     * @param k
     * @param start
     * @param end
     * @return
     */
    public Map<String, Object> getHashAll(String hk) {
        try {
            HashOperations<String, String, Object> hashOps =  redisTemplate.opsForHash();
            return hashOps.entries(hk);
        } catch (Throwable t) {
            logger.error("获取hashAll缓存失败key[" + hk + ", error[" + t + "]");
        }
        return null;
    }
    
    /**
     * 移除list缓存
     * @param k
     * @return
     */
    public boolean removeOneOfHash(String hk, String k) {
        try {
        	HashOperations<String, String, Object> hashOps =  redisTemplate.opsForHash();
        	hashOps.delete(hk, k);
            return true;
        } catch (Throwable t) {
            logger.error("移除hash缓存失败key[" + k + ", error[" + t + "]");
        }
        return false;
    }
    
    
    
    
    public boolean cacheList_db2(String k, String v) {
        try {
            ListOperations<String, String> listOps =  redisTemplate_db2.opsForList();
            listOps.rightPush(k, v);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }
    /**
     * 缓存value操作
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheValue_db2(String k, String v, long time) {
        try {
            ValueOperations<String, String> valueOps =  redisTemplate_db2.opsForValue();
            valueOps.set(k, v);
            if (time > 0) redisTemplate_db2.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }
    
    /**
     * 获取缓存
     * @param k
     * @return
     */
    public String getValue_db2(String k) {
        try {
            ValueOperations<String, String> valueOps =  redisTemplate_db2.opsForValue();
            return valueOps.get(k);
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }


    /**
     * 移除缓存
     * @param key
     * @return
     */
    public boolean remove_db2(String key) {
        try {
        	redisTemplate_db2.delete(key);
            return true;
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }
}
