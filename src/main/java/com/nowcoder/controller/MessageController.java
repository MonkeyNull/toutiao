package com.nowcoder.controller;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.*;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.jws.WebParam;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private  static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        content = HtmlUtils.htmlEscape(content);
        try {
            Message message = new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(fromId);
            message.setToId(toId);
//            message.setConversationId(fromId < toId ? String.format("%d_%d",fromId,toId) : String.format("%d_%d",toId,fromId));
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(0,"发送消息成功");
        }catch (Exception e){
            logger.error("发送失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发送失败");
        }

    }
    @RequestMapping(path = {"/msg/detail"},method = RequestMethod.GET)
    public String conversationDetail(Model model, @Param("conversationId") String conversationId){
        try {
            List<Message> conversationlist = messageService.getConversationDetial(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for (Message msg : conversationlist) {
                ViewObject vo = new ViewObject();
                vo.set("message",msg);
                User user = userService.getUser(msg.getFromId());
                if(user == null)
                    continue;
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取详情消息失败" +e.getMessage());
        }
        return "letterDetail";

    }
    @RequestMapping(path = {"/msg/list"},method = RequestMethod.GET)
    public String conversationDetail(Model model){
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation",msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("target",user);
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error("获取站内信列表失败");
        }
        return "letter";
    }

    @RequestMapping(path = {"/delmessage"},method = RequestMethod.GET)
    public String delMessage(@Param("messageId") int messageId) {

        messageService.delMessage(messageId);

        return "redirect:/";
    }



}
