package com.nowcoder.util;

import com.nowcoder.controller.NewsController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.logging.Logger;

@Service
public class JedisAdapter implements InitializingBean {
    private  static final org.slf4j.Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool = null;
    @Override
    public void afterPropertiesSet() throws Exception{
        pool = new JedisPool("localhost",6379);
    }
    public Jedis getJedis(){
        return pool.getResource();
    }
    public long sadd(String key,String value){
        Jedis jedis= null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
           logger.error("发生异常",e.getMessage());
           return 0;
        }finally {
            jedis.close();
        }
    }
    public long srem(String key,String value){
        Jedis jedis= null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常",e.getMessage());
            return 0;
        }finally {
            jedis.close();
        }
    }
    public Boolean sismember(String key,String value){
        Jedis jedis= null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常",e.getMessage());
            return false;
        }finally {
            jedis.close();
        }
    }
    public long scard(String key){
        Jedis jedis= null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常",e.getMessage());
            return 0;
        }finally {
            jedis.close();
        }
    }
    public long lpush(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("发生异常",e.getMessage());
            return 0;
        }finally {
            jedis.close();
        }


    }
    public List<String> brpop(int timeout,String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
//            Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.error("出队产生错误");
            return null;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }
}
