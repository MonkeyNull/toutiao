package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    private  static final org.slf4j.Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel model){
        try{
        String json = JSON.toJSONString(model);
        String key = RedisKeyUtil.getEventQueueKey();
        jedisAdapter.lpush(key,json);
        return true;
        }catch (Exception e) {
            logger.error("入队是发生异常", e.getMessage());
            return false;
        }
    }
}
