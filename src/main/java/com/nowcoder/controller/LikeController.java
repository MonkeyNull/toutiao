package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId){
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId,EntityType.ENTITY_NEWS,newsId);
        News news = newsService.selectById(newsId);
        //更新数据库news中的like字段
        newsService.updateLikeCount(newsId,(int )likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId())
        .setEntityType(EntityType.ENTITY_NEWS).setEntityId(newsId).setEntityOwnerId(news.getUserId()));
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
    @RequestMapping(path = {"/dislike"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId){
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId,EntityType.ENTITY_NEWS,newsId);
        //更新数据库news中的like字段
        newsService.updateLikeCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

}
