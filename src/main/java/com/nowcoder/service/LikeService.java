package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jiedisAdapter;

    /**
     * 如果喜欢返回1，如果不喜欢返回-1，否则返回0
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        if (jiedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisKey(entityId,entityType);
        return jiedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1 : 0;
    }
    public long like(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jiedisAdapter.sadd(likeKey,String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisKey(entityId,entityType);
        jiedisAdapter.srem(disLikeKey,String.valueOf(userId));

        return jiedisAdapter.scard(likeKey);
    }
    public long disLike(int userId,int entityType,int entityId){
        String disLikeKey = RedisKeyUtil.getDisKey(entityId,entityType);
        jiedisAdapter.sadd(disLikeKey,String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jiedisAdapter.srem(likeKey,String.valueOf(userId));

        return jiedisAdapter.scard(likeKey);
    }
}
