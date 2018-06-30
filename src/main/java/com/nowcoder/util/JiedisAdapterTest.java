package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.nowcoder.controller.NewsController;
import com.qiniu.util.Json;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;
@Component
public class JiedisAdapterTest {
    private  static final org.slf4j.Logger logger = LoggerFactory.getLogger(JiedisAdapterTest.class);
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
//        jedis.set("pv","100");
//        //对KEY的值进行加1
//        jedis.incr("pv");
//        //将key的值加上5
//        jedis.incrBy("pv",5);
//        System.out.println(jedis.keys("*"));
        //列表操作
//        String listName = "listA";
//        for (int i = 0;i < 10;i++){
//            jedis.lpush("listName","a"+String.valueOf(i));
//        }
        //将列表中的数据显示出来
//        System.out.println(jedis.lrange("listName",0,5));
//        //列表数据长度
//        System.out.println(jedis.llen("listName"));
        //往列表内插入一条数据
        //在a4后插入
//        jedis.linsert("listName",BinaryClient.LIST_POSITION.AFTER,"a4","xx");
        //在a4前插入
//        jedis.linsert("listName",BinaryClient.LIST_POSITION.BEFORE,"a4","bb");
//        System.out.println(jedis.lrange("listName",0,13));
        //set操作
//        String s1 = "news1";
//        String s2 = "news2";
//        for(int i = 0 ; i < 10;i++){
//            //向集合插入数值
//            jedis.sadd(s1,String.valueOf(i));
//            jedis.sadd(s2,String.valueOf(i*2));
//        }
        //打印集合的数值
//        System.out.println(jedis.smembers(s1));
//        System.out.println(jedis.smembers(s2));
//        //求交集
//        System.out.println(jedis.sinter(s1,s2));
//        //求并集
//        System.out.println(jedis.sunion(s1,s2));
//        //求差集
//        System.out.println(jedis.sdiff(s1,s2));
        String key = "demo";
        jedis.lpush(key,"1...");
        jedis.lpush(key,"2...");
        jedis.lpush(key,"3...");
        List<String> list= jedis.brpop(0,key);
        for (String s : list) {
            System.out.println(s);
        }
    }


//    private Jedis jedis = null;
//    private JedisPool pool = new JedisPool("localhost", 6379);
//
//    public void set(String key, String value) {
//        Jedis jedis = null;
//        try {
//            jedis = pool.getResource();
//            jedis.set(key, value);
//        } catch (Exception e) {
//            logger.error("发生异常" + e.getMessage());
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
//
//    public String get(String key){
//        Jedis jedis = null;
//        try{
//            jedis = pool.getResource();
//            return jedis.get(key);
//        }catch (Exception e){
//            logger.error("发生异常"+e.getMessage());
//            return null;
//        }finally {
//            jedis.close();
//        }
//    }
//    public void setObject(String key,Object obj){
//        set(key,JSON.toJSONString(obj));
//    }
//    public <T> T getObject(String key, Class<T> clazz){
//        String value = get(key);
//        if(value != null){
//            return JSON.parseObject(value, clazz);
//        }
//        return null;
//    }
}
