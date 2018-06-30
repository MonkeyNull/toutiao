package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.apache.commons.collections.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setToId(model.getEntityOwnerId());
        message.setFromId(model.getActorId());
        User user = userService.getUser(model.getActorId());
        News news = newsService.selectById(model.getEntityId());
        message.setContent("用户" + user.getName() + "赞了你的贴子" + news.getTitle());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
